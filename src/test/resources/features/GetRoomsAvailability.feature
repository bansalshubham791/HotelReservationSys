@GetRoomAvailability @HotelReservationSystemRegression
Feature: Search for available hotel rooms
  In order to plan a hotel stay
  As a guest
  I want to see which rooms are available for my selected dates

  Background: create an auth token
    When the user submits valid login credentials:
      | username | admin    |
      | password | password |
    Then the system should authenticate the user
    And the user should receive a valid session

  @GetRoomList
  Scenario: Guest searches for available rooms for a valid stay period
    Given the hotel accepts bookings for future dates
    When the guest searches for available rooms with a check-in date of "2026-02-17"
    And the check-out date is "2026-02-18"
    Then the system should return the list of available rooms
