package com.payoneer.jobexecutor.services.api;

import com.payoneer.jobexecutor.domain.models.Job;
import com.payoneer.jobexecutor.domain.models.JobType;

public interface JobExecutor {
    void execute(Job job);
    boolean isMappingFor(JobType jobType);
}
