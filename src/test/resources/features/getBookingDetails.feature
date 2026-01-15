@hotel-reservation-system-regression   @get-booking-details

Feature: Get Booking details
  As a visitor
  I want to check the booking details

  Background: create an auth token
    When the user submits valid login credentials:
      | username | password |
      | admin    | password |
    Then the system should authenticate the user
    And the user should receive a valid session

  @get-booking-detail  @schema-validation
  Scenario Outline: Get the details of the room by room id
    Given the user wants to check the room details
    When the user asks the details of the room by:
      | <roomid> |
    Then details of the room is available:
      | roomName  |
      | roomPrice |
    And the response matches with json schema "getRoomDetails.json"

    Examples:
      | roomid |
      | 1      |
      | 2      |

  @get-room-summary
  Scenario Outline: Get the room booking summary
    Given the user wants to check the room booking summary
    When the user asks the room booking summary for roomid"<roomid>"
    Then the room booking summary response should be successful

    Examples:
      | roomid |
      | 1      |
      | 2      |

  @security @negative @deny-booking-details
  Scenario: Unauthorized user attempts to retrieve booking details
    Given the user is not authenticated
    When the user asks the room booking summary for roomid 1
    Then the system should deny access
    And the user should see an auth error message "Authentication required"

  @negative @invalid-room-booking-details
  Scenario: Attempt to retrieve booking details using an invalid room ID
    Given the user wants to check the room booking summary
    When the user asks the room booking summary for roomid 100
    Then the system should not show any valid room


