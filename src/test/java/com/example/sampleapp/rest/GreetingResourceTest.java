package com.example.sampleapp.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.endsWith;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(WiremockService.class)
public class GreetingResourceTest {

    // @Inject
    // @RestClient
    // GreetingService greetingService;

    // @Inject
    // @ConfigProperty(name = "mock.externalservice.enabled", defaultValue = "false")
    // Boolean mockExternalServiceEnabled;

    // @BeforeAll
    // public static void setup() {
    //     // GreetingService mock = Mockito.mock(GreetingService.class);
    //     // Mockito.when(mock.hello()).thenReturn("Hi ");
    //     // QuarkusMock.installMockForType(new GreetingMockService(), GreetingService.class);
    // }

    // @BeforeEach
    // public void setup(){
        // if (mockExternalServiceEnabled) {
            // Mockito.when(greetingService.hello()).thenReturn("hello from mockito");
            // QuarkusMock.installMockForInstance(new GreetingMockService(), greetingService);
            // System.out.println("mocking external service");
        // }
    // }

    @Test
    public void testGreetingEndpoint() {
     
        given()
          .when().get("/greeting/test")
          .then()
            .log().all()
             .statusCode(200)
            //  .body(is("Hello test"));
            // .body(is("Hi test"));
            .body(endsWith(" test"));
    }

}
