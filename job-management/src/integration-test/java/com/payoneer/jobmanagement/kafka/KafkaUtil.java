package com.payoneer.jobmanagement.kafka;

import com.payoneer.jobmanagement.IntegrationTestSpring;
import com.payoneer.jobmanagement.drivers.kafka.JobEvent;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class KafkaUtil implements IntegrationTestSpring {
    @Autowired
    private KafkaTemplate<String, Object> kafkaProducer;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private static Consumer<String, JobEvent> consumer;

    protected void closeKafkaProducer() {
        if (kafkaProducer != null) {
            kafkaProducer.destroy();
        }
    }

    protected Consumer<String, JobEvent> createConsumer(final String topic) {
        if (consumer == null) {
            final DefaultKafkaConsumerFactory<String, JobEvent> factory = new DefaultKafkaConsumerFactory<>(getConsumerProperties());
            consumer = factory.createConsumer();
            consumer.subscribe(Collections.singletonList(topic));
        }

        return consumer;
    }

    protected void sendMessage(final String topic, final String key, final JobEvent message) {
        try {
            final ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(topic, key, message);
            kafkaProducer.send(producerRecord).get(5, TimeUnit.SECONDS);
        } catch (final Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> getConsumerProperties() {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString(),
                ConsumerConfig.GROUP_ID_CONFIG, "consumer",
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true",
                ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "10",
                ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "60000",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
                KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true",
                "schema.registry.url", "mock://mockSchemaRegister");
    }
}
