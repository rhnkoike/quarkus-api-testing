package com.example.sampleapp.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(TestDatabase.class)
@Tag("integration")
public class FruitsEndpointTest {

    @Test
    public void testListAllFruits() {
        //List all, should have all 3 fruits the database has initially:
        given()
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body(
                        containsString("Cherry"),
                        containsString("Apple"),
                        containsString("Banana"));

        //Delete the Cherry:
        given()
                .when().delete("/fruits/1")
                .then()
                .statusCode(204);

        //List all, cherry should be missing now:
        given()
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body(
                        not(containsString("Cherry")),
                        containsString("Apple"),
                        containsString("Banana"));

        //Create the Pear:
        given()
                .when()
                .body("{\"name\" : \"Pear\"}")
                .contentType("application/json")
                .post("/fruits")
                .then()
                .statusCode(201);

        //List all, cherry should be missing now:
        given()
                .when().get("/fruits")
                .then()
                // .log().body()
                .statusCode(200)
                .body(
                        not(containsString("Cherry")),
                        containsString("Apple"),
                        containsString("Banana"),
                        containsString("Pear"));
    }

    @Test
    public void testUpdateFruits() {
        //Get fruit (id=1):
        Fruit f = given()
                .when().get("/fruits/1")
                .then()
                .statusCode(200)
                .extract().as(Fruit.class);

        //Update the Cherry:
        f.setName("Red Cherry");
        given()
                .when()
                .body(f)
                .contentType("application/json")
                .put("/fruits/1")
                .then()
                .statusCode(200);

        //List all, cherry should be missing now:
        given()
                .when().get("/fruits")
                .then()
                // .log().body()
                .statusCode(200)
                .body(
                        containsString("Red Cherry"),
                        containsString("Apple"),
                        containsString("Banana"));
    }
}
