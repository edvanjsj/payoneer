package com.payoneer.jobexecutor.domain.models;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class Job {
    private Long id;
    private JobType type;
    private JobStatus status;
    private LocalDateTime queuedAt;
    private LocalDateTime scheduledTo;
    private Integer priority;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private String message;
    private Map<String, String> params;
}
