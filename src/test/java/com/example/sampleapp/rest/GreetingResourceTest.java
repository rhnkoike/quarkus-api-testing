package com.example.sampleapp.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class GreetingResourceTest {

    @Inject
    @RestClient
    GreetingService greetingService;

    // @BeforeAll
    // public static void setup() {
    //     // GreetingService mock = Mockito.mock(GreetingService.class);
    //     // Mockito.when(mock.hello()).thenReturn("Hi ");
    //     // QuarkusMock.installMockForType(new GreetingMockService(), GreetingService.class);
    // }

    @Test
    public void testHelloEndpoint() {
        // Mockito.when(greetingService.hello()).thenReturn("hello from mockito");
        QuarkusMock.installMockForInstance(new GreetingMockService(), greetingService);

        given()
          .when().get("/greeting/test")
          .then()
             .statusCode(200)
            //  .body(is("Hello test"));
            .body(is("Hi test"));
    }

}
