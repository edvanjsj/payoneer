package com.payoneer.jobmanagement.services.impl;

import com.payoneer.jobmanagement.entities.Job;
import com.payoneer.jobmanagement.repositories.JobRepository;
import com.payoneer.jobmanagement.services.exceptions.JobException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class JobServiceImplTest {

    @Mock
    private JobRepository mock_jobRepository;

    @InjectMocks
    private JobServiceImpl jobService;

    @Test
    void create_usingJobWithIdInfo_shouldNotUpdateAnExistingJob() {
        Job job = mockJobWithId();
        assertThrows(JobException.class, () -> jobService.create(job));
    }

    @Test
    void create_usingJobWithoutIdInfo_shouldCreateJob() {
        Mockito.when(mock_jobRepository.save(Mockito.any())).thenReturn(mockJobWithId());

        Job createdJob = jobService.create(new Job());

        assertThat(createdJob).isNotNull();
        assertThat(createdJob.getId()).isEqualTo(1L);
    }


    Job mockJobWithId() {
        Job job = new Job();
        job.setId(1L);

        return job;
    }
}