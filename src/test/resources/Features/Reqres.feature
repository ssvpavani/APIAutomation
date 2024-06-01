@Reqres
Feature: Reqres API
  Background:
    Given Launch the Reqres base URL

  @createUser 
  Scenario Outline: Create a new user
    When Create a new user with "<name>" "<job>"
    Then Validate the reqrespost status code 201
    And Validate the response name
    Examples:
      |name    | job                 |
      |Sasi    |  QAEngineer         |
  @GetUser
  Scenario: Get user Details
    When Fetch the user
    Then Validate the reqres status code 200
  @UpdateuserDetails
  Scenario: Update the user details
    When Update the user details
    Then Validate the reqres status code 200
    And Validate the Updated job

  @PartiallyUpdateBookingDetails
  Scenario: partially Update the Booking details
    When partially update the user details
    Then Validate the reqres status code 200
    And Validate the PartialUpdated job

  @DeleteCreatedUser
  Scenario:delete the created user
    When delete the created user
    Then Validate the reqresdelete status code 204

