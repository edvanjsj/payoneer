package com.payoneer.jobmanagement.drivers.kafka;

import com.payoneer.jobmanagement.entities.Job;
import com.payoneer.jobmanagement.entities.JobStatus;
import com.payoneer.jobmanagement.services.api.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JobConsumer {

    private final JobService jobService;

    @Transactional
    @KafkaListener(topics = "${kafka.job.return.topic-name}")
    void onEvent(final JobEvent jobEvent) {
        final Job job = jobService.get(jobEvent.getId());
        updateValues(job, jobEvent);
        jobService.update(job);
    }

    void updateValues(Job job, JobEvent event) {
        job.setStatus(JobStatus.valueOf(event.getStatus()));
        job.setFinishedAt(LocalDateTime.now());
        job.setMessage(event.getMessage());
    }
}
