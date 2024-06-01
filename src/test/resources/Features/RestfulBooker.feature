@RestfulBooker
Feature: Restful Booker
  Background:
    Given Launch the RestfulBooker base URL
@createToken
  Scenario: Generate a token to authorize
    When Create a token
    Then Validate the status code 200
  @CreateBooking
  Scenario Outline: Create a booking
    When Create a new booking with "<firstname>" "<lastname>" "<totalprice>" "<depositpaid>" "<checkin>" "<checkout>" "<additionalneeds>"
    Then Validate the status code 200
    And Validate the booking is created with the given details
    Examples:
      |firstname |lastname |totalprice |depositpaid |checkin     |checkout    |additionalneeds |
      |Carmel    |Crooks   |500        |true        |2024-06-01  |2024-06-01  |Dinner          |


@GetBookingIds
  Scenario: Get Booking Ids
    When Fetch the all the Ids
    Then Validate the status code 200
@GetBookingDetails
  Scenario: Get the booking details of the created user
    When Fetch the user details
    Then Validate the status code 200
    And Validate the retrieved booking matches the Created booking
  @UpdateBookingDetails
  Scenario: Update the Booking details
    When Update the booking details
      |firstname  |lastname   |totalprice   |depositpaid  |checkin      |checkout     |additionalneeds  |
      |Carmel     |Crooks     |999          |true         |2024-05-01   |2024-06-01   | Lunch           |
    Then Validate the status code 200
    And Validate the booking details are updated

  @PartiallyUpdateBookingDetails
  Scenario: partially Update the Booking details
    When partially update the booking details
    Then Validate the status code 200
    And Validate the booking details are updated partially
@DeleteCreatedBooking
  Scenario:delete the Booking details
    When delete the booking details of the created user
    Then Validate the delete status code 201
    And Validate the booking details are deleted

