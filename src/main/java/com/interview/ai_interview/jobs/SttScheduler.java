package com.interview.ai_interview.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class SttScheduler {

    private final SttJobService sttJobService;

    @Scheduled(fixedDelay = 5000)
    public void schedule() {
        sttJobService.dispatchPendingJobs();
    }
}
