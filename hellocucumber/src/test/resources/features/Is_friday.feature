Feature: Check if today is Friday
  Scenario: Get /isFriday
    When I make a GET request to "/isFriday"
    Then the response should be true if today is Friday