Feature: Is it Friday yet?
  Everybody wants to know when it's Friday

  Scenario: Sunday isn't Friday
    Given today is "<today>"
    When I ask whether it's Friday yet
    Then I should be told "<isFriday>"

  Examples:
    | today    | isFriday |
    | Monday   | Nope     |
    | Tuesday  | Nope     |
    | Wednesday| Nope     |
    | Thursday | Nope     |
    | Friday   | Yes      |
    | Saturday | Nope     |
    | Sunday   | Nope     |