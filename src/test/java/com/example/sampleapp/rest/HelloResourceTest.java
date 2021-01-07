package com.example.sampleapp.rest;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.*;


@QuarkusTest
public class HelloResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body(is("hello"));
    }

    @Test
    public void testJson() {
      given()
        .when().get("/hello/json")
        .then()
          // .log().all()
          .body(containsString("Yamada"));
    }

    @Test
    public void testJson2() {
      given()
        .when().get("/hello/json")
        .then()
          // .log().body()
          .body("age",equalTo(20));

    }

    @Test
    public void testJson3() {
      given()
        .when().get("/hello/json")
        .then()
          // .log().body()
          .body("birthdate",not(empty()))
          .body("name", equalToIgnoringCase("yamada"))
          .body("gender",nullValue())
          .body("age",lessThan(30));

    }

    @Test
    public void testJson404() {
      given()
        .when().get("/hello/404")
        .then()
          .statusCode(404);
          // .log().all();
    }
}