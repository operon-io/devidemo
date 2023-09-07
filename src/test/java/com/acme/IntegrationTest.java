package com.acme;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.ProducerTemplate;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.acme.routes.AcmeRouteBuilder;

//
// NOTE: when running tests, the "test"-profile is activated (see application.yml).
//
// For further information, please find e.g. https://camel.apache.org/camel-quarkus/next/user-guide/testing.html
//
@TestProfile(IntegrationTest.class)
@QuarkusTest
public class IntegrationTest extends CamelQuarkusTestSupport {

    @Inject
    public CamelContext camelContext;

    private final String inputData = "" +
        "id;date;arrival;departure;extra_info;firstname;lastname;address;city;zipCode;mobilePhone\n" +
        "\"45\";\"2022-06-26\";\"0852\";\"0645\";\"0\";\"Christelle\";\"Bernier\";\"854 Kiehn Mill 65\";\"New Annabellmouth\";\"37457-1349\";\"+358939919057\"\n" + 
        "\"37\";\"2022-06-25\";\"0656\";\"0949\";\"0\";\"Garnett\";\"Stracke\";\"64983 Kristian Estates 11\";\"Lake Omariborough\";\"59062-9310\";\"+358064167239\"\n"
        ;

    private final String expectedOutputJson = 
        "[\n" +
        "        {\n" +
        "                \"person_id\": \"45\",\n" +
        "                \"date\": \"2022-06-26\",\n" +
        "                \"arrival_ts\": \"0852\",\n" +
        "                \"departure_ts\": \"0645\",\n" +
        "                \"extra_info\": \"0\",\n" +
        "                \"code\": \"OK\"\n" +
        "        },\n" +
        "        {\n" +
        "                \"person_id\": \"37\",\n" +
        "                \"date\": \"2022-06-25\",\n" +
        "                \"arrival_ts\": \"0656\",\n" +
        "                \"departure_ts\": \"0949\",\n" +
        "                \"extra_info\": \"0\",\n" +
        "                \"code\": \"OK\"\n" +
        "        }\n" +
        "]"
        ;

    @Test
    public void integrationTest() throws Exception {
        ProducerTemplate pt = camelContext.createProducerTemplate();
        Object response = pt.requestBody("direct:controller", inputData);
        MockEndpoint mock = getMockEndpoint("mock:controller.result");
        mock.expectedMinimumMessageCount(1);
        List<Exchange> exchanges = mock.getExchanges();

        for (Exchange ex : exchanges) {
            String messageBody = ex.getIn().getBody(String.class);
            assertTrue(messageBody.contains("\"person_id\": \"45\""));
        }
        mock.assertIsSatisfied();
    }

}