package com.interview.ai_interview.services;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;

public interface MinioService {
    String uploadFile(MultipartFile file);
    File downloadToTempFile(String objectName);
    String getPresignedUrl(String objectName);
}
