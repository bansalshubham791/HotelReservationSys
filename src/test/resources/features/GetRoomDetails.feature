@GetRoomDetails @HotelReservationSystemRegression
Feature: View hotel room information
  In order to help guests make informed booking decisions
  As a hotel booking platform
  I want to provide accurate and complete room details


  Background: create an auth token
    When the user submits valid login credentials:
      | username | admin    |
      | password | password |
    Then the system should authenticate the user
    And the user should receive a valid session

  @Positive @HappyFlow @RoomDetails
  Scenario: Guest views details of an available hotel room
    Given the hotel offers rooms for booking
    When the guest requests details for room number 1
    Then the system should provide the room information for room number 1
    And the room should have a name and description
    And the room should display its price per night
    And the room should list the amenities available to the guest
  @Negative @ErrorValidation @NonExistentRoom
  Scenario: Guest attempts to view details of a Nonon-existent room
    Given the hotel offers rooms for booking
    When the guest requests details for a room that does not exist like 100
    Then the system should inform the guest that the room could not be found



