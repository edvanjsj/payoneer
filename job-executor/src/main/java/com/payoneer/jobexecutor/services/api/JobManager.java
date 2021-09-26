package com.payoneer.jobexecutor.services.api;

import com.payoneer.jobexecutor.domain.models.Job;

public interface JobManager {
    Job run(Job job);
}
