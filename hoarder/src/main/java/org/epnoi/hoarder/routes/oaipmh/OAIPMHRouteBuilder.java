package org.epnoi.hoarder.routes.oaipmh;

import lombok.Getter;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.epnoi.hoarder.routes.SourceProperty;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 30/11/15.
 */
@Component
public class OAIPMHRouteBuilder extends RouteBuilder{

    public static final String URI_RETRIEVE_METAINFORMATION = "direct:oaipmh.metainformation.retrieve";

    public static final String URI_AVOID_DELETE             = "direct:oaipmh.filter.non-deleted";

    @Getter
    protected Namespaces ns = new Namespaces("oai", "http://www.openarchives.org/OAI/2.0/")
            .add("dc", "http://purl.org/dc/elements/1.1/")
            .add("provenance", "http://www.openarchives.org/OAI/2.0/provenance")
            .add("oai_dc", "http://www.openarchives.org/OAI/2.0/oai_dc/")
            .add("rss", "http://purl.org/rss/1.0/");

    @Override
    public void configure() throws Exception {

        /*********************************************************************************************************************************
         * -> Set Common OAI-PMH Xpath Expressions
         *********************************************************************************************************************************/
        from(URI_RETRIEVE_METAINFORMATION).
                setProperty(SourceProperty.SOURCE_PROTOCOL,                constant("oaipmh")).
                setProperty(SourceProperty.SOURCE_URI,                     simple("http://www.epnoi.org/oaipmh/${property." + SourceProperty.SOURCE_NAME + "}")).
                setProperty(SourceProperty.PUBLICATION_TITLE,              xpath("//oai:metadata/oai:dc/dc:title/text()",String.class).namespaces(ns)).
                setProperty(SourceProperty.PUBLICATION_DESCRIPTION,        xpath("//oai:metadata/oai:dc/dc:description/text()",String.class).namespaces(ns)).
                setProperty(SourceProperty.PUBLICATION_PUBLISHED,          xpath("//oai:header/oai:datestamp/text()",String.class).namespaces(ns)).
                setProperty(SourceProperty.PUBLICATION_URI,                xpath("//oai:header/oai:identifier/text()",String.class).namespaces(ns)).
                setProperty(SourceProperty.PUBLICATION_URL,                xpath("//oai:metadata/oai:dc/dc:identifier/text()",String.class).namespaces(ns)).
                setProperty(SourceProperty.PUBLICATION_LANGUAGE,           xpath("//oai:metadata/oai:dc/dc:language/text()",String.class).namespaces(ns)).
                setProperty(SourceProperty.PUBLICATION_RIGHTS,             xpath("//oai:metadata/oai:dc/dc:rights/text()",String.class).namespaces(ns)).
                setProperty(SourceProperty.PUBLICATION_CREATORS,           xpath("string-join(//oai:metadata/oai:dc/dc:creator/text(),\";\")",String.class).namespaces(ns)).
                setProperty(SourceProperty.PUBLICATION_FORMAT,             xpath("substring-after(//oai:metadata/oai:dc/dc:format[1]/text(),\"/\")", String.class).namespaces(ns)).
                setProperty(SourceProperty.PUBLICATION_METADATA_FORMAT,    constant("xml")).
                to(URI_AVOID_DELETE);


        /*********************************************************************************************************************************
         * -> Avoid OAI-PMH Deleted Resources
         *********************************************************************************************************************************/
        from(URI_AVOID_DELETE).
                choice().
                when().xpath("//oai:header[@status=\"deleted\"]", String.class, ns).stop().
                end();

    }
}
