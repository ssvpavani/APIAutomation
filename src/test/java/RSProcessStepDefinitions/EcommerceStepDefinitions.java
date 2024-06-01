package RSProcessStepDefinitions;
import PojoClasses.LoginRequest;
import PojoClasses.Orders;
import PojoClasses.orderDetails;
import Utilities.ReadConfig;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;
public class EcommerceStepDefinitions {
    RequestSpecification requestspecification;
    public static Response response;
    Response deleteresponse;
   public static String token;
   public static String userId;
   public static String ProductID;
   public static String orderID;
   String msg;
    @Given("Launch the Ecommerce base url")
    public void launch_the_ecommerce_base_url() {
        String debugmode = ReadConfig.getdebugmode();
        if (debugmode.equals("on")) {
            baseURI = ReadConfig.getEcommerce_url();
        } else {
            baseURI = System.getProperty("url");
        }
    }
    @When("Create a token to authorize")
    public void create_a_token_to_authorize() {
        requestspecification = RestAssured.given().contentType(ContentType.JSON);
        LoginRequest loginrequest = new LoginRequest();
        loginrequest.setUserEmail("ssvpavani9390@gmail.com");
        loginrequest.setUserPassword("Sasi@1998");
        requestspecification.body(loginrequest);
        response = requestspecification.post("/api/ecom/auth/login");
        response.then().log().all();
        token = response.jsonPath().getString("token");
        userId = response.jsonPath().getString("userId");
        Assertions.assertNotNull(token, "Token is null");
        Assertions.assertNotNull(userId, "User ID is null");
    }
    @Then("Validate the Emcommerce status code {int}")
    public void validate_the_emcommerce_status_code(Integer expectedStatusCode) {
        int ActualStatusCode=response.statusCode();
        Assertions.assertEquals(expectedStatusCode,ActualStatusCode);
    }

    @When("Add the product")
    public void add_the_product() {
        requestspecification = RestAssured.given();
        requestspecification.param("productName", "Laptop");
        requestspecification.param("productAddedBy", userId);
        requestspecification.param("productCategory", "fashion");
        requestspecification.param("productSubCategory", "shirts");
        requestspecification.param("productPrice", "31150");
        requestspecification.param("productDescription", "HP");
        requestspecification.param("productFor", "women");
        requestspecification.multiPart("productImage", new File("C://Users//ssathi//Desktop//laptop.jpg"));
        requestspecification.header("authorization", token);
        requestspecification.contentType(ContentType.MULTIPART);
        response = requestspecification.post("/api/ecom/product/add-product");
        response.then().log().all();
        ProductID = response.jsonPath().getString("productId");
        msg=response.jsonPath().getString("message");
    }

    @And("Validate that the product is created")
    public void validateThatTheProductIsCreated() {
        String responseBody = response.getBody().asString();
        Assert.assertTrue("Product is not created", responseBody.contains("Product Added Successfully"));
    }
    @When("Create a new order")
    public void createANewOrder() {
        requestspecification = RestAssured.given();
        requestspecification.header("authorization", token);
        requestspecification.contentType(ContentType.JSON);
        orderDetails orderDetails = new orderDetails();
        orderDetails.setCountry("India");
        orderDetails.setProductOrderedId(ProductID);
        List<orderDetails> orderDetailsList = new ArrayList<>();
        orderDetailsList.add(orderDetails);
        Orders orders = new Orders();
        orders.setOrders(orderDetailsList);
        requestspecification.body(orders);
        response = requestspecification.request(Method.POST, "/api/ecom/order/create-order");
        response.then().log().all();
        orderID=response.jsonPath().getString("orders[0]");

    }

    @And("Validate that the order is created")
    public void validateThatTheOrderIsCreated() {
        String responseBody = response.getBody().asString();
        Assert.assertTrue("Product is not created", responseBody.contains("Order Placed Successfully"));

    }
    @When("Fetch the order details")
    public void fetchTheOrderDetails() {
        requestspecification = RestAssured.given();
        requestspecification.header("authorization", token);
        requestspecification.queryParam("id",orderID );
        response = requestspecification.request(Method.GET,"/api/ecom/order/get-orders-details");
        response.then().log().all();
    }
    @Given("Delete the product")
    public void delete_the_product() {
        requestspecification = RestAssured.given();
        requestspecification.header("authorization", token);
        requestspecification.contentType(ContentType.JSON);
        deleteresponse = requestspecification.request(Method.DELETE,"/api/ecom/product/delete-product/"+ProductID);
        response.then().log().all();

    }


    @And("Validate that the product is deleted")
    public void validateThatTheProductIsDeleted() {
        // Send a request to fetch the product details by its ID
        requestspecification = RestAssured.given();
        requestspecification.header("authorization", token);
        response = requestspecification.request(Method.GET,"/api/ecom/product/get-product-details/"+ProductID);

        // Get the response status code
        int statusCode = response.getStatusCode();

        // Check if the product is not found (HTTP 404 status code)
        if (statusCode == 404) {
            System.out.println("Product is successfully deleted.");
        } else {
            // Alternatively, you can check if the response body contains an error message indicating that the product is not found
            String responseBody = deleteresponse.getBody().asString();
            // Check if the response body contains an error message indicating that the product is not found
            Assert.assertTrue("Product is still found after deletion", responseBody.contains("Product not found"));
        }
    }


}
