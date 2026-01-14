@BookingCreation @HotelReservationSystemRegression

Feature: Hotel Room Booking
  As a visitor
  I want to book a hotel room
  So that I can reserve a stay

  Background: create an auth token
    When the user submits valid login credentials:
      | username | admin    |
      | password | password |
    Then the system should authenticate the user
    And the user should receive a valid session

  @Positive @SuccessfulHotelBooking
  Scenario Outline: Successful hotel room booking
    Given the room is available for the desired dates
      | firstname   | lastname   |  email              | phone       | checkin   | checkout    | depositpaid |
      | <firstname> | <lastname> | <email>             | <phone>     | <checkin> | <checkout>  | <depositpaid> |
    And the user submits the booking
    Then the booking should be successfully created with response code 201
    And a confirmation with booking ID is displayed
    And the response matches with json schema "booking.json"

    Examples:
      | checkin    | checkout   | firstname | lastname | email                  | phone       |  depositpaid |
      | 2026-03-10 | 2026-03-12 | Alice     | Smith    | alice.smith@mail.com   | 91234567890  |  false     |
      | 2026-04-15 | 2026-04-18 | Bob       | Jones    | bob.jones@mail.com     | 99876543210  |  true      |

  @ErrorValidation @Negative @IncorrectFieldValues
  Scenario Outline: create a booking with incorrect field values
    Given the room is available for the desired dates
      | firstname   | lastname   |  email              | phone       | checkin   | checkout    | depositpaid |
      | <firstname> | <lastname> | <email>             | <phone>     | <checkin> | <checkout>  | <depositpaid> |
    And the user submits the booking
    Then the user should see response with incorrect "<FieldError>"

    Examples:
      | firstname | lastname | email               | phone        | checkin    | checkout   | FieldError                          | depositpaid |
      |           | Name     | user1@gmail.com      | 98989898980 | 2025-03-15 | 2026-01-18 | Firstname should not be blank       | false       |
      | First     | W        | last2@gmail.com      | 98989898980 | 2025-03-15 | 2026-01-18 | size must be between 3 and 30       | false       |
      | First     | LAST     | user3                | 879558797034 | 2025-03-15 | 2026-01-18 | must be a well-formed email address |  false       |
      | First     | last     | user4@gmail.com      | 8795587970   | 2025-03-15 | 2026-01-18 | size must be between 11 and 21      |  false       |
      | First     | last     | user.5@gmail.com     | 98989898980 |            | 2026-01-18 | must not be null                    | false       |