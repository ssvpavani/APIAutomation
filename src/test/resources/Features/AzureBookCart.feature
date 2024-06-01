@BookCart
Feature: Login to the Bookcart and performing http methods
  Background:
    Given Launch the AzureBookCart base url

  @AddingitemtoCart
  Scenario Outline: Adding the item to the cart
    When Add the item to the cart with "<userId>" "<firstName>" "<lastName>" "<username>" "<password>" "<gender>" "<userTypeId>"
    Then Validate the statuscode 200
  Examples:
    |userId   |firstName  |lastName |username   |password   |gender|userTypeId|
    |10       |Stern      |David    |sterndavid |stern@123  |male  |1         |
  @GetListofItems @Regression
  Scenario: Getting the list of items in the cart
    When Fetch the items in the cart
    Then Validate the statuscode 200

  @reducingItemsQuantity
  Scenario: Reduces the quantity by one for an item in shopping cart
    When update the cart by reducing an item
    Then Validate the statuscode 200
    And Validate the quantity is reduced

  @DeletingAllItemsinCart
  Scenario: Deleting all the items in the cart
    And Delete all items in the cart
    Then Validate the statuscode 200
    And  Validate the cartitems  are deleted