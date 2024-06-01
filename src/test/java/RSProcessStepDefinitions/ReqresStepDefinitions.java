package RSProcessStepDefinitions;

import static io.restassured.RestAssured.*;
import PojoClasses.ReqresPojo;
import Utilities.ReadConfig;
import io.cucumber.core.internal.com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.Assert;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReqresStepDefinitions {
    public static RequestSpecification requestspecification;
    public static Response response;
    public static int statusCode;
    String id;
    String expName="Sasi";
    String actualname;
    String expUpdatedjob="Backend Developer";
    String actualupdatedjob;
    String actualPartialJob;
    String expPartialJob="Python Backend Developer";

    @Given("Launch the Reqres base URL")
    public void launch_the_reqres_base_url() throws IOException {
        String debugmode = ReadConfig.getdebugmode();
        if (debugmode.equals("on")) {
            baseURI = ReadConfig.getReqres_url();
        } else {
            baseURI = System.getProperty("url");
        }
        System.out.println("Base URI: " + baseURI);
        System.out.println("System property url: " + System.getProperty("url"));
    }


    @When("Create a new user with {string} {string}")
    public void create_a_new_user_with(String name,String job) throws JsonProcessingException {
        requestspecification = RestAssured.given().log().all();
        ReqresPojo reqres = new ReqresPojo();
        reqres.setName(name);
        reqres.setJob(job);
        requestspecification.header("Content-Type", "application/json");
        requestspecification.body(reqres);
        response = requestspecification.request(Method.POST, "/users");
        response.then().log().all();
        id = response.jsonPath().getString("id");
        actualname=response.jsonPath().getString("name");
    }

    @Then("Validate the reqrespost status code {int}")
    public void validate_the_reqrespost_status_code(Integer int1) {
        int statusCode = response.getStatusCode();
        Assert.assertEquals(int1.intValue(), statusCode); // Updated to compare against int1
    }
    @And("Validate the response name")
    public void validateTheResponseName() {
        Assert.assertEquals(expName,actualname);
    }

    @When("Fetch the user")
    public void fetch_the_user() {
        requestspecification = RestAssured.given();
        response = requestspecification.request(Method.GET, "/users/2");
        statusCode = response.getStatusCode();
    }

    @Then("Validate the reqres status code {int}")
    public void validate_the_requres_status_code(Integer expStatusCode) {
        statusCode = response.getStatusCode();
        Assert.assertEquals(expStatusCode.intValue(), statusCode);
    }

    @When("Update the user details")
    public void update_the_user_details() {
        RequestSpecification https = RestAssured.given();
        JSONObject inputbody = new JSONObject();
        inputbody.put("name", "Pavani");
        inputbody.put("job", "Backend Developer");
        https.header("Content-Type", "application/json");
        https.body(inputbody.toString()); // Updated to toString() from toJSONString()
        response = https.request(Method.PUT, "/users/"+id); // Store response in instance variable
        actualupdatedjob=response.jsonPath().getString("job");
    }
    @And("Validate the Updated job")
    public void validateTheUpdatedJob() {
        Assert.assertEquals(expUpdatedjob,actualupdatedjob);
    }
    @When("partially update the user details")
    public void partiallyUpdateTheUserDetails() {
        RequestSpecification https = RestAssured.given();
        JSONObject obj = new JSONObject();
        obj.put("name", "Pavani");
        obj.put("job", "Python Backend Developer");
        https.header("Content-Type", "application/json");
        https.body(obj.toString()); // Updated to toString() from toJSONString()
        response = https.request(Method.PATCH, "/users/"+id); // Store response in instance variable
        actualPartialJob=response.jsonPath().getString("job");

    }

    @When("delete the created user")
    public void deleteTheCreatedUser() {
        RequestSpecification https = RestAssured.given();
        response = https.request(Method.DELETE, "/users/"+id); // Store response in instance variable
    }

    @Then("Validate the reqresdelete status code {int}")
    public void validateTheReqresdeleteStatusCode(int int1) {
        int statusCode = response.getStatusCode();
        Assert.assertEquals(int1, statusCode); // Updated to compare against int1
    }


    @And("Validate the PartialUpdated job")
    public void validateThePartialUpdatedJob() {
        Assert.assertEquals(expPartialJob,actualPartialJob);

    }
}
