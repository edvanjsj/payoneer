package com.payoneer.jobexecutor;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

@SpringBootTest(
        classes = JobExecutorApplication.class,
        properties = {"spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}", "spring.jpa.properties.hibernate.enable_lazy_load_no_trans=true"}
)
@EmbeddedKafka(partitions = 1, topics = { "${kafka.job.return.topic-name}" })
public interface IntegrationTestSpring {
}
