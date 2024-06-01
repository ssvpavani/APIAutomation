package RSProcessStepDefinitions;
import static io.restassured.RestAssured.*;

import PojoClasses.Items;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AzurebookCartStepDefinitions {
    public static RequestSpecification requestspecification;
    public static Response response;
    public int initialQuantity;
    public int updatedQuantity;
    List<Map<String, Object>> items;
    List<Map<String, Object>> expitems = new ArrayList<>();
    @Given("Launch the AzureBookCart base url")
    public void launch_the_azure_book_cart_base_url() {
        String debugmode = ReadConfig.getdebugmode();
        if (debugmode.equals("on")) {
            baseURI = ReadConfig.getAzureBookCart_url() ;
        } else {
            baseURI = System.getProperty("url");
        }
    }
    @When("Add the item to the cart with {string} {string} {string} {string} {string} {string} {string}")
    public void addTheItemToTheCartWith(String userId, String firstName, String lastName, String username, String password, String gender, String userTypeId) {
        Items items = new Items();
        items.setUserId(Integer.parseInt(userId));
        items.setFirstName(firstName);
        items.setLastName(lastName);
        items.setUsername(username);
        items.setPassword(password);
        items.setGender(gender);
        items.setUserTypeId(Integer.parseInt(userTypeId));
        requestspecification = RestAssured.given();
        requestspecification.header("Content-Type", "application/json");
        requestspecification.body(items);
        response = requestspecification.request(Method.POST, "/ShoppingCart/AddToCart/15/2");
        response.then().log().all();
    }
    @Then("Validate the statuscode {int}")
    public void validate_the_statuscode(Integer int1) {
        int statuscode=response.getStatusCode();
        Assert.assertEquals(statuscode,200);
    }

    @When("Fetch the items in the cart")
    public void fetch_the_items_in_the_cart() {
        requestspecification= RestAssured.given();
        requestspecification.header("Content-Type","application/json");
        response= requestspecification.request(Method.GET,"/ShoppingCart/15");
        response.then().log().all();
        initialQuantity=response.jsonPath().getInt("[0].quantity");
        System.out.println("Initial Quantity: " + initialQuantity);

    }

    @When("update the cart by reducing an item")
    public void update_the_cart_by_reducing_an_item() {
        requestspecification= RestAssured.given();
        requestspecification.header("accept", "text/plain");
        requestspecification.request(Method.PUT,"ShoppingCart/15/2");
        response.then().log().all();
        updatedQuantity=response.jsonPath().getInt("[0].quantity");

    }
    @And("Validate the quantity is reduced")
    public void validateTheQuantityIsReduced() {

        Assert.assertTrue("Item quantity was not reduced!", updatedQuantity > initialQuantity);

    }


    @Given("Delete all items in the cart")
    public void delete_all_items_in_the_cart() {
        requestspecification = RestAssured.given();
        requestspecification.request(Method.DELETE, "ShoppingCart/15/2");
        response = requestspecification.request(Method.GET, "/ShoppingCart/15");
        response.then().log().all();
        items = response.jsonPath().getList("$");
        expitems.clear();
        System.out.println(expitems);
        System.out.println(items);
    }


    @And("Validate the cartitems  are deleted")
    public void validateTheCartitemsAreDeleted() {
        Assert.assertEquals(expitems, items);

    }
}
