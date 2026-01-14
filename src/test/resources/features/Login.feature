@Login @HotelReservationSystemRegression
Feature: Hotel booking login
  In order to access the hotel booking system
  As a guest or hotel manager
  I want to log in using valid credentials

  @Authentication
  Scenario: create an auth token
  When the user submits valid login credentials:
  | username | admin    |
  | password | password |
  Then the system should authenticate the user
  And the user should receive a valid session

    @ErrorValidation @LoginAuthentication
  Scenario Outline: User attempts login with different credentials
    Given the user attempts to log in to the hotel booking system
    When the user submits the following credentials
      | username | password |
      | <username> | <password> |
    Then the system should return the response "<response>"

    Examples:
      | username   | password   | response  |
      | admin1     | password   | 401       |
      | admin      | Password12 | 401       |