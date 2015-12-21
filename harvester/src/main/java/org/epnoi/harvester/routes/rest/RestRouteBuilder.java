package org.epnoi.harvester.routes.rest;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.epnoi.model.Source;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 01/12/15.
 */
@Component
public class RestRouteBuilder extends RouteBuilder {

    @Value("${epnoi.harvester.rest.port}")
    protected Integer port;

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json_xml)
                .dataFormatProperty("prettyPrint", "true")
                .dataFormatProperty("json.in.disableFeatures", "FAIL_ON_UNKNOWN_PROPERTIES,ADJUST_DATES_TO_CONTEXT_TIME_ZONE")
                .dataFormatProperty("xml.out.mustBeJAXBElement", "false")
                .contextPath("harvester/rest")
                .port(port);

        rest("/sources").description("harvester rest service for sources management")
                //.consumes("application/json").produces("application/json")

                .post().description("Add a new source").type(Source.class).outType(Source.class)
                .to("bean:sourceService?method=create")

                .get("/").description("List all existing sources").outTypeList(Source.class)
                .to("bean:sourceService?method=list")

                .get("/{id}").description("Find a source by uri").outType(Source.class)
                //.param().name("id").type(path).description("The id of the user to get").dataType("int").endParam()
                .to("bean:sourceService?method=get(${header.id})")

                .delete("/{id}").description("Remove an existing source").outType(Source.class)
                .to("bean:sourceService?method=remove")

                .put("/{id}").description("Remove an existing source").type(Source.class).outType(Source.class)
                .to("bean:sourceService?method=update")

        ;

    }
}