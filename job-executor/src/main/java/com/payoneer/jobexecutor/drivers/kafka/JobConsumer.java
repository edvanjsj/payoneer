package com.payoneer.jobexecutor.drivers.kafka;

import com.payoneer.jobexecutor.domain.models.Job;
import com.payoneer.jobexecutor.domain.models.JobStatus;
import com.payoneer.jobexecutor.domain.models.JobType;
import com.payoneer.jobexecutor.services.api.JobManager;
import com.payoneer.jobmanagement.drivers.kafka.JobEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JobConsumer {

    private final JobManager jobManager;

    @Transactional
    @KafkaListener(topics = "${kafka.job.topic-name}")
    void onEvent(final JobEvent jobEvent) {
        final Job job = toJob(jobEvent);
        jobManager.run(job);
    }

    Job toJob(JobEvent event) {
        Job job = new Job();
        job.setId(event.getId());
        job.setType(JobType.valueOf(event.getType()));
        job.setStatus(JobStatus.valueOf(event.getStatus()));
        job.setQueuedAt(event.getQueuedAt());
        job.setPriority(event.getPriority());
        job.setScheduledTo(event.getScheduledTo());
        job.setStartedAt(event.getStartedAt());
        job.setFinishedAt(LocalDateTime.now());
        job.setMessage(event.getMessage());
        job.setParams(event.getParams());

        return job;
    }
}
