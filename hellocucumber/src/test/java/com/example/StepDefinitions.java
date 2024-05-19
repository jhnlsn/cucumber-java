package com.example;

import io.cucumber.spring.CucumberContextConfiguration;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<String> response;

    @When("I make a GET request to {string}")
    public void i_make_a_get_request_to(String path) {
        response = restTemplate.getForEntity("/isFriday", String.class);
    }

    @Then("the response should be true if today is Friday")
    public void the_response_should_be_true_if_today_is_friday() {
        boolean isFriday = LocalDate.now().getDayOfWeek() == DayOfWeek.FRIDAY;
        assertEquals(Boolean.toString(isFriday), response.getBody());
    }

}