@Business @HotelReservationSystemRegression

Feature: Hotel Room Booking
  As a visitor
  I want to search and book a hotel room
  So that I can reserve a stay at the Bed & Breakfast

  Background: create an auth token

  @Authentication
  Scenario: User successfully logs into the system
    When the user submits valid login credentials:
      | username | admin    |
      | password | password |
    Then the system should authenticate the user
    And the user should receive a valid session

    @positive
  Scenario Outline: Successful hotel room booking
    Given the user selects check-in date "<checkin>"
    And the user selects check-out date "<checkout>"
    And the user chooses "<roomType>" room
    When the user enters guest details
      | firstname   | lastname   |  email              | phone       |
      | <firstname> | <lastname> | <email>             | <phone>     |
    And the user submits the booking
    Then the booking should be successfully created
    And a confirmation with booking ID is displayed

    Examples:
      | checkin    | checkout   | roomType | firstname | lastname | email                  | phone       |
      | 2026-03-10 | 2026-03-12 | Single   | Alice     | Smith    | alice.smith@mail.com   | 1234567890  |
      | 2026-04-15 | 2026-04-18 | Double   | Bob       | Jones    | bob.jones@mail.com     | 9876543210  |


  @PostBooking
  Scenario: User verifies booking details after completion
    Given the user has completed a booking
    When the user views the booking confirmation
    Then the confirmation must show:
      | field           | value present? |
      | Booking ID      | Yes            |
      | Guest name      | Yes            |
      | Check-in date   | Yes            |
      | Check-out date  | Yes            |
      | Room type       | Yes            |
      | Total price     | Yes            |
