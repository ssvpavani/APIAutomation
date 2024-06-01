package ChainingProcess;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import static org.hamcrest.Matchers.equalTo;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class GoogleMaps_RSA {
    public static String actualScope;
    public static String PlaceId;
    public static String expectedScope = "APP";
    public static String actualstatus;
    public static String expectedstatus = "OK";
    public static String actualAddress;
    public static String expectedAddress = "29, side layout, cohen 09";
    public static String newAddress;
    public static void main(String args[]) {
        baseURI = "https://rahulshettyacademy.com";

        //post - addplace
        Response response = given()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "  \"location\": {\n" +
                        "    \"lat\": -38.383494,\n" +
                        "    \"lng\": 33.427362\n" +
                        "  },\n" +
                        "  \"accuracy\": 50,\n" +
                        "  \"name\": \"Frontline house\",\n" +
                        "  \"phone_number\": \"(+91) 983 893 3937\",\n" +
                        "  \"address\": \"29, side layout, cohen 09\",\n" +
                        "  \"types\": [\n" +
                        "    \"shoe park\",\n" +
                        "    \"shop\"\n" +
                        "  ],\n" +
                        "  \"website\": \"http://google.com\",\n" +
                        "  \"language\": \"French-IN\"\n" +
                        "}")
                .when()
                .post("/maps/api/place/add/json")
                .then()
                .statusCode(200)
                .log().all()
                .extract().response();
        actualScope = response.jsonPath().getString("scope");
        PlaceId = response.jsonPath().getString("place_id");
        actualstatus = response.jsonPath().getString("status");
        Assertions.assertEquals(expectedScope, actualScope);
        Assertions.assertEquals(expectedstatus, actualstatus);
        System.out.println(PlaceId);


        //get Place

        Response getPlacereponse = given()
                .log().all()
                .queryParam("key", "qaclick123")
                .queryParam("place_id", PlaceId)
                .when()
                .get("maps/api/place/get/json")

                .then()
                .assertThat().log().all().statusCode(200).extract().response();
        actualAddress = getPlacereponse.jsonPath().getString("address");
        Assertions.assertEquals(expectedAddress, actualAddress);


        //put - Updating the address

        newAddress = "plano,Texas";
        given()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body("{\r\n"
                        + "    \"place_id\":\"" + PlaceId + "\",\r\n"
                        + "    \"address\":\"" + newAddress + "\",\r\n"
                        + "    \"key\":\"qaclick123\"\r\n"
                        + "}")
                .when()
                .put("maps/api/place/update/json")

                .then()
                .assertThat().log().all().statusCode(200)
                .body("msg", equalTo("Address successfully updated"));


        //delete
        given()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"place_id\":\""+PlaceId+"\"\n" +
                        "}")
                .when()
                .put("maps/api/place/delete/json")

                .then()
                .assertThat().log().all().statusCode(200);
    }
}