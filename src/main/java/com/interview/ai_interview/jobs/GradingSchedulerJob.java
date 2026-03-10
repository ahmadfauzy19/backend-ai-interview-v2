package com.interview.ai_interview.jobs;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GradingSchedulerJob {
    private final GradingServiceJob gradingServiceJob;

    @Scheduled(fixedDelay = 5000)
    public void schedule() {
        gradingServiceJob.dispatchPendingJobs();
    }
    
}
