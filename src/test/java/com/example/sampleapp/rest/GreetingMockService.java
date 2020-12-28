package com.example.sampleapp.rest;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.test.Mock;

// @Mock
// @RestClient
// @ApplicationScoped
// public class GreetingMockService implements GreetingService
public class GreetingMockService 
{

    // @Override
    public String hello() {
        System.out.println("called mock service");
        return "Hi ";
    }
    
}
