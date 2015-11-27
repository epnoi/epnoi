package org.epnoi.hoarder.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.epnoi.hoarder.data.Repository;
import org.epnoi.hoarder.data.RepositoryId;
import org.epnoi.model.Source;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 21/10/15.
 */
@Component
public class RestRouteBuilder extends RouteBuilder {

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

        rest("/repositories").description("hoarder rest service for repository management")
                //.consumes("application/json").produces("application/json")

                .post().type(Repository.class).outType(RepositoryId.class).description("Create a new repository")
                .to("bean:sourceService?method=newRepository")

                .get("/").description("List all repositories").outTypeList(Repository.class)
                .to("bean:sourceService?method=listRepositories")

                .get("/{id}").description("Find a repository by id").outType(Repository.class)
                //.param().name("id").type(path).description("The id of the user to get").dataType("int").endParam()
                .to("bean:sourceService?method=getRepository(${header.id})");



//        rest("/stemmers/{id}/analysis").description("Stemming analysis rest service")
//
//                .post().type(Document.class).outType(Analysis.class).description("Make a stemming process using the stemmer")
//                //.param().name("body").type(body).description("The document to be stemmed").endParam()
//                .to("bean:analysisService?method=stem");
    }
}
