package RSProcessStepDefinitions;

import PojoClasses.AddPlace;
import PojoClasses.Location;
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

import java.util.Arrays;

import static io.restassured.RestAssured.baseURI;

public class RSAMapsStepDefinitions {
    public static RequestSpecification requestspecification;
    public static Response response;
    public static String PlaceId;
    public static String expectedAddress="1234street,plano,Texas";
    public static String expectedupdatedAddress="102 street,Plano,Texas";
    public static String actualScope;
    public static String expectedScope="APP";

    @Given("Launch the Maps base URL")
    public void launch_the_maps_base_url() {
        String debugmode = ReadConfig.getdebugmode();

        if (debugmode.equals("on")) {
            baseURI = ReadConfig.getRSAMaps_url() ;
        } else {
            baseURI = System.getProperty("url");
        }    }

    @When("Create a new Place with {string} {string} {string} {string} {string} {string} {string} {string} {string}")
    public void createANewPlaceWith(String lat, String lng, String accuracy, String name, String phone_number, String address, String types, String website, String language) {
        AddPlace addPlace =new AddPlace();
        Location location=new Location();
        location.setLat(Double.parseDouble(lat));
        location.setLng(Double.parseDouble(lng));
        location.setLat(Double.parseDouble(lat));
        location.setLng(Double.parseDouble(lng));
        addPlace.setLocation(location);
        addPlace.setName(name);
        addPlace.setPhone_number(phone_number);
        addPlace.setAddress(address);
        addPlace.setTypes(Arrays.asList(types.split(", ")));        addPlace.setWebsite(website);
        addPlace.setLanguage(language);
        requestspecification = RestAssured.given().log().all();
        requestspecification.queryParam("key", "qaclick123");
        requestspecification.header("Content-Type", "application/json");
        requestspecification.body(addPlace);
        response=requestspecification.request(Method.POST,"/maps/api/place/add/json");
        PlaceId = response.jsonPath().getString("place_id");
        actualScope =response.jsonPath().getString("scope");

    }


    @Then("Validate the Maps status code {int}")
    public void validate_the_maps_status_code(Integer int1) {
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
    }

    @And("Validate the scope")
    public void validateThescope() {
        Assert.assertEquals(expectedScope,actualScope);

    }
    @When("Fetch the Location details")
    public void fetch_the_location_details() {
        requestspecification = RestAssured.given().log().all();
        requestspecification.queryParam("key", "qaclick123");
        requestspecification.queryParam("place_id", PlaceId);
       response=requestspecification.request(Method.GET,"/maps/api/place/get/json");
    }

    @Then("Validate the retrieved address matches the Created address")
    public void validate_the_retrieved_address_matches_the_created_address() {
        String actualAddress = response.jsonPath().getString("address");
        Assert.assertEquals(expectedAddress,actualAddress);
    }
    @When("Update the address details")
    public void update_the_address_details() {
        requestspecification = RestAssured.given().log().all();
        requestspecification.queryParam("key", "qaclick123");
        requestspecification.header("Content-Type", "application/json");
        requestspecification.body("{\n" +
                "    \"place_id\":\"" + PlaceId + "\",\n" +
                "    \"address\":\"" + expectedupdatedAddress + "\",\n" +
                "    \"key\":\"qaclick123\"\n" +
                "}");
        response = requestspecification.request(Method.PUT, "/maps/api/place/update/json");
    }

    @Then("Validate the address details are updated")
    public void validate_the_address_details_are_updated() {
        fetch_the_location_details(); // Fetch the updated details to validate
        String actualUpdatedAddress = response.jsonPath().getString("address");
        Assert.assertEquals(expectedupdatedAddress,actualUpdatedAddress);
    }
    @When("delete the address details")
    public void delete_the_address_details() {
        requestspecification = RestAssured.given().log().all();
        requestspecification.queryParam("key", "qaclick123");
        requestspecification.header("Content-Type", "application/json");
        requestspecification.body("{\n" +
                "    \"place_id\":\"" + PlaceId + "\"\n" +
                "}");
        response = requestspecification.request(Method.DELETE, "/maps/api/place/delete/json");
    }


    @And("Validate the address details are deleted")
    public void validateTheAddressDetailsAreDeleted() {
        fetch_the_location_details();
        String statusMessage = response.jsonPath().getString("msg");
        Assert.assertEquals("Get operation failed, looks like place_id  doesn't exists", statusMessage);
    }



}
