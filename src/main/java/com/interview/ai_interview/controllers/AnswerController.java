package com.interview.ai_interview.controllers;

import com.interview.ai_interview.services.AnswerService;
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

    private final AnswerService answerService;

@PostMapping(
        value = "/upload",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
)
public ResponseEntity<String> uploadAnswer(
        @RequestParam("video") MultipartFile video,
        @RequestParam("questionId") UUID questionId,
        @RequestParam("userId") UUID userId
) {
    answerService.uploadAnswer(video, questionId, userId);
    return ResponseEntity.ok("Berhasil upload jawaban");
}
}