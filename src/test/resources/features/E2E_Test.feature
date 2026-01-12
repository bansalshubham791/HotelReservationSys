@Business @HotelReservationSystemRegression

Feature: Hotel Room Booking
  As a visitor
  I want to search and book a hotel room
  So that I can reserve a stay

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
    Given the user enters guest details
      | firstname   | lastname   |  email              | phone       |  roomType    | checkin   | checkout    |
      | <firstname> | <lastname> | <email>             | <phone>     |  <roomType>  | <checkin> | <checkout>  |
    And the user submits the booking
    Then the booking should be successfully created with response code 201
    And a confirmation with booking ID is displayed

    Examples:
      | checkin    | checkout   | roomType | firstname | lastname | email                  | phone       |
      | 2026-03-10 | 2026-03-12 | 1         | Alice     | Smith    | alice.smith@mail.com   | 91234567890  |
      | 2026-04-15 | 2026-04-18 | 2         | Bob       | Jones    | bob.jones@mail.com     | 99876543210  |
