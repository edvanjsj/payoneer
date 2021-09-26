package com.payoneer.jobmanagement.drivers.http.dto;

import com.payoneer.jobmanagement.entities.Job;
import com.payoneer.jobmanagement.utils.DateUtil;
import lombok.Data;

@Data
public class JobDTO {
    private Long id;
    private String type;
    private String status;
    private String queuedAt;
    private String startedAt;
    private String finishedAt;
    private String scheduledTo;

    public static JobDTO of(Job job) {
        if (job == null) {
            return null;
        }

        JobDTO jobDTO = new JobDTO();
        jobDTO.setId(jobDTO.getId());
        jobDTO.setType(job.getType());
        jobDTO.setStatus(job.getStatus().name());
        jobDTO.setQueuedAt(DateUtil.toString(job.getQueuedAt()));
        jobDTO.setStartedAt(DateUtil.toString(job.getStartedAt()));
        jobDTO.setFinishedAt(DateUtil.toString(job.getFinishedAt()));
        jobDTO.setScheduledTo(DateUtil.toString(job.getScheduledTo()));

        return jobDTO;
    }

    public Job toEntity() {
        Job job = new Job();
        job.setId(id);
        job.setType(type);
        job.setQueuedAt(DateUtil.toLocalDateTime(queuedAt));
        job.setStartedAt(DateUtil.toLocalDateTime(startedAt));
        job.setFinishedAt(DateUtil.toLocalDateTime(finishedAt));
        job.setScheduledTo(DateUtil.toLocalDateTime(scheduledTo));

        return job;
    }
}
