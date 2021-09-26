package com.payoneer.jobmanagement.services.impl;

import com.payoneer.jobmanagement.drivers.kafka.JobPublisher;
import com.payoneer.jobmanagement.entities.Job;
import com.payoneer.jobmanagement.entities.JobStatus;
import com.payoneer.jobmanagement.services.api.JobManager;
import com.payoneer.jobmanagement.services.api.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import static com.payoneer.jobmanagement.entities.JobStatus.FAILED;
import static com.payoneer.jobmanagement.entities.JobStatus.RUNNING;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobManagerImpl implements JobManager {
    private final JobService jobService;
    private final JobPublisher jobPublisher;

    // every 10 seconds the scheduler will look for jobs that are ready to run
    @Scheduled(initialDelay = 20000, fixedRate = 10000)
    @Transactional
    @Override
    public void run() {
        final List<Job> jobs = jobService.getJobsReadyToExecute();

        // First of all we need change all statuses before running any of the the jobs so that scheduler doesn't get the same job more than once
        jobs.forEach(job -> changeJobStatus(job, RUNNING, "running"));

        jobs.forEach(job -> doProcess(job));
    }

    private Job doProcess(@NotNull Job job) {
        try {
            jobPublisher.publish(job);
        } catch (Exception e) {
            changeJobStatus(job, FAILED, e.getMessage());
            e.printStackTrace();
        }

        return job;
    }

    private void changeJobStatus(Job job, JobStatus status, String message) {
        if (FAILED.equals(status)) {
            job.setFinishedAt(LocalDateTime.now());
        } else if (RUNNING.equals(status)) {
            job.setStartedAt(LocalDateTime.now());
        }

        job.setStatus(status);
        job.setMessage(message);
        jobService.update(job);
    }
}
