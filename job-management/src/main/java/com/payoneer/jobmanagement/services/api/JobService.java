package com.payoneer.jobmanagement.services.api;

import com.payoneer.jobmanagement.entities.Job;
import com.payoneer.jobmanagement.entities.JobStatus;

import java.util.List;

public interface JobService {
    Job update(Job job);
    Job create(Job job);
    Job get(Long id);
    List<Job> getJobsReadyToExecute();
    List<Job> getJobsByStatus(JobStatus status);
 }
