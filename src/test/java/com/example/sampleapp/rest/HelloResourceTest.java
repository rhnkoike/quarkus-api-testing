package com.example.sampleapp.rest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

// @QuarkusTest
public class HelloResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body(is("hello")).log().all();
    }

    // @Test
    public void testTestEndpoint() {
        given()
          .when().get("http://localhost:8080/test")
          .then()
             .statusCode(200)
             .body(is("helloTest"));
    }

}