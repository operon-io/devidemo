package com.devikone.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class RestApiRoute extends RouteBuilder {
    
    @Override
    public void configure() throws Exception {

        //
        // IBGW-service
        //
        from("rest:get:/gateway/control").autoStartup("{{service.ibgw.rest.enabled}}").to("direct:service.ibgw.control.handle_get");
        from("rest:get:/gateway/data").autoStartup("{{service.ibgw.rest.enabled}}").to("direct:service.ibgw.data.handle_get_message");
        from("rest:get:/gateway/admin/status/{appApikey}").autoStartup("{{service.ibgw.rest.enabled}}").to("direct:service.ibgw.admin.get_app_status");
        from("rest:post:/gateway/control/ready/{ibgwMessageId}").autoStartup("{{service.ibgw.rest.enabled}}").to("direct:service.ibgw.control.handle_post_ready");
        from("rest:post:/gateway/control").autoStartup("{{service.ibgw.rest.enabled}}").to("direct:service.ibgw.control.handle_post_message");
        from("rest:post:/gateway/control/test").autoStartup("{{service.ibgw.rest.enabled}}").to("direct:service.ibgw.control.handle_post_test_message");
        from("rest:post:/gateway/data").autoStartup("{{service.ibgw.rest.enabled}}").to("direct:service.ibgw.data.handle_post_message");
        from("rest:post:/gateway/admin/apikey/{appApikey}/app/{appname}").autoStartup("{{service.ibgw.rest.enabled}}").to("direct:service.ibgw.admin.set_appname_by_apikey");
    }
}