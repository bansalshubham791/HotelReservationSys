@get-booking-report @hotel-reservation-system-regression
Feature: Generate hotel booking report
  In order to monitor hotel performance and occupancy
  As a hotel manager
  I want to generate a booking report
  So that I can review booking and revenue information

  Background: create an auth token
    When the user submits valid login credentials:
      | username | admin    |
      | password | password |
    Then the system should authenticate the user
    And the user should receive a valid session

    @get-report @hotel-management
  Scenario: Hotel manager generates a booking report
    Given the hotel has booking data available
    When the hotel manager requests the booking report
    Then the system should generate the booking report

