@booking-creation @hotel-reservation-system-regression

Feature: Hotel Room Booking
  As a visitor
  I want to book a hotel room
  So that I can reserve a stay

  Background: create an auth token
    When the user submits valid login credentials:
      | username | password |
      | admin    | password |
    Then the system should authenticate the user
    And the user should receive a valid session

  @positive @successful-hotel-booking
  Scenario Outline: Successful hotel room booking
    Given user gives checkin date as "<checkin>" and checkout date as "<checkout>"
    And user tries to create a booking:
      | firstname   | lastname   | email   | phone   | depositpaid   |
      | <firstname> | <lastname> | <email> | <phone> | <depositpaid> |
    And the user submits the booking
    Then the booking should be successfully created
    And a confirmation with booking ID is displayed
    And the response matches with json schema "getBookingDetails.json"

    Examples:
      | checkin    | checkout   | firstname | lastname | email                | phone       | depositpaid |
      | 2026-03-10 | 2026-03-12 | Alice     | Smith    | alice.smith@mail.com | 91234567890 | false       |
      | 2026-04-15 | 2026-04-18 | Bob       | Jones    | bob.jones@mail.com   | 99876543210 | true        |

  @error-validation @negative @incorrect-field-values
  Scenario Outline: create a booking with incorrect field values
    Given user gives checkin date as "<checkin>" and checkout date as "<checkout>"
    And user tries to create a booking:
      | firstname   | lastname   | email   | phone   | depositpaid   |
      | <firstname> | <lastname> | <email> | <phone> | <depositpaid> |
    And the user submits the booking
    Then the user gets "<FieldError>" error message


    Examples:
      | firstname | lastname | email            | phone        | checkin    | checkout   | FieldError                          | depositpaid |
      |           | Name     | user1@gmail.com  | 98989898980  | 2025-03-15 | 2026-01-18 | Firstname should not be blank       | false       |
      | First     | W        | last2@gmail.com  | 98989898980  | 2025-03-15 | 2026-01-18 | size must be between 3 and 30       | false       |
      | First     | LAST     | user3            | 879558797034 | 2025-03-15 | 2026-01-18 | must be a well-formed email address | false       |
      | First     | last     | user4@gmail.com  | 8795587970   | 2025-03-15 | 2026-01-18 | size must be between 11 and 21      | false       |
      | First     | last     | user.5@gmail.com | 98989898980  |            | 2026-01-18 | must not be null                    | false       |

  @checkout-before-checkin @error-validation
  Scenario Outline: create a booking when user give checkout date as earlier than check in date
    Given user gives checkin date as "<checkin>" and checkout date as "<checkout>"
    When user tries to create a booking:
      | firstname   | lastname   | email   | phone   | depositpaid   |
      | <firstname> | <lastname> | <email> | <phone> | <depositpaid> |
    And the user submits the booking
    Then the user gets "<FieldError>" error message
    And the user is unable to create a booking
    Examples:
      | firstname | lastname | email           | phone       | checkin    | checkout   | FieldError               | depositpaid |
      | Test      | Name     | user1@gmail.com | 98989898980 | 2026-03-12 | 2026-02-10 | Failed to create booking | false       |
      | First     | Www      | last2@gmail.com | 98989898980 | 2026-03-11 | 2026-02-18 | Failed to create booking | false       |
