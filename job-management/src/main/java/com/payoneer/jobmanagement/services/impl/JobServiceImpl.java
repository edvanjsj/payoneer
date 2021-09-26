package com.payoneer.jobmanagement.services.impl;

import com.payoneer.jobmanagement.entities.Job;
import com.payoneer.jobmanagement.entities.JobStatus;
import com.payoneer.jobmanagement.repositories.JobRepository;
import com.payoneer.jobmanagement.services.api.JobService;
import com.payoneer.jobmanagement.services.exceptions.JobException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.List;

import static com.payoneer.jobmanagement.entities.JobStatus.QUEUED;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;

    @Override
    public Job create(@NotNull Job job) {
        if (job.getId() != null) {
            throw new JobException("You cannot update an existing job. Please remove id info if you want to create a new job.");
        }

        job.setStatus(QUEUED);
        job.setQueuedAt(LocalDateTime.now());

        if (job.getScheduledTo() == null) {
            job.setScheduledTo(job.getQueuedAt());
        }

        return jobRepository.save(job);
    }

    @Override
    public Job update(Job job) {
        return jobRepository.save(job);
    }

    @Override
    public Job get(@NotNull Long id) {
        return jobRepository.findById(id)
                            .orElseThrow(() -> new JobException("Job with id " + id + " not found"));
    }

    @Override
    public List<Job> getJobsReadyToExecute() {
        return jobRepository.findByStatusAndScheduledToLessThanEqualOrderByPriorityAscScheduledToAsc(QUEUED, LocalDateTime.now().withNano(999999999));
    }

    @Override
    public List<Job> getJobsByStatus(JobStatus status) {
        return jobRepository.findByStatus(status);
    }
}
