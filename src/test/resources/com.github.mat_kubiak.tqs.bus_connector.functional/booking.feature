Feature: Booking a ticket

  Scenario: Book a valid ticket
    Given I'm on the Bus Selector Website
    When I search for trips from "Prague" to "Rome" on "2024-04-18"
    When I select the first ticket
    When I book a ticket with name "Ana" and surname "Pereira"
    Then I should see a ticket confirmation

  Scenario: Fail at booking a ticket for a past date
    Given I'm on the Bus Selector Website
    When I search for trips from "Prague" to "Rome" on "2000-04-18"
    Then I should be redirected to home page
