package com.acme.routes;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.dataformat.csv.CsvDataFormat;
import io.operon.camel.model.CamelOperonHeaders;

@ApplicationScoped
public class AcmeRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // CsvDataFormat config
        CsvDataFormat csv = new CsvDataFormat();
        csv.setSkipHeaderRecord(true);
        csv.setDelimiter(';');
        csv.setUseOrderedMaps(true);

        // JacksonDataFormat config
        JacksonDataFormat json = new JacksonDataFormat();

        // Exception handler for route errors
        onException(Exception.class) // Catch all the Exception -type exceptions
            .log("Error occured: ${exception}") // Log error
            .handled(true) // The error is not passed on to other error handlers
            .stop(); // Stop routing processing for this error

        from("direct:csvToJson")
            .routeId("csvToJson")
            .setBody(body()
                // replaces html hex code &#x27; for single quote and html hex code &#x22; for double quote, with single quote '
                .regexReplaceAll("&#x27;", "'")
                .regexReplaceAll("&#x22;", "'"))
            .unmarshal(csv)
            .marshal(json)
            .convertBodyTo(String.class)
            .to("mock:csvToJson.result")
            ;
    }
}