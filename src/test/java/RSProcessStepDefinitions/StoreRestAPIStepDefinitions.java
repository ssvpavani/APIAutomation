package RSProcessStepDefinitions;

import PojoClasses.Products;
import Utilities.ReadConfig;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class StoreRestAPIStepDefinitions {
    public static RequestSpecification requestspecification;
    public static Response response;
    public static String expectedtitle="HP Laptop";
    public static String actualtitle;
    String id;
    int actualprice;
    int expectedprice=200;
    String ExpUpdatedTitle="Dell Laptop";
    String actualUpdatedTitle;
    int putstatusCode;
    int actual_deleteStatusCode;
    @Given("Launch the StoreRestAPI base URL")
    public void launch_the_store_rest_api_base_url() {
        String debugmode = ReadConfig.getdebugmode();
        if (debugmode.equals("on")) {
            baseURI = ReadConfig.getStoreRestAPI_url();
        } else {
            baseURI = System.getProperty("url");
        }
    }

    @When("Create a new product with {string} {string} {string} {string}")
    public void create_a_new_product_with(String title, String price, String description, String category) {
        Products products=new Products();
        products.setTitle(title);
        products.setPrice(Integer.parseInt(price));
        products.setDescription(description);
        products.setCategory(category);
        requestspecification = RestAssured.given().log().all();
        requestspecification.header("Content-Type", "application/json; charset=UTF-8");
        requestspecification.body(products);
        response=requestspecification.request(Method.POST,"/products");
        response.then().log().all();
        actualtitle=response.jsonPath().getString("data.title");
        id=  response.jsonPath().getString("_id");

    }

    @Then("Validate the StoreRestAPI status code {int}")
    public void validate_the_store_rest_api_status_code(Integer int1) {
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 201);
    }

    @And("Validate the product is created with the given details")
    public void validateTheProductIsCreatedWithTheGivenDetails() {
        Assertions.assertEquals(expectedtitle,actualtitle);
    }

    @When("Fetch all the Products")
    public void fetch_all_the_products() {
        requestspecification = RestAssured.given().log().all();
        response=requestspecification.request(Method.GET,"/products");
        response.then().log().all();

    }
    @Then("Validate the getall StoreRestAPI status code {int}")
    public void validateTheGetallStoreRestAPIStatusCode(int arg0) {
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
    }

    @When("Fetch the Single Product")
    public void fetch_the_single_product() {
        requestspecification = RestAssured.given().log().all();
        response=requestspecification.request(Method.GET,"/products/running-sneaker");
        response.then().log().all();
        actualprice=response.jsonPath().getInt("data.price");

    }
    @And("Validate the retrieved product matches the Created product")
    public void validateTheRetrievedProductMatchesTheCreatedProduct() {
        Assert.assertEquals(expectedprice, actualprice);

    }



    @When("Update the Product details")
    public void update_the_product_details(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        Map<String, String> products = data.get(0);

        // Construct the JSON request body with proper formatting
        String updateBody = "{\n" +
                "    \"title\": \"" + products.get("title") + "\",\n" +
                "    \"price\": " + products.get("price") + ",\n" + // Assuming price is a number, no quotes needed
                "    \"description\": \"" + products.get("description") + "\",\n" +
                "    \"category\": \"" + products.get("category") + "\"\n" +
                "}";


        requestspecification = RestAssured.given().log().all();
        requestspecification.header("Content-Type", "application/json; charset=UTF-8");
        requestspecification.body(updateBody);
        response=requestspecification.request(Method.PUT,"/products/running-sneaker");
        response.then().log().all();
        actualUpdatedTitle=response.jsonPath().getString("data.title");
        putstatusCode = response.getStatusCode();


    }
    @Then("Validate the put StoreRestAPI status code {int}")
    public void validateThePutStoreRestAPIStatusCode(int expectedputstatuscode) {
        Assert.assertEquals(expectedputstatuscode,putstatusCode);

    }

    @And("Validate the Product details are updated")
    public void validate_the_product_details_are_updated() {
        Assertions.assertEquals(ExpUpdatedTitle,actualUpdatedTitle);
    }

    @When("delete the product details")
    public void delete_the_product_details() {
        requestspecification = RestAssured.given().log().all();
        response=requestspecification.request(Method.DELETE,"/products/running-sneaker");
        response.then().log().all();
        actual_deleteStatusCode=response.getStatusCode();
    }
    @Then("Validate the delete StoreRestAPI status code {int}")
    public void validateTheDeleteStoreRestAPIStatusCode(int expectedDeletestatuscode) {
        Assert.assertEquals(expectedDeletestatuscode,actual_deleteStatusCode);


    }

    @And("Validate the product details are deleted")
    public void validate_the_product_details_are_deleted() {
        String responseBody = response.getBody().asString();
        Assertions.assertTrue(responseBody.contains("Success! Product deleted"));
    }



}
