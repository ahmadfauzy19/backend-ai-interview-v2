package com.interview.ai_interview.services.impl;

import com.interview.ai_interview.models.Answer;
import com.interview.ai_interview.models.Candidate;
import com.interview.ai_interview.models.Interview;
import com.interview.ai_interview.models.InterviewParticipant;
import com.interview.ai_interview.models.Question;
import com.interview.ai_interview.models.TranscriptStatusEnum;
import com.interview.ai_interview.repositories.AnswerRepository;
import com.interview.ai_interview.repositories.CandidateRepository;
import com.interview.ai_interview.repositories.InterviewParticipantRepository;
import com.interview.ai_interview.repositories.QuestionRepository;
import com.interview.ai_interview.services.AnswerService;
import com.interview.ai_interview.services.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;
import java.io.File;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final MinioService minioService;
    private final QuestionRepository questionRepository;
    private final CandidateRepository candidateRepository;
    private final InterviewParticipantRepository participantRepository;
    private final AnswerRepository answerRepository;
//     private final SttClient sttClient;

    @Override
    @Transactional
    public void uploadAnswer(MultipartFile file, UUID questionId, UUID userId) {
        File tempVideo = null;
        File tempAudio = null;
        try {
                Question question = questionRepository.findById(questionId)
                        .orElseThrow(() -> new RuntimeException("Question not found"));

                Candidate candidate = candidateRepository.findByUserId(userId)
                        .orElseThrow(() -> new RuntimeException("Candidate not found"));

                Interview interview = question.getInterview();

                InterviewParticipant participant = participantRepository
                        .findByInterviewIdAndCandidateId(interview.getId(), candidate.getId())
                        .orElseGet(() -> {
                                InterviewParticipant newParticipant = InterviewParticipant.builder()
                                        .interview(interview)
                                        .candidate(candidate)
                                        .startedAt(LocalDateTime.now())
                                        .build();
                                return participantRepository.save(newParticipant);
                        });

                // Upload ke MinIO
                String storedFileName = minioService.uploadFile(file);

                // Save Answer tanpa transcript
                Answer answer = Answer.builder()
                        .participant(participant)
                        .question(question)
                        .audioPath(storedFileName)
                        .status(TranscriptStatusEnum.PENDING)
                        .retryCount(0)
                        .createdAt(LocalDateTime.now())
                        .build();

                answerRepository.save(answer);
                // System.out.println("Received file: " + file.getOriginalFilename());
                // // Ambil Question
                // Question question = questionRepository.findById(questionId)
                //         .orElseThrow(() -> new RuntimeException("Question not found"));

                // // Ambil Candidate dari userId
                // Candidate candidate = candidateRepository.findByUserId(userId)
                //         .orElseThrow(() -> new RuntimeException("Candidate not found"));

                // // Ambil Interview dari Question
                // Interview interview = question.getInterview();

                // // Cari atau buat InterviewParticipant
                // InterviewParticipant participant = participantRepository
                //         .findByInterviewIdAndCandidateId(interview.getId(), candidate.getId())
                //         .orElseGet(() -> {
                //                 InterviewParticipant newParticipant = InterviewParticipant.builder()
                //                         .interview(interview)
                //                         .candidate(candidate)
                //                         .startedAt(LocalDateTime.now())
                //                         .build();
                //                 return participantRepository.save(newParticipant);
                //         });

                // // Upload file ke MinIO
                // String storedFileName = minioService.uploadFile(file);

                // // ===============================
                // // Create temp files
                // // ===============================
                // tempVideo = File.createTempFile("video-", ".mp4");
                // tempAudio = File.createTempFile("audio-", ".wav");

                // file.transferTo(tempVideo);

                // // ===============================
                // // Extract Audio
                // // ===============================
                // Ffmpeg.extractAudio(
                //         tempVideo.getAbsolutePath(),
                //         tempAudio.getAbsolutePath()
                // );

                // // ===============================
                // // Send to STT Service
                // // ===============================
                // String transcriptAnswer = sttClient.transcribe(tempAudio);


                // // Simpan Answer
                // Answer answer = Answer.builder()
                //         .participant(participant)
                //         .question(question)
                //         .audioPath(storedFileName)
                //         .transcript(transcriptAnswer)
                //         .createdAt(LocalDateTime.now())
                //         .build();

                // answerRepository.save(answer);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload answer: " + e.getMessage(), e);
        } finally {
            // Hapus temp files
            if (tempVideo != null && tempVideo.exists()) {
                tempVideo.delete();
            }
            if (tempAudio != null && tempAudio.exists()) {
                tempAudio.delete();
            }
        }

    }
}
