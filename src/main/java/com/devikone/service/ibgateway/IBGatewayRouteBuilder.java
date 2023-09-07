package com.devikone.service.ibgateway;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;

import io.operon.camel.model.CamelOperonHeaders;

import com.devikone.service.ibgateway.exception.*;

@ApplicationScoped
public class IBGatewayRouteBuilder extends RouteBuilder {

    @Inject
    IBGatewayController ibgw;
    
    @Override
    public void configure() throws Exception {

        onException(UnauthorizedException.class)
            .setHeader(Exchange.HTTP_RESPONSE_CODE).constant("401") // Unauthorized -status code
            ;

        onException(DataSaveException.class)
            .handled(true)
            .setHeader(Exchange.HTTP_RESPONSE_CODE).constant("400") // Bad-request
            .setBody().simple("${exception.message}")
            ;

        onException(DataLoadException.class)
            .handled(true)
            .setHeader(Exchange.HTTP_RESPONSE_CODE).constant("400") // Bad-request
            .setBody().simple("${exception.message}")
            ;

        from("direct:service.ibgw.control.handle_get").autoStartup("{{service.ibgw.enabled}}")
            .bean(ibgw, "authenticate(${headers.x-api-key})")
            .bean(ibgw, "popControlMessages(${headers.x-api-key})")
            .choice()
                .when(simple("${body} == null"))
                    .setHeader(Exchange.HTTP_RESPONSE_CODE).constant("204")
                    .endChoice()
                .otherwise()
                    .setHeader(Exchange.HTTP_RESPONSE_CODE).constant("200")
                    .setHeader(Exchange.CONTENT_TYPE).constant("application/json")
                    .bean(ibgw, "controlMessagePollResponseToJson(*)")
            ;
        
        from("direct:service.ibgw.control.handle_post_ready").autoStartup("{{service.ibgw.enabled}}")
            .bean(ibgw, "authenticate(${headers.x-api-key})")
            .bean(ibgw, "ackReadyControlMessage(${headers.ibgwMessageId}, 360000L)") // expire value for ack-ready is set to 100 hours
            .setHeader(Exchange.HTTP_RESPONSE_CODE).constant("200")
            ;

        //
        // TODO: Require Admin api-key (now uses app's api-key)
        //
        from("direct:service.ibgw.admin.set_appname_by_apikey").autoStartup("{{service.ibgw.enabled}}")
            .bean(ibgw, "authenticate(${headers.x-api-key})")
            .bean(ibgw, "setAppNameByApiKey(${headers.appApikey}, ${headers.appname})")
            .log("set the apikey and app-name")
            ;
        
        //
        // TODO: Require Admin api-key (now uses app's api-key)
        //
        from("direct:service.ibgw.admin.get_app_status").autoStartup("{{service.ibgw.enabled}}")
            .bean(ibgw, "authenticate(${headers.x-api-key})")
            .bean(ibgw, "getAppStatusByApiKey(${headers.appApikey})")
            .log("Get app-status")
            ;
        
        from("direct:service.ibgw.control.handle_post_message").autoStartup("{{service.ibgw.enabled}}")
            .bean(ibgw, "authenticate(${headers.x-api-key})")
            .log("Creating and saving a control-message :: ${body}")
            .bean(ibgw, "mapControlMessage(${headers.x-api-key}, *)")
            .bean(ibgw, "saveControlMessage(${headers.x-api-key}, ${body})")
            .bean(ibgw, "controlMessageToJson(*)")
            ;

        //
        // This route is for testing the functionality
        //
        from("direct:service.ibgw.control.handle_post_test_message").autoStartup("{{service.ibgw.enabled}}")
            .bean(ibgw, "authenticate(${headers.x-api-key})")
            .log("Creating and saving a test control-message")
            .bean(ibgw, "newTestControlMessage()")
            .bean(ibgw, "saveControlMessage(${headers.x-api-key}, ${body})")
            .log("  - Saved")
            .bean(ibgw, "controlMessageToJson(*)")
            .setHeader(Exchange.HTTP_RESPONSE_CODE).constant("200")
            .setHeader(Exchange.CONTENT_TYPE).constant("application/json")
            ;

        from("direct:service.ibgw.data.handle_post_message").autoStartup("{{service.ibgw.enabled}}")
            .bean(ibgw, "authenticate(${headers.x-api-key})")
            //.log("Saving data-entry")
            .bean(ibgw, "saveData(${headers.x-api-key}, *)")
            .setHeader(Exchange.HTTP_RESPONSE_CODE).constant("200")
            .setHeader(Exchange.CONTENT_TYPE).constant("application/json")
            .bean(ibgw, "dataMetaInfoToJson(*)")
            ;

        from("direct:service.ibgw.data.handle_get_message").autoStartup("{{service.ibgw.enabled}}")
            .bean(ibgw, "authenticate(${headers.x-api-key})")
            //.log("Loading data-entry")
            .bean(ibgw, "loadData(${headers.x-api-key}, *)")
            .choice()
                .when(simple("${header.IBGW_DATA} == null"))
                    .setHeader(Exchange.HTTP_RESPONSE_CODE).constant("204")
                    .endChoice()
                .otherwise()
                    .setHeader(Exchange.HTTP_RESPONSE_CODE).constant("200")
                    .setHeader(Exchange.CONTENT_TYPE).constant("application/octet-stream")
                    .setBody().header("IBGW_DATA")
                    .removeHeader("IBGW_DATA")
            ;
    }
}