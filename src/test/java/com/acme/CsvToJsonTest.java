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
@TestProfile(CsvToJsonTest.class)
@QuarkusTest
public class CsvToJsonTest extends CamelQuarkusTestSupport {

    @Inject
    public CamelContext camelContext;

    private final String inputData = "" +
        "id;date;arrival;departure;extra_info;firstname;lastname;address;city;zipCode;mobilePhone\n" +
        "\"45\";\"2022-06-26\";\"0852\";\"0645\";\"0\";\"Christelle\";\"Bernier\";\"854 Kiehn Mill 65\";\"New Annabellmouth\";\"37457-1349\";\"+358939919057\"\n" + 
        "\"37\";\"2022-06-25\";\"0656\";\"0949\";\"0\";\"Garnett\";\"Stracke\";\"64983 Kristian Estates 11\";\"Lake Omariborough\";\"59062-9310\";\"+358064167239\"\n"
        ;

    private final String expectedOutputCsvToJson = "[{\"id\":\"45\",\"date\":\"2022-06-26\",\"arrival\":\"0852\",\"departure\":\"0645\",\"extra_info\":\"0\"," +
        "\"firstname\":\"Christelle\",\"lastname\":\"Bernier\",\"address\":\"854 Kiehn Mill 65\",\"city\":\"New Annabellmouth\"," +
        "\"zipCode\":\"37457-1349\",\"mobilePhone\":\"+358939919057\"},{\"id\":\"37\",\"date\":\"2022-06-25\",\"arrival\":\"0656\"," +
        "\"departure\":\"0949\",\"extra_info\":\"0\",\"firstname\":\"Garnett\",\"lastname\":\"Stracke\",\"address\":\"64983 Kristian Estates 11\"," +
        "\"city\":\"Lake Omariborough\",\"zipCode\":\"59062-9310\",\"mobilePhone\":\"+358064167239\"}]"
        ;

    //
    // This tests single route that transforms CSV to JSON
    //
    @Test
    public void csvToJsonTest() throws Exception {
        //
        // We use ProducerTemplate so we can call any direct-route
        // we want, to gain fine-grained control for the testing.
        //
        ProducerTemplate pt = camelContext.createProducerTemplate();

        //
        // Sending headers to route is optional
        //
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("anykey", "123");

        //
        // requestBodyAndHeaders is synchronic-call which gets the response.
        // 
        Object response = pt.requestBodyAndHeaders("direct:csvToJson", inputData, headers);

        //
        // mock-endpoints are used to set the asserts for the routes.
        //
        MockEndpoint mockCsvToJson = getMockEndpoint("mock:csvToJson.result");
        //
        // Add an assert that at least 1 message was received (does not evaluate assert at this point)
        //
        mockCsvToJson.expectedMinimumMessageCount(1);
        List<Exchange> exchangesCsvToJson = mockCsvToJson.getExchanges();

        //
        // We can inspect the message content by iterating the Exchanges that the mock-endpoint
        // received.
        //
        for (Exchange ex : exchangesCsvToJson) {
            String messageBody = ex.getIn().getBody(String.class);
            assertEquals(expectedOutputCsvToJson, messageBody);
        }
        //
        // Evaluate the added asserts
        //
        mockCsvToJson.assertIsSatisfied();
    }

}