package com.example.sampleapp.rest;

import java.util.Collections;
import java.util.Map;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class TestStubService implements QuarkusTestResourceLifecycleManager {

	private WireMockServer wireMockServer;

	@Override
	public Map<String, String> start() {
		wireMockServer = new WireMockServer();
		wireMockServer.start();

		stubFor(get(urlEqualTo("/helloext"))
				.willReturn(aResponse()
						.withHeader("Content-Type", "text/plain")
						.withBody("Hi ")));

		stubFor(get(urlMatching(".*")).atPriority(10)
				.willReturn(aResponse().proxiedFrom("https://localhost:8080/")));

		return Collections.singletonMap("org.example.sampleapp.rest.GreetingService/mp-rest/url", wireMockServer.baseUrl());
	}

	@Override
	public void stop() {
		if (null != wireMockServer) {
			wireMockServer.stop();
		}
	}
}
