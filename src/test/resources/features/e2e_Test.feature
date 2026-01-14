@business @hotel-reservation-system-regression @e2e
Feature: Hotel Room Booking
  As a visitor
  I want to search and book a hotel room
  So that I can reserve a stay

  Background: create an auth token

  @positive @complete-flow
  Scenario Outline: Successful hotel room booking
    Given the user submits valid login credentials:
      | username | admin    |
      | password | password |
    Then the system should authenticate the user
    And the user should receive a valid session
    Then the room is available for the desired dates
      | firstname   | lastname   |  email              | phone       | checkin   | checkout    | depositpaid |
      | <firstname> | <lastname> | <email>             | <phone>     | <checkin> | <checkout>  | <depositpaid> |
    And the user submits the booking
    Then the booking should be successfully created with response code 201
    And a confirmation with booking ID is displayed
    When the user updates the existing booking with the following details:
      | roomid   | firstname   | lastname   | depositpaid   | checkin   | checkout  |
      | 34        | Lion        | Dear      | false        | 2026-03-11           | 2026-03-12  |
    Then the booking should be successfully updated with response code 200
    And a booking exists with booking id
    When the user cancels the booking
    Then the booking should be successfully cancelled with response code 200
    And the booking should no longer be retrievable

    Examples:
      | checkin    | checkout   | firstname | lastname | email                  | phone       |  depositpaid |
      | 2026-03-10 | 2026-03-12 | Alice     | Smith    | alice.smith@mail.com   | 91234567890  |  false     |
      | 2026-04-15 | 2026-04-18 | Bob       | Jones    | bob.jones@mail.com     | 99876543210  |  true      |

