package com.interview.ai_interview.jobs;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;

import org.springframework.stereotype.Service;

import com.interview.ai_interview.models.GradingJob;
import com.interview.ai_interview.models.GradingStatusEnum;
import com.interview.ai_interview.models.InterviewParticipant;
import com.interview.ai_interview.models.TranscriptStatusEnum;
import com.interview.ai_interview.repositories.AnswerRepository;
import com.interview.ai_interview.repositories.GradingJobRepository;
import com.interview.ai_interview.repositories.InterviewParticipantRepository;
import com.interview.ai_interview.repositories.QuestionRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GradingServiceJob {
    private final AnswerRepository answerRepository;
    private final GradingJobRepository gradingJobRepository;
    private final GradingProcessorJob gradingProcessorJob;
    private final InterviewParticipantRepository participantRepository;
    private final QuestionRepository questionRepository;
    private final Executor gradingExecutor;

    @Transactional
    public void dispatchPendingJobs() {
        // 1. Ambil kandidat partisipan yang belum punya grading job
        List<InterviewParticipant> candidates = participantRepository.findParticipantsWithoutGradingJob();
        System.out.println("Found " + candidates.size() + " participants without grading job");

        gradingExecutor.execute(() -> {
            for (InterviewParticipant participant : candidates) {
                try {
                    // 2. Hitung total soal di interview tersebut
                    long totalQuestions = questionRepository.countByInterviewId(participant.getInterview().getId());
    
                    // 3. Hitung jawaban yang sudah 'DONE'
                    long completedAnswers = answerRepository.countByParticipantIdAndStatus(participant.getId(),TranscriptStatusEnum.DONE);
                    System.out.println("Participant ID: " + participant.getId() + ", Total Questions: " + totalQuestions + ", Completed Answers: " + completedAnswers);
    
                    // 4. Validasi: Jika semua soal sudah dijawab
                    if (totalQuestions > 0 && totalQuestions == completedAnswers) {
                        System.out.println("Creating grading job for participant ID: " + participant.getId());
                        createJob(participant.getId());
                    }else{
                        continue;
                    }
                    
                } catch (Exception e) {
                    log.error("Failed to process participant ID: {}", participant.getId(), e);
                }
            }
    
            gradingProcessorJob.processAllPendingGrading();
        });
    }

    private void createJob(UUID participantId) {
        GradingJob job = new GradingJob();
        job.setParticipant(participantRepository.findById(participantId)
                .orElseThrow(() -> new EntityNotFoundException("Participant not found with ID: " + participantId)));
        job.setStatus(GradingStatusEnum.PENDING);
        job.setRetryCount(0);
        job.setCreatedAt(LocalDateTime.now());
        
        gradingJobRepository.save(job);
        log.info("Grading job created for participant ID: {}", participantId);
    }
}
