package org.epnoi.hoarder.routes.rss;

import lombok.Getter;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.epnoi.hoarder.routes.SourceProperty;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 30/11/15.
 */
@Component
public class RssRouteBuilder extends RouteBuilder{

    public static final String URI_RETRIEVE_METAINFORMATION = "direct:rss.metainformation.retrieve";


    @Getter
    private Namespaces ns = new Namespaces("rss", "http://purl.org/rss/1.0/")
            .add("provenance", "http://www.openarchives.org/OAI/2.0/provenance");

    @Override
    public void configure() throws Exception {

        /*********************************************************************************************************************************
         * -> Set Common Rss Xpath Expressions
         *********************************************************************************************************************************/
        from(URI_RETRIEVE_METAINFORMATION).
                setProperty(SourceProperty.SOURCE_PROTOCOL,            constant("rss")).
                setProperty(SourceProperty.SOURCE_URI,                 simple("http://www.epnoi.org/rss/${property."+SourceProperty.SOURCE_NAME+"}")).
                setProperty(SourceProperty.PUBLICATION_TITLE,          xpath("//rss:item/rss:title/text()", String.class).namespaces(ns)).
                setProperty(SourceProperty.PUBLICATION_DESCRIPTION,    xpath("//rss:item/rss:description/text()", String.class).namespaces(ns)).
                setProperty(SourceProperty.PUBLICATION_PUBLISHED,      xpath("//rss:item/dc:date/text()", String.class).namespaces(ns)).
                setProperty(SourceProperty.PUBLICATION_URI,            xpath("//rss:item/rss:link/text()", String.class).namespaces(ns)).
                setProperty(SourceProperty.PUBLICATION_URL,            xpath("//rss:item/rss:link/text()", String.class).namespaces(ns)).
                setProperty(SourceProperty.PUBLICATION_LANGUAGE,       xpath("//rss:channel/dc:language/text()", String.class).namespaces(ns)).
                setProperty(SourceProperty.PUBLICATION_RIGHTS,         xpath("//rss:channel/dc:rights/text()", String.class).namespaces(ns)).
                setProperty(SourceProperty.PUBLICATION_CREATORS,       xpath("string-join(//rss:channel/dc:creator/text(),\";\")", String.class).namespaces(ns)).
                setProperty(SourceProperty.PUBLICATION_FORMAT,         constant("htm")).
                setProperty(SourceProperty.PUBLICATION_METADATA_FORMAT,constant("xml"));



    }
}
