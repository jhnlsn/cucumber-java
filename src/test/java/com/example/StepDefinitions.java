package com.example;

import io.cucumber.spring.CucumberContextConfiguration;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Given;
import io.cucumber.java.After;
import io.cucumber.java.Before;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.example.service.DateService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(partitions = 1, topics = {"isFridayTopic"})
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
                                  "spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"})
public class StepDefinitions {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private Consumer<String, String> consumer;

    @MockBean
    private DateService dateService;

    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<String> response;

    @Before("@EmbeddedKafka")
    public void setUp() {
        Map<String, Object> consumerProperties =
            KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker);

        ConsumerFactory<String, String> consumerFactory =
            new DefaultKafkaConsumerFactory<>(consumerProperties);

        consumer = consumerFactory.createConsumer();
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, "isFridayTopic");
    }

    @After("@EmbeddedKafka")
    public void tearDown() {
        consumer.close();
    }

    @Given("today is {string}")
    public void today_is_friday(String expectedDayOfWeek) {
        when(dateService.getToday()).thenReturn(DayOfWeek.valueOf(expectedDayOfWeek.toUpperCase()));
    }

    @When("I make a GET request to {string}")
    public void i_make_a_get_request_to(String path) {
        response = restTemplate.getForEntity("/isFriday", String.class);
    }

    @Then("the response should be {string}")
    public void the_response_should_be_true_if_today_is_friday(String responseExpected) {
        assertEquals(responseExpected, response.getBody());
    }

    @When("I make a GET request to {string} with")
    public void i_make_a_get_request_to_with_emitEvent_set_to_true(String path){
        response = restTemplate.getForEntity("/isFriday?emitEvent=true", String.class);
    }

    @Then("an event should be emitted to Kafka")
    public void an_event_should_be_emitted_to_kafka() {
        // Verify that the KafkaTemplate#send method was called
        ConsumerRecord<String, String> received = KafkaTestUtils.getSingleRecord(consumer, "isFridayTopic");
        assertEquals("true", received.value());
    }

}