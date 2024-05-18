package hellocucumber;

import io.cucumber.java.en.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StepDefinitions {

    private String today;
    private String actualAnswer;

    @Given("today is {string}")
    public void today_is_sunday(String day) {
        // Write code here that turns the phrase above into concrete actions
        // throw new io.cucumber.java.PendingException();
        this.today = day;
    }
    @When("I ask whether it's {string} yet")
    public void i_ask_whether_it_s_friday_yet(String expectedResult) {
        // Write code here that turns the phrase above into concrete actions
        // throw new io.cucumber.java.PendingException();
        this.actualAnswer = expectedResult.equals(today) ? "Yes" : "Nope";
    }
    @Then("I should be told {string}")
    public void i_should_be_told(String expectedAnswer) {
        // Write code here that turns the phrase above into concrete actions
        // throw new io.cucumber.java.PendingException();
        assertEquals(expectedAnswer, actualAnswer);
    }

}
