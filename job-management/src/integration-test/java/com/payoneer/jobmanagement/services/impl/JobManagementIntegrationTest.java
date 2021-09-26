package com.payoneer.jobmanagement.services.impl;

import com.payoneer.jobmanagement.drivers.kafka.JobEvent;
import com.payoneer.jobmanagement.drivers.kafka.JobPublisher;
import com.payoneer.jobmanagement.entities.Job;
import com.payoneer.jobmanagement.entities.JobStatus;
import com.payoneer.jobmanagement.kafka.KafkaUtil;
import com.payoneer.jobmanagement.services.api.JobService;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Test suite on Job management")
class JobManagementIntegrationTest extends KafkaUtil {
    @Value("${kafka.job.topic-name}")
    private String producerTopic;

    @Value("${kafka.job.return.topic-name}")
    private String receiverTopic;

    private Consumer<String, JobEvent> consumer;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JobManagerImpl jobManager;

    @BeforeEach
    void setUp() {
        consumer = createConsumer(producerTopic);
    }

    @AfterEach
    void clearDatabase() {
        jdbcTemplate.execute("DELETE FROM job;");
    }

    @Test
    @Transactional
    @Sql(
            scripts = {"file:src/integration-test/resources/test-data/queued-jobs.sql"},
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)
    )
    @DisplayName("Running with many jobs stored should return only two queued jobs")
    void run_withManyJobsStored_shouldReturnOnlyTwoQueuedJobs() {
        jobManager.run();

        int TIMEOUT_MS = 20000;
        ConsumerRecords<String, JobEvent> records = KafkaTestUtils.getRecords(consumer, TIMEOUT_MS);

        List<JobEvent> jobs = new ArrayList<>();
        records.forEach(record -> jobs.add(record.value()));

        assertThat(jobs).hasSize(2);
        assertThat(jobs).allSatisfy(job -> List.of(1, 6).contains(job.getId()));
    }

    @Nested
    @DisplayName("Test suite on job priorities")
    class JobPriorityTest {
        @Test
        @Transactional
        @Sql(
                scripts = {"file:src/integration-test/resources/test-data/priority-jobs.sql"},
                config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)
        )
        @DisplayName("Running with many jobs stored should return ordered by priority and date")
        void run_withManyJobsStored_shouldReturnOrderedByPriorityAndDate() {
            jobManager.run();

            int TIMEOUT_MS = 20000;
            ConsumerRecords<String, JobEvent> records = KafkaTestUtils.getRecords(consumer, TIMEOUT_MS);

            List<JobEvent> jobs = new ArrayList<>();
            records.forEach(record -> jobs.add(record.value()));
            List<Long> retrievedIds = jobs.stream().map(j -> j.getId()).collect(Collectors.toList());

            assertThat(retrievedIds).containsExactly(-4L, -1L, -3L, -2L, -5L, -6L);
        }
    }

    @Nested
    @DisplayName("Test suite on job execution return")
    class JobExcutionReturnTest {

        @Autowired
        private JobService jobService;

        @Test
        @Transactional
        @Sql(
                scripts = {"file:src/integration-test/resources/test-data/running-jobs.sql"},
                config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)
        )
        @DisplayName("Job execution return received should return job status")
        void jobExecutionReturnReceived_shouldUpdateJobStatus() {
            List<Job> runningJobs = jobService.getJobsByStatus(JobStatus.RUNNING);
            List<Long> runningJobsIds = runningJobs.stream().map(j -> j.getId()).collect(Collectors.toList());

            sendJobFinishedEvents(runningJobs);

            List<Job> updatedJobs = jobService.getJobsByStatus(JobStatus.SUCCESS);
            Long[] updatedJobsIds = updatedJobs.stream().map(j -> j.getId()).toArray(Long[]::new);

            assertThat(runningJobsIds).containsExactly(updatedJobsIds);
        }

        @SneakyThrows
        private void sendJobFinishedEvents(List<Job> jobs) {
            List<JobEvent> events = jobs.stream().map(j -> {
                JobEvent e = JobPublisher.toJobEvent(j);
                e.setStatus("SUCCESS");
                e.setMessage("success");
                return e;
            }).collect(Collectors.toList());

            events.forEach(event -> {
                sendMessage(receiverTopic, String.valueOf(event.getId()), event);
            });

            Thread.sleep(2000);
        }
    }
}
