package com.interview.ai_interview.services;

import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

public interface AnswerService {
    void uploadAnswer(MultipartFile video, UUID questionId, UUID userId);
}
