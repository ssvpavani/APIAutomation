package ChainingProcess;
import java.io.File;

import static io.restassured.RestAssured.*;

public class AzureBookCart {
    public static void main(String[] args) {

        // Base URL for the Azure Bookcart
         baseURI = "https://bookcart.azurewebsites.net/api";
       File path=new File("C:\\Users\\ssathi\\IdeaProjects\\RestAssured_CRUD\\src\\main\\java\\data.json");

        //post
        given()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .when()
                .post("/ShoppingCart/AddToCart/15/2")
                .then()
                .statusCode(200)
                .log().all(); // Logging response details


        given()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .when()
                .get("/ShoppingCart/15")
                .then()
                .statusCode(200)
                .log().all(); // Logging response details

        // PUT request example to update an existing Gist
        given()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .when()
                .put("ShoppingCart/15/2")
                .then()
                .statusCode(200)
                .log().all(); // Logging response details

        // DELETE request example to delete an existing Gist
        given()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .when()
                .delete("ShoppingCart/15/2")
                .then()
                .statusCode(200)
                .log().all(); // Logging response details


        //to check whether the deleted id is available or not
    }
}
