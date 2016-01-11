package org.epnoi.modeler.routes.rest;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.epnoi.model.Source;
import org.epnoi.storage.model.Analysis;
import org.epnoi.storage.model.Domain;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 11/01/16.
 */
@Component
public class RestRouteBuilder extends RouteBuilder {

    @Value("${epnoi.modeler.rest.port}")
    protected Integer port;

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json_xml)
                .dataFormatProperty("prettyPrint", "true")
                .dataFormatProperty("json.in.disableFeatures", "FAIL_ON_UNKNOWN_PROPERTIES,ADJUST_DATES_TO_CONTEXT_TIME_ZONE")
                .dataFormatProperty("xml.out.mustBeJAXBElement", "false")
                .contextPath("modeler/rest")
                .port(port);

        rest("/analyses").description("modeler rest service for analyses management")
                //.consumes("application/json").produces("application/json")

                .post().description("Add a new analysis").type(Domain.class).outType(String.class)
                .to("bean:topicModelingService?method=create")

                .get("/").description("List all existing analyses").outTypeList(Analysis.class)
                .to("bean:topicModelingService?method=list")

                .get("/{uri}").description("Find an analysis by uri").outType(Analysis.class)
                //.param().name("id").type(path).description("The id of the user to get").dataType("int").endParam()
                .to("bean:topicModelingService?method=get(${header.uri})")

                .delete("/{uri}").description("Remove an existing analysis").outType(Analysis.class)
                .to("bean:topicModelingService?method=remove(${header.uri})")

                .put("/{id}").description("Update an existing analysis").type(Analysis.class).outType(Source.class)
                .to("bean:topicModelingService?method=update")

        ;

    }
}