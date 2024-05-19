Feature: Check if today is Friday
  Scenario: Get /isFriday
    Given today is "<today>"
    When I make a GET request to "/isFriday"
    Then the response should be "<response>"
  
  Examples:
    | today   | response |
    | friday  | true     |
    | monday  | false    |
    | tuesday | false    |
    | wednesday | false  |
    | thursday | false   |
    | saturday | false   |
    | sunday   | false   |