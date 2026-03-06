package com.interview.ai_interview.jobs;

import com.interview.ai_interview.models.Answer;
import com.interview.ai_interview.models.TranscriptStatusEnum;
import com.interview.ai_interview.repositories.AnswerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SttTransactionService {

    private final AnswerRepository answerRepository;

    @Transactional
    public UUID lockAndMarkProcessing(UUID answerId) {

        Answer answer = answerRepository.findByIdForUpdate(answerId)
                .orElseThrow();

        if (answer.getStatus() != TranscriptStatusEnum.PENDING) {
            return null;
        }

        answer.setStatus(TranscriptStatusEnum.PROCESSING);
        answerRepository.save(answer);

        return answer.getId();
    }
}
