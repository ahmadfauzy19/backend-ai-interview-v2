package com.interview.ai_interview.controllers;

import com.interview.ai_interview.services.AnswerService;
import com.interview.ai_interview.utils.CustomUserDetail;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/api/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<String> uploadAnswer(
            @RequestParam("video") MultipartFile video,
            @RequestParam("questionId") UUID questionId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();

        UUID userId = user.getId();
        answerService.uploadAnswer(video, questionId, userId);
        return ResponseEntity.ok("Berhasil upload jawaban");
    }

    @GetMapping("/list-candidate")
    public ResponseEntity<?> listCandidate() {
        return ResponseEntity.ok(answerService.getCandidateList());
    }

    @GetMapping("/candidate-result/{candidateId}")
    public ResponseEntity<?> candidateResult(
            @PathVariable UUID candidateId
    ) {
        return ResponseEntity.ok(answerService.getCandidateResult(candidateId));
    }

    @GetMapping("/list-candidate/{interviewId}")
    public ResponseEntity<?> listCandidate(
            @PathVariable UUID interviewId
    ) {
        return ResponseEntity.ok(
                answerService.getCandidateListByInterviewId(interviewId)
        );
    }
}