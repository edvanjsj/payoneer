package com.payoneer.jobexecutor.drivers.kafka;

import com.payoneer.jobexecutor.domain.models.Job;
import com.payoneer.jobmanagement.drivers.kafka.JobEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobPublisher {
    @Value("${kafka.job.return.topic-name}")
    private String topic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(@NotNull Job job) {
        kafkaTemplate.send(topic, job.getId().toString(), toJobEvent(job))
                     .addCallback((e) -> log.info("event sent to topic"),
                                  (e) -> log.error("error sending event to topic"));
    }

    private JobEvent toJobEvent(Job job) {
        return JobEvent.newBuilder()
                        .setId(job.getId())
                        .setType(job.getType().name())
                        .setStatus(job.getStatus().name())
                        .setQueuedAt(job.getQueuedAt())
                        .setPriority(job.getPriority())
                        .setStartedAt(job.getStartedAt())
                        .setFinishedAt(job.getFinishedAt())
                        .setScheduledTo(job.getScheduledTo())
                        .setMessage(job.getMessage())
                        .setParams(job.getParams())
                        .build();
    }
}
