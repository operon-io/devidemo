package com.devikone.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class RestConfigurationRoute extends RouteBuilder {

    @ConfigProperty(name = "REST_API_HOST", defaultValue="localhost")
    String REST_API_HOST;

    //
    // NOTE: platform-http uses still port 8080
    //
    @ConfigProperty(name = "REST_API_PORT", defaultValue="8081")
    String REST_API_PORT;

    @Override
    public void configure() throws Exception {
        restConfiguration()
            // NOTE: yaml's rest configuration assumes implicitly platform-http,
            //       when there are more than one (http) get -route.
            //       netty-http works when there is one get -route.
            //.component("netty-http")
            .component("platform-http")
            .host(REST_API_HOST)
            .port(REST_API_PORT)
            ;
    }
}