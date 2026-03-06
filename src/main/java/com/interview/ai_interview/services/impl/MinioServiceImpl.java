package com.interview.ai_interview.services.impl;

import java.io.File;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.interview.ai_interview.services.MinioService;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    @Value("${minio.url}")
    private String internalUrl;

    @Value("${minio.public-url}")
    private String publicUrl;

    @Override
    public String uploadFile(MultipartFile file) {
        try {

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            return fileName;

        } catch (Exception e) {
            throw new RuntimeException("Failed upload file to MinIO", e);
        }
    }

    @Override
    public File downloadToTempFile(String objectName) {

        try {

            String tempDir = System.getProperty("java.io.tmpdir");
            String fileName = "minio-" + UUID.randomUUID() + ".mp4";

            File tempFile = new File(tempDir, fileName);

            minioClient.downloadObject(
                    io.minio.DownloadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .filename(tempFile.getAbsolutePath())
                            .build()
            );

            return tempFile;

        } catch (Exception e) {

            throw new RuntimeException("Failed to download file from MinIO", e);

        }
    }

    @Override
    public String getPresignedUrl(String objectName) {

        try {
            String internalPresignedUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(60 * 60)
                            .build()
            );

            return internalPresignedUrl.replace(
                "http://minio:9000/" + bucketName,
                publicUrl + "/storage"
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed generate presigned url", e);
        }
    }
}