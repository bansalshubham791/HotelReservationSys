@booking-edit @hotel-reservation-system-regression
Feature: Edit hotel booking
  As a registered user
  I want to edit an existing booking
  So that I can update guest and stay details

  Background: create an auth token
    When the user submits valid login credentials:
      | username | admin    |
      | password | password |
    Then the system should authenticate the user
    And the user should receive a valid session

  @happy-flow @update-booking
  Scenario Outline: Update an existing booking
    Given the room is available for the desired dates
      | firstname   | lastname   |  email              | phone       | checkin   | checkout    | depositpaid |
      | <firstname> | <lastname> | <email>             | <phone>     | <checkin> | <checkout>  | <depositpaid> |
    And the user submits the booking
    Then the booking should be successfully created with response code 201
    When the user updates the existing booking with the following details:
      | roomid   | firstname   | lastname   | depositpaid   | checkin   | checkout  |
      | 34      | Lion  | Dear    | false        | 2026-03-11 | 2026-03-12 |
    Then the booking should be successfully updated with response code 200
    Examples:
      | firstname | lastname | email              | phone       | checkin      | checkout   | depositpaid |
      | Drax      | Eight     | drax.Eight@gmail.com| 98989898984 | 2026-02-22 | 2026-02-23 | true        |

