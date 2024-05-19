package com.example;

import io.cucumber.spring.CucumberContextConfiguration;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import com.example.service.DateService;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StepDefinitions {

    @MockBean
    private DateService dateService;

    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<String> response;

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

    // @Then("the response should be false if today is not Friday")
    // public void the_response_should_be_false_if_today_is_not_friday() {
    //     boolean isNotFriday = LocalDate.now().getDayOfWeek() != DayOfWeek.FRIDAY;
    //     assertEquals(Boolean.toString(isNotFriday), response.getBody());
    // }

}