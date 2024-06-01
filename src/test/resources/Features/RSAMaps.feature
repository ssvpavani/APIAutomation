@RSAMaps
Feature: RSA Maps
  Background:
    Given Launch the Maps base URL
  @AddPlace
  Scenario Outline: Add a new place
    When Create a new Place with "<lat>" "<lng>" "<accuracy>" "<name>" "<phone_number>" "<address>" "<types>" "<website>" "<language>"
    Then Validate the Maps status code 200
    And Validate the scope

    Examples:
      |lat       |lng          |accuracy  |name               |phone_number       |address                            |types                |website             |language|
      |-38.383494|33.427362    |50        |Frontline house    |(+91) 983 893 3937 |1234street,plano,Texas             |shoe park, shop      |http://google.com   |French-IN|
  @GetPlace
  Scenario: Fetch the address added
    When Fetch the Location details
    Then Validate the Maps status code 200
    And Validate the retrieved address matches the Created address

@UpdatePlace
  Scenario: Update the address
    When Update the address details
    Then Validate the Maps status code 200
    And Validate the address details are updated

@DeletePlace
  Scenario:Delete the address details
    When delete the address details
    Then Validate the Maps status code 200
    And Validate the address details are deleted