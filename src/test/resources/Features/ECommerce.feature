@Ecommerce
Feature: RSA Emcommerce
  Background:
    Given Launch the Ecommerce base url

  @CreateaToken
  Scenario: Create a Token to Authorize
    When Create a token to authorize
    Then Validate the Emcommerce status code 200

  @AddaProduct
  Scenario: Create a new product
    When Add the product
    Then Validate the Emcommerce status code 201
    And Validate that the product is created

  @CreateOrder
  Scenario: Create a order
    When Create a new order
    Then Validate the Emcommerce status code 201
    And Validate that the order is created
  @ViewOrderDetails
    Scenario: view the order details
      When Fetch the order details
      Then Validate the Emcommerce status code 200

  @DeletingtheProduct
  Scenario: Delete the product
    And Delete the product
    Then Validate the Emcommerce status code 200
    And Validate that the product is deleted
