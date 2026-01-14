@BookingCancellation @HotelReservationSystemRegression
Feature: Cancel hotel booking
  As a guest
  I want to cancel an existing booking
  So that the room becomes available again

  Background: create an auth token
    When the user submits valid login credentials:
      | username | admin    |
      | password | password |
    Then the system should authenticate the user
    And the user should receive a valid session
    Then the user enters guest details
  | firstname | lastname |  email              | phone          | checkin   | checkout    | depositpaid |
  | James     | Dear     | james.dear@gmail.com | 989898989877  | 2026-05-10 | 2026-05-12 | false        |
    And the user submits the booking
    Then the booking should be successfully created with response code 201

  @Positive
  Scenario: Cancel an existing booking successfully
    Given a booking exists with booking id
    When the user cancels the booking
    Then the booking should be successfully cancelled with response code 200
    And the booking should no longer be retrievable

  @Security
  Scenario: Cancel booking without authentication
    Given a booking exists with booking id
    When the user attempts to cancel the booking without authentication
    Then the booking should not be successfully cancelled with response code 401
    And the user should see an error message "Authentication required"

  @Negative
  Scenario: Cancel a non-existent booking
    Given no booking exists with booking id "99999"
    When the user cancels the booking
    Then the booking should not be successfully cancelled with response code 500
    And the user should see an error message "Failed to delete booking"