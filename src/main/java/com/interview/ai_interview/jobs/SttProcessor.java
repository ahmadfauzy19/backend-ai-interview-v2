package com.interview.ai_interview.jobs;

import com.interview.ai_interview.models.Answer;
import com.interview.ai_interview.models.TranscriptStatusEnum;
import com.interview.ai_interview.repositories.AnswerRepository;
import com.interview.ai_interview.services.MinioService;
import com.interview.ai_interview.client.SttClient;
import com.interview.ai_interview.utils.Ffmpeg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SttProcessor {

    private final AnswerRepository answerRepository;
    private final SttClient sttClient;
    private final MinioService minioService;

    public void processHeavy(UUID answerId) {

        File tempVideo = null;
        File tempAudio = null;

        try {
            Answer answer = answerRepository.findById(answerId).orElseThrow();

            tempVideo = minioService.downloadToTempFile(answer.getAudioPath());
            tempAudio = File.createTempFile("audio-", ".wav");

            Ffmpeg.extractAudio(
                    tempVideo.getAbsolutePath(),
                    tempAudio.getAbsolutePath()
            );

            String transcript = sttClient.transcribe(tempAudio);

            updateSuccess(answerId, transcript);

        } catch (Exception e) {
            log.error("STT FAILED {}", answerId, e);
            handleRetry(answerId);
        } finally {
            if (tempVideo != null && tempVideo.exists()) tempVideo.delete();
            if (tempAudio != null && tempAudio.exists()) tempAudio.delete();
        }
    }

    @Transactional
    protected void updateSuccess(UUID id, String transcript) {
        Answer answer = answerRepository.findById(id).orElseThrow();
        answer.setTranscript(transcript);
        answer.setStatus(TranscriptStatusEnum.DONE);
        answerRepository.save(answer);
    }

    @Transactional
    protected void handleRetry(UUID id) {
        Answer answer = answerRepository.findById(id).orElseThrow();

        int retry = answer.getRetryCount() == null ? 0 : answer.getRetryCount();

        if (retry < 3) {
            answer.setRetryCount(retry + 1);
            answer.setStatus(TranscriptStatusEnum.PENDING);
        } else {
            answer.setStatus(TranscriptStatusEnum.FAILED);
        }

        answerRepository.save(answer);
    }
}
