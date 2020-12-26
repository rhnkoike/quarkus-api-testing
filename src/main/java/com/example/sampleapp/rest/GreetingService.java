package com.example.sampleapp.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/")
@ApplicationScoped
@RegisterRestClient(baseUri = "http://localhost:8080/")
public interface GreetingService {

    @GET
    @Path("/helloext")
    @Produces(MediaType.TEXT_PLAIN)
    String hello();
}