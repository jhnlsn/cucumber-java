package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;

import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(partitions = 1, topics = {"isFridayTopic"})
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
                                  "spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"})
public class IsFridayEmbeddedKafkaTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Test
    public void whenIsFridayCalled_EmbeddedKafkaTest_thenKafkaEventIsProduced() {
        // Call the /isFriday endpoint

        // Create the consumer
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        Consumer<String, String> consumer = new DefaultKafkaConsumerFactory<String, String>(consumerProps).createConsumer();

        consumer.subscribe(Collections.singletonList("isFridayTopic"));

        // Define a Duration time to wait for 5 seconds
        Duration duration = Duration.ofSeconds(5);
        var records = KafkaTestUtils.getRecords(consumer, duration, 1);

        // Check that the event was produced
        assertThat(records.count()).isEqualTo(0);

        consumer.close();
    }
}