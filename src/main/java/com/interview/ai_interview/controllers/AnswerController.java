package com.interview.ai_interview.controllers;

import com.interview.ai_interview.models.Answer;
import com.interview.ai_interview.models.Interview;
import com.interview.ai_interview.models.InterviewParticipant;
import com.interview.ai_interview.models.Question;
import com.interview.ai_interview.models.Candidate;
import com.interview.ai_interview.repositories.AnswerRepository;
import com.interview.ai_interview.repositories.InterviewParticipantRepository;
import com.interview.ai_interview.repositories.QuestionRepository;
import com.interview.ai_interview.repositories.CandidateRepository;
import com.interview.ai_interview.services.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;
import org.springframework.http.MediaType;


@RestController
@RequestMapping("/api/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final MinioService minioService;
    private final InterviewParticipantRepository participantRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final CandidateRepository candidateRepository;

    @PostMapping(
        value = "/upload",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<String> uploadAnswer(
            @RequestParam("video") MultipartFile video,
            @RequestParam("questionId") UUID questionId
    ) {
        try {

            // // 1️⃣ Ambil question
            // Question question = questionRepository
            //         .findById(questionId)
            //         .orElseThrow(() -> new RuntimeException("Question not found"));

            // // 2️⃣ Ambil interview dari question
            // Interview interview = question.getInterview();

            // // 3️⃣ Dummy candidate (sementara ambil 1 saja)
            // Candidate candidate = candidateRepository.findAll()
            //         .stream()
            //         .findFirst()
            //         .orElseThrow(() -> new RuntimeException("No candidate found"));

            // // 4️⃣ Buat InterviewParticipant dummy
            // InterviewParticipant participant = InterviewParticipant.builder()
            //         .interview(interview)
            //         .candidate(candidate)
            //         .startedAt(LocalDateTime.now())
            //         .totalScore(null) // kosong
            //         .finalRecommendation(null) // kosong
            //         .build();

            // participantRepository.save(participant);

            // 5️⃣ Upload video ke MinIO
            String storedFileName = minioService.uploadFile(video);

            // // 6️⃣ Simpan Answer
            // Answer answer = Answer.builder()
            //         .participant(participant)
            //         .question(question)
            //         .audioPath(storedFileName)
            //         .createdAt(LocalDateTime.now())
            //         .build();

            // answerRepository.save(answer);

            return ResponseEntity.ok("Berhasil upload jawaban");

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Gagal upload jawaban: " + e.getMessage());
        }
    }
}