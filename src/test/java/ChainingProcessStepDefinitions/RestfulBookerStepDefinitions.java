package ChainingProcessStepDefinitions;

import PojoClasses.BookingDates;
import PojoClasses.BookingDetails;
import Utilities.ReadConfig;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class RestfulBookerStepDefinitions {
    public static String token;
    public static Response response;
    public static int booking_id;
    public static String expectedLastName="Crooks";
    public static String expUpdatedFirstName="Carmel";
    public static String EPUFName="Todd";
    public static String EPULName="Jones";
    public static List<String> BookingIds;

    @Given("Launch the RestfulBooker base URL")
    public void launch_the_RestfulBooker_base_url() {
        String debugmode = ReadConfig.getdebugmode();
        if (debugmode.equals("on")) {
            baseURI = ReadConfig.getRestfulBooker_url();
        } else {
            baseURI = System.getProperty("url");
        }
    }
    @When("Create a token")
    public void create_a_token() {
         response = given()
                .header("Content-Type", "application/json")
                .body("{ \"username\" : \"admin\", \"password\" : \"password123\" }")
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .log().all()
                .extract().response();
        token=response.jsonPath().getString("token");
        System.out.println("Token: " + token);
    }

    @When("Create a new booking with {string} {string} {string} {string} {string} {string} {string}")
    public void create_a_new_booking(String firstname, String lastname, String totalprice, String depositpaid, String checkin, String checkout, String additionalneeds) {
        BookingDetails bookingdetails = new BookingDetails();
        bookingdetails.setFirstname(firstname);
        bookingdetails.setLastname(lastname);
        bookingdetails.setTotalprice(Double.parseDouble(totalprice));
        bookingdetails.setDepositpaid(Boolean.parseBoolean(depositpaid));
        bookingdetails.setAdditionalneeds(additionalneeds);
        BookingDates bookingdates = new BookingDates();
        bookingdates.setCheckin(checkin);
        bookingdates.setCheckout(checkout);
        bookingdetails.setBookingdates(bookingdates);
        response = given()
                .header("Content-Type", "application/json")
                .body(bookingdetails)
                .when()
                .log().all()
                .post("/booking")
                .then()
                .log().all()
                .extract().response();
        booking_id = response.jsonPath().getInt("bookingid");
        //System.out.println("booking_id: " + booking_id);
    }

    @Then("Validate the status code {int}")
    public void validate_the_status_code(Integer expectedStatusCode) {
        int ActualStatusCode=response.statusCode();
        Assertions.assertEquals(expectedStatusCode,ActualStatusCode);
    }



    @Then("Validate the booking is created with the given details")
    public void validate_the_booking_is_created_with_the_given_details() {

       String actualLastName= response.jsonPath().getString("booking.lastname");
       Assertions.assertEquals(expectedLastName,actualLastName);
    }




    @When("Fetch the user details")
    public void fetch_the_user_details() {
        response =given()
                .contentType("application/json")
                .when()
                .get("/booking/"+booking_id)
                .then()
                .statusCode(200)
                .log().all()
                .extract().response();
    }
    @And("Validate the retrieved booking matches the Created booking")
    public void Validate_the_retrieved_booking_matches_the_Created_booking()
    {
        String actualLastName= response.jsonPath().getString("lastname");
        Assertions.assertEquals(expectedLastName,actualLastName);

    }

    @When("Fetch the all the Ids")
    public void fetch_the_all_the_ids() {
        response =given()
                .contentType("application/json")
                .when()
                .get("/booking")
                .then()
                .statusCode(200)
                .log().all()
                .extract().response();
        BookingIds=new ArrayList<>();
        List<Integer> ids = response.jsonPath().getList("bookingid");
        for (Integer id : ids) {
            BookingIds.add(id.toString());
        }
    }

    @When("Update the booking details")
    public void update_the_booking_details(DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        Map<String, String> bookingDetails = data.get(0);

        // Construct the JSON request body with proper formatting
        String updateBody = "{\n" +
                "    \"firstname\" : \"" + bookingDetails.get("firstname") + "\",\n" +
                "    \"lastname\" : \"" + bookingDetails.get("lastname") + "\",\n" +
                "    \"totalprice\" : " + bookingDetails.get("totalprice") + ",\n" +
                "    \"depositpaid\" : " + bookingDetails.get("depositpaid") + ",\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"" + bookingDetails.get("checkin") + "\",\n" +
                "        \"checkout\" : \"" + bookingDetails.get("checkout") + "\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"" + bookingDetails.get("additionalneeds") + "\"\n" +
                "}";

        // Send the request to update the booking details
        response = given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .body(updateBody)
                .when()
                .put("/booking/" + booking_id)
                .then()
                .extract().response();
    }

    @Then("Validate the booking details are updated")
    public void validate_the_booking_details_are_updated() {

       String actualUpdatedFName= response.jsonPath().getString("firstname");
       Assertions.assertEquals(expUpdatedFirstName, actualUpdatedFName);
    }

    @When("partially update the booking details")
    public void partially_update_the_booking_details() {
        response= given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token="+token)
                .body("{\n" +
                        "    \"firstname\" : \"Todd\",\n" +
                        "    \"lastname\" : \"Jones\"\n" +
                        "}")
                .when()
                .patch("/booking/"+booking_id)

                .then()
                .statusCode(200)
                .log().all()
                .extract().response();
    }

    @Then("Validate the booking details are updated partially")
    public void validate_the_booking_details_are_updated_partially() {
     String APUFName= response.jsonPath().getString("firstname");
     String APULName= response.jsonPath().getString("lastname");
     Assertions.assertEquals(EPUFName,APUFName);
     Assertions.assertEquals(EPULName,APULName);


    }



    @When("delete the booking details of the created user")
    public void delete_the_booking_details_of_the_created_user() {
        response= given()
                .when()
                .header("Content-Type", "application/json")
                .header("Cookie", "token="+token)
                .delete("/booking/"+booking_id)

                .then()
                .statusCode(201)
                .log().all()
               .extract().response();
    }
    @Then("Validate the delete status code {int}")
    public void validate_the_delete_status_code(Integer expectedDeleteStatusCode) {
        int actualDeleteStatusCode=response.statusCode();
        Assertions.assertEquals(expectedDeleteStatusCode,actualDeleteStatusCode);
    }
    @Then("Validate the booking details are deleted")
    public void validate_the_booking_details_are_deleted() {
        fetch_the_all_the_ids();
        Assertions.assertFalse(BookingIds.contains(booking_id));

    }



}
