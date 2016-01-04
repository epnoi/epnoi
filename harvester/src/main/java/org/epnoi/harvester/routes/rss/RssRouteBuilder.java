package org.epnoi.harvester.routes.rss;

import lombok.Getter;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.epnoi.model.Record;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 01/12/15.
 */
@Component
public class RssRouteBuilder extends RouteBuilder {

    public static final String URI_RETRIEVE_METAINFORMATION = "direct:rss.metainformation.retrieve";

    @Getter
    protected Namespaces ns = new Namespaces("provenance", "http://www.openarchives.org/OAI/2.0/provenance")
            .add("rss", "http://purl.org/rss/1.0/");

    @Override
    public void configure() throws Exception {

        from(URI_RETRIEVE_METAINFORMATION).
                setProperty(Record.SOURCE_PROTOCOL, constant("rss")).
                setProperty(Record.SOURCE_NAME, xpath("//rss:channel/rss:title/text()", String.class).namespaces(ns)).
                setProperty(Record.SOURCE_URL, xpath("//rss:channel/rss:link/text()", String.class).namespaces(ns)).
                setProperty(Record.PUBLICATION_TITLE, xpath("//rss:item/rss:title/text()", String.class).namespaces(ns)).
                setProperty(Record.PUBLICATION_DESCRIPTION, xpath("//rss:item/rss:description/text()", String.class).namespaces(ns)).
                setProperty(Record.PUBLICATION_PUBLISHED, xpath("//rss:item/dc:date/text()", String.class).namespaces(ns)).
                setProperty(Record.PUBLICATION_URI, xpath("//rss:item/rss:link/text()", String.class).namespaces(ns)).
                setProperty(Record.PUBLICATION_URL, xpath("//rss:item/rss:link/text()", String.class).namespaces(ns)).
                setProperty(Record.PUBLICATION_URL_LOCAL, simple("${header.CamelFileAbsolutePath}")).
                setProperty(Record.PUBLICATION_LANGUAGE, xpath("//rss:channel/dc:language/text()", String.class).namespaces(ns)).
                setProperty(Record.PUBLICATION_RIGHTS, xpath("//rss:channel/dc:rights/text()", String.class).namespaces(ns)).
                setProperty(Record.PUBLICATION_CREATORS, xpath("string-join(//rss:channel/dc:creator/text(),\";\")", String.class).namespaces(ns)).
                setProperty(Record.PUBLICATION_FORMAT, constant("htm")).
                setProperty(Record.PUBLICATION_METADATA_FORMAT, constant("xml")).
                setProperty(Record.PUBLICATION_REFERENCE_URL, simple("${header.CamelFileParent}/.camel/${header.CamelFileNameOnly}"));


    }
}
