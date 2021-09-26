package com.payoneer.jobexecutor.services.impl;

import com.payoneer.jobexecutor.domain.models.Job;
import com.payoneer.jobexecutor.domain.models.JobStatus;
import com.payoneer.jobexecutor.drivers.kafka.JobPublisher;
import com.payoneer.jobexecutor.services.api.JobExecutor;
import com.payoneer.jobexecutor.services.api.JobManager;
import com.payoneer.jobexecutor.services.exception.JobException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import static com.payoneer.jobexecutor.domain.models.JobStatus.FAILED;
import static com.payoneer.jobexecutor.domain.models.JobStatus.RUNNING;
import static com.payoneer.jobexecutor.domain.models.JobStatus.SUCCESS;

@Service
@RequiredArgsConstructor
public class JobManagerImpl implements JobManager {

    private final List<JobExecutor> executors;

    private final JobPublisher publisher;

    @Override
    public Job run(@NotNull Job job) {
        try {
            JobExecutor executor = executors.stream()
                    .filter(e -> e.isMappingFor(job.getType()))
                    .findFirst()
                    .orElseThrow(() -> new JobException("There is no a executor for job of type " + job.getType()));
            executor.execute(job);
            changeJobStatusAndSendToBroker(job, SUCCESS, "success");
        } catch (Exception e) {
            changeJobStatusAndSendToBroker(job, FAILED, e.getMessage());
        }

        return job;
    }

    private void changeJobStatusAndSendToBroker(Job job, JobStatus status, String message) {
        if (List.of(SUCCESS, FAILED).contains(status)) {
            job.setFinishedAt(LocalDateTime.now());
        } else if (RUNNING.equals(status)) {
            job.setStartedAt(LocalDateTime.now());
        }

        job.setStatus(status);
        job.setMessage(message);

        publisher.publish(job);
    }
}
