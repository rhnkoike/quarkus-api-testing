package com.example.sampleapp.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.path.xml.XmlPath;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    // @Test
    public void testTestEndpoint() {
        given().log().all()
          .when().get("/hello/greeting/test")
          .then()
             .statusCode(200)
             .body(is("hello test")).time(lessThan(1000L)).log().all();
    }

    @Test
    public void testJson() {
      given()
        .when().get("/hello/json")
        .then()
          .log().all()
          .assertThat()
          .body(containsString("Yamada"));

    }

    @Test
    public void testJson2() {
      given()
        .when().get("/hello/json")
        .then()
          .log().body()
          .assertThat()
          .body("age",equalTo(20));

    }

    @Test
    public void testJson3() {
      given()
        .when().get("/hello/json")
        .then()
          .log().body()
          .assertThat()
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
          .statusCode(404)
          .log().all();
    }




    // @Test
    public void testExternalJson() {
      
      int sum = 0;
      Object obj =
      given()
        .queryParam("CUSTOMER_ID","68195")
        .queryParam("PASSWORD","1234!")
        .queryParam("Account_No","1")
        .when().get("http://demo.guru99.com/V4/sinkministatement.php")
        // .timeIn(TimeUnit.MILLISECONDS);
        .then()
           .log().all()
          //  .assertThat()
          //  .statusCode(200)
          .contentType("application/json")
           .extract().path("result.statements.AMOUNT");

      System.out.println(((XmlPath)obj).get());
           

    }

}