package com.payoneer.jobmanagement.repositories;

import com.payoneer.jobmanagement.entities.Job;
import com.payoneer.jobmanagement.entities.JobStatus;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobRepository extends PagingAndSortingRepository<Job, Long> {
    List<Job> findByStatusAndScheduledToLessThanEqualOrderByPriorityAscScheduledToAsc(JobStatus status, LocalDateTime dateTime);
    List<Job> findByStatus(JobStatus status);
}
