package com.example.sampleapp.external;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class GreetingExtResource {

 
    @GET
    @Path("/helloext")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        System.out.println("called external service.");
        return "Hello ";
    }
}