@booking-cancellation @hotel-reservation-system-regression
Feature: Cancel hotel booking
  As a guest
  I want to cancel an existing booking
  So that the room becomes available again

  Background: create an auth token
    When the user submits valid login credentials:
      | username | password |
      | admin    | password |
    Then the system should authenticate the user
    And the user should receive a valid session


  @positive @cancel-booking
  Scenario: Cancel an existing booking successfully
    Given user gives checkin date as "2026-03-14" and checkout date as "2026-03-15"
    When user tries to create a booking:
      | firstname | lastname | email          | phone       | depositpaid |
      | Test      | Shubham  | abcd@gmail.com | 98989898984 | false       |
    And the user submits the booking
    Then the booking should be successfully created
    And a booking exists with booking id
    When the user cancels the booking
    Then the booking should be successfully cancelled
    And the booking should no longer be retrievable

  @security
  Scenario: Cancel booking without authentication
    Given user gives checkin date as "2026-03-11" and checkout date as "2026-03-12"
    When user tries to create a booking:
      | firstname | lastname | email         | phone       | depositpaid |
      | Shubham   | Test     | abc@gmail.com | 98989898987 | false       |
    And the user submits the booking
    Then the booking should be successfully created
    And a booking exists with booking id
    When the user attempts to cancel the booking without authentication
    Then the booking should not be successfully cancelled
    And the user should see an error message "Authentication required"

  @negative @booking-cancel-error-validation
  Scenario: Cancel a non-existing booking
    Given no booking exists with booking id "99999"
    Then the user should see an error message "Failed to delete booking"
    And the booking should not be cancelled

