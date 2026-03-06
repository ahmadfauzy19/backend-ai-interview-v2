package com.interview.ai_interview.jobs;

import com.interview.ai_interview.models.Answer;
import com.interview.ai_interview.repositories.AnswerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;


@Slf4j
@Service
@RequiredArgsConstructor
public class SttJobService {

    private final AnswerRepository answerRepository;
    private final SttProcessor sttProcessor;
    private final SttTransactionService transactionService;
    private final Executor sttExecutor;

    public void dispatchPendingJobs() {

        List<Answer> jobs =
                answerRepository.findPendingForProcessing(PageRequest.of(0, 1));

        for (Answer answer : jobs) {

            sttExecutor.execute(() -> {

                UUID lockedId =
                        transactionService.lockAndMarkProcessing(answer.getId());

                if (lockedId != null) {
                    sttProcessor.processHeavy(lockedId);
                }

            });
        }
    }
}