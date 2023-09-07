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
@TestProfile(IntegrationAltTest.class)
@QuarkusTest
public class IntegrationAltTest extends CamelQuarkusTestSupport {

    @Inject
    public CamelContext camelContext;

    private final String inputData = "" +
        "id;date;arrival;departure;extra_info;firstname;lastname;address;city;zipCode;mobilePhone\n" +
        "\"45\";\"2022-06-26\";\"0852\";\"0645\";\"0\";\"Christelle\";\"Bernier\";\"854 Kiehn Mill 65\";\"New Annabellmouth\";\"37457-1349\";\"+358939919057\"\n" + 
        "\"37\";\"2022-06-25\";\"0656\";\"0949\";\"0\";\"Garnett\";\"Stracke\";\"64983 Kristian Estates 11\";\"Lake Omariborough\";\"59062-9310\";\"+358064167239\"\n"
        ;

    private final String expectedOutputJson = "[{\"person_id\": \"45\", \"date\": \"2022-06-26\", \"arrival_ts\": \"0852\", \"departure_ts\": \"0645\", \"extra_info\": \"0\", \"code\": \"Check!\"}, {\"person_id\": \"37\", \"date\": \"2022-06-25\", \"arrival_ts\": \"0656\", \"departure_ts\": \"0949\", \"extra_info\": \"0\", \"code\": \"Check!\"}]";

    //
    // This tests the alternative mapping method with Operon-script
    //
    @Test
    public void integrationAltTest() throws Exception {
        ProducerTemplate pt = camelContext.createProducerTemplate();
        Object response = pt.requestBody("direct:controller.operon", inputData);
        MockEndpoint mock = getMockEndpoint("mock:controller.result");
        mock.expectedMinimumMessageCount(1);
        List<Exchange> exchanges = mock.getExchanges();

        for (Exchange ex : exchanges) {
            String messageBody = ex.getIn().getBody(String.class);
            assertEquals(expectedOutputJson, messageBody);
        }
        mock.assertIsSatisfied();
    }

}