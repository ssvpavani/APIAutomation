package ChainingProcess;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import java.util.ArrayList;
import java.util.List;

public class RestfulBooker {
    public static String token;
    public static int booking_id;

    public static void main(String args[])
    {
        baseURI = "https://restful-booker.herokuapp.com";
        //authorization

        Response response = given()
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

         //create booking
        Response response1 = given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"firstname\" : \"Smith\",\n" +
                        "    \"lastname\" : \"Steve\",\n" +
                        "    \"totalprice\" : 500,\n" +
                        "    \"depositpaid\" : true,\n" +
                        "    \"bookingdates\" : {\n" +
                        "        \"checkin\" : \"2024-06-01\",\n" +
                        "        \"checkout\" : \"2024-08-01\"\n" +
                        "    },\n" +
                        "    \"additionalneeds\" : \"Dinner\"\n" +
                        "}")
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .log().all()
                .extract().response();
        booking_id=response1.jsonPath().getInt("bookingid");
        System.out.println("booking_id: " + booking_id);


        //get

        Response getresponse =given()
                .contentType("application/json")
                .when()
                .get("/booking/"+booking_id)
                .then()
                .statusCode(200)
                .log().all()
                .extract().response();
        //to get all booking ids
        Response getresponse1 =given()
                .contentType("application/json")
                .when()
                .get("/booking")
                .then()
                .statusCode(200)
                .log().all()
                .extract().response();
        List<String> BookingIds=new ArrayList<String>();
        for(int i=0;i<booking_id;i++)
        {
            BookingIds.add(getresponse1.jsonPath().getString("["+i+"].bookingid"));
        }

        //put

        given()

                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token="+token)
                .body("{\n" +
                        "    \"firstname\" : \"James\",\n" +
                        "    \"lastname\" : \"Brown\",\n" +
                        "    \"totalprice\" : 111,\n" +
                        "    \"depositpaid\" : true,\n" +
                        "    \"bookingdates\" : {\n" +
                        "        \"checkin\" : \"2018-01-01\",\n" +
                        "        \"checkout\" : \"2019-01-01\"\n" +
                        "    },\n" +
                        "    \"additionalneeds\" : \"Breakfast\"\n" +
                        "}")
                .when()
                .put("/booking/"+booking_id)

                .then()
                .statusCode(200)
                .log().all();
        //patch
        given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token="+token)
                .body("{\n" +
                        "    \"firstname\" : \"James\",\n" +
                        "    \"lastname\" : \"Brown\"\n" +
                        "}")
                .when()
                .patch("/booking/"+booking_id)

                .then()
                .statusCode(200)
                .log().all();
        //delete

        given()
                .when()
                .header("Content-Type", "application/json")
                .header("Cookie", "token="+token)
                .delete("/booking/"+booking_id)

                .then()
                .statusCode(201)
                .log().all();
        Assertions.assertFalse(BookingIds.contains(booking_id));
    }

}
