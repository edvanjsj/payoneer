package com.payoneer.jobexecutor.services.impl;

import com.payoneer.jobexecutor.domain.models.Job;
import com.payoneer.jobexecutor.domain.models.JobType;
import com.payoneer.jobexecutor.services.api.JobExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SendEmailJobExecutor implements JobExecutor {
    @Override
    public void execute(Job job) {
        // Send email job implementation
        log.info("Sending email with the following params: \n {}", job.getParams());
    }

    @Override
    public boolean isMappingFor(JobType jobType) {
        return JobType.SEND_EMAIL.equals(jobType);
    }
}
