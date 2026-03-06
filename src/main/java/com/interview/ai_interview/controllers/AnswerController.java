package com.interview.ai_interview.controllers;

import java.io.File;
import java.util.UUID;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.interview.ai_interview.services.AnswerService;
import com.interview.ai_interview.services.MinioService;
import com.interview.ai_interview.utils.CustomUserDetail;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;
    private final MinioService minioService;

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

    @PreAuthorize("hasRole('ADMIN') or hasRole('INTERVIEWER')")
    @GetMapping("/candidate-result/{candidateId}")
    public ResponseEntity<?> candidateResult(
            @PathVariable UUID candidateId
    ) {
        return ResponseEntity.ok(answerService.getCandidateResult(candidateId));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('INTERVIEWER')")
    @GetMapping("/list-candidate/{interviewId}")
    public ResponseEntity<?> listCandidate(
            @PathVariable UUID interviewId
    ) {
        return ResponseEntity.ok(
                answerService.getCandidateListByInterviewId(interviewId)
        );
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadVideo(
            @PathVariable String fileName
    ) {

        File videoFile = minioService.downloadToTempFile(fileName);

        Resource resource = new FileSystemResource(videoFile);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
}