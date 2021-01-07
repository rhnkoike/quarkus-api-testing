package com.example.sampleapp.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.endsWith;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(TestStubService.class)
public class GreetingResourceTest {

    @Test
    public void testGreetingEndpoint() {
     
        given()
          .when().get("/greeting/test")
          .then()
            .log().all()
             .statusCode(200)
            //  .body(is("Hello test"));
            .body(endsWith(" test"));
    }

}
