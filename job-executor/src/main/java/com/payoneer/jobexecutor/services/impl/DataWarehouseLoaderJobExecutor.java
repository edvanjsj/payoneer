package com.payoneer.jobexecutor.services.impl;

import com.payoneer.jobexecutor.domain.models.Job;
import com.payoneer.jobexecutor.domain.models.JobType;
import com.payoneer.jobexecutor.services.api.JobExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DataWarehouseLoaderJobExecutor implements JobExecutor {
    @Override
    public void execute(Job job) {
        // Load data into DW job implementation
        log.info("Loading data into DW with the following params: \n {}", job.getParams());
    }

    @Override
    public boolean isMappingFor(JobType jobType) {
        return JobType.DATA_WAREHOUSE_LOADER.equals(jobType);
    }
}
