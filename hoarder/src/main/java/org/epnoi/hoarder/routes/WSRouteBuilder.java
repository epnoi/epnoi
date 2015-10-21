package org.epnoi.hoarder.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.epnoi.model.Source;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 21/10/15.
 */
@Component
public class WSRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json_xml)
                .dataFormatProperty("prettyPrint", "true")
                .dataFormatProperty("json.in.disableFeatures", "FAIL_ON_UNKNOWN_PROPERTIES,ADJUST_DATES_TO_CONTEXT_TIME_ZONE")
                .dataFormatProperty("xml.out.mustBeJAXBElement", "false")
                .contextPath("hoarder/rest")
                .port(8080);

        rest("/sources").description("hoarder rest service")
                //.consumes("application/json").produces("application/json")

                .get("/").description("List all sources").outTypeList(Source.class)
                .to("bean:sourcesService?method=listSources")

                .get("/{id}").description("Find a source by id").outType(Source.class)
                //.param().name("id").type(path).description("The id of the user to get").dataType("int").endParam()
                .to("bean:sourcesService?method=getSource(${header.id})");



//        rest("/stemmers/{id}/analysis").description("Stemming analysis rest service")
//
//                .post().type(Document.class).outType(Analysis.class).description("Make a stemming process using the stemmer")
//                //.param().name("body").type(body).description("The document to be stemmed").endParam()
//                .to("bean:analysisService?method=stem");
    }
}
