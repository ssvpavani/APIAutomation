@StoreRestAPI
Feature:StoreRestAPI
  Background:
    Given Launch the StoreRestAPI base URL

  @CreateProduct
  Scenario Outline: Create a new product
    When Create a new product with "<title>" "<price>" "<description>" "<category>"
    Then Validate the StoreRestAPI status code 201
    And Validate the product is created with the given details
    Examples:
      |title           |price   |description                        |category            |
      |HP Laptop       |30000   |Thin & Light, Dual Speakers        |Electronics         |

  @GetAllProducts
  Scenario: Fetch all the Products
    When Fetch all the Products
    Then Validate the getall StoreRestAPI status code 200

  @GetProduct
  Scenario: Get the Product
    When Fetch the Single Product
    Then Validate the getall StoreRestAPI status code 200
    And Validate the retrieved product matches the Created product


  @UpdateBookingDetails
  Scenario: Update the Product details
    When Update the Product details
      |title           |price   |description                        |category                   |
      |Dell Laptop     |40000   |Premium Thin and Light Laptop      |612e42d755b07f20de9ec6a5   |
    Then Validate the put StoreRestAPI status code 202
    And Validate the Product details are updated

  @DeleteCreatedBooking
  Scenario:delete the Booking details
    When delete the product details
    Then Validate the delete StoreRestAPI status code 202
    And Validate the product details are deleted

