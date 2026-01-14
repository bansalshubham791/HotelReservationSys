@HotelReservationSystemRegression   @GetRoomDetails

Feature: Get Booking details
  As a visitor
  I want to check the booking details

Background: create an auth token
When the user submits valid login credentials:
| username | admin    |
| password | password |
Then the system should authenticate the user
And the user should receive a valid session

  @GetRoomDetails  @SchemaValidation
  Scenario Outline: Get the details of the room by room id
    Given the user wants to check the room details
    When the user asks the details of the room by:
    | <roomid>  |
    Then details of the room is available:
    | roomName  |
    | roomPrice |
    And the response matches with json schema "room.json"

    Examples:
      | roomid |
      | 1      |
      | 2      |

  @GetRoomSummary
  Scenario Outline: Get the room booking summary
    Given the user wants to check the room booking summary
    When the user asks the room booking summary for "<roomid>"
    Then the room booking summary response should be successful

    Examples:
      | roomid |
      | 1      |
      | 2      |