package ChainingProcess;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Reqres {
    public static void main(String args[])
    {
        int id;
        baseURI = "https://reqres.in/api";

        //get
            given()
                    .when()
                    .get("/users?page=2")
                    .then()
                    .statusCode(200)
                    .body("page",equalTo(2))
                    .log().all();


        //post

            id=given()
                    .contentType("application/json")
                    .body("{\n" +
                            "    \"name\": \"morpheus\",\n" +
                            "    \"job\": \"leader\"\n" +
                            "}")
                    .when()
                    .post("/users")
                    .jsonPath().getInt("id");
        //put

            given()
                    .contentType("application/json")
                    .body("{\n" +
                            "    \"name\": \"morpheus\",\n" +
                            "    \"job\": \"zion resident\"\n" +
                            "}")
                    .when()
                    .put("/users/"+id)

                    .then()
                    .statusCode(200)
                    .log().all();

         //patch
        given()
                .contentType("application/json")
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"zion resident\"\n" +
                        "}")
                .when()
                .put("/users/"+id)

                .then()
                .statusCode(200)
                .log().all();
        //delete

            given()
                    .when()
                    .delete("/users/"+id)

                    .then()
                    .statusCode(204)
                    .log().all();


    }

}
