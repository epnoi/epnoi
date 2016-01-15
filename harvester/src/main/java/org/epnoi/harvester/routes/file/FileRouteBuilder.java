package org.epnoi.harvester.routes.file;

import lombok.Getter;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.epnoi.model.Record;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 01/12/15.
 */
@Component
public class FileRouteBuilder extends RouteBuilder {

    public static final String URI_RETRIEVE_METAINFORMATION = "direct:file.metainformation.retrieve";

    @Getter
    protected Namespaces ns = new Namespaces("dc", "http://purl.org/dc/elements/1.1/");

    @Override
    public void configure() throws Exception {
        // TODO Beta version. It should read an XML file with the same name than pdf
        from(URI_RETRIEVE_METAINFORMATION).
                setProperty(Record.SOURCE_PROTOCOL, constant("file")).
                setProperty(Record.SOURCE_NAME,                xpath("substring-before(substring-after(//oai:request/text(),\"http://\"),\"/\")", String.class).namespaces(ns)).
                setProperty(Record.SOURCE_URL,                 xpath("//oai:request/text()", String.class).namespaces(ns)).
                setProperty(Record.PUBLICATION_TITLE,          xpath("//oai:metadata/oai:dc/dc:title/text()", String.class).namespaces(ns)).
                setProperty(Record.PUBLICATION_DESCRIPTION,    xpath("//oai:metadata/oai:dc/dc:description/text()",String.class).namespaces(ns)).
                setProperty(Record.PUBLICATION_PUBLISHED,      xpath("//oai:header/oai:datestamp/text()",String.class).namespaces(ns)).
                setProperty(Record.PUBLICATION_URI,            xpath("//oai:header/oai:identifier/text()",String.class).namespaces(ns)).
                setProperty(Record.PUBLICATION_URL,            xpath("//oai:metadata/oai:dc/dc:identifier/text()",String.class).namespaces(ns)).
                setProperty(Record.PUBLICATION_URL_LOCAL,      simple("${header.CamelFileAbsolutePath}")).
                setProperty(Record.PUBLICATION_SUBJECT,        xpath("string-join(//oai:metadata/oai:dc/dc:subject/text(),\";\")", String.class).namespaces(ns)).
                setProperty(Record.PUBLICATION_AUTHORED,       xpath("//oai:metadata/oai:dc/dc:date/text()",String.class).namespaces(ns)).
                setProperty(Record.PUBLICATION_LANGUAGE,       xpath("string-join(//oai:metadata/oai:dc/dc:language/text(),\";\")", String.class).namespaces(ns)).
                setProperty(Record.PUBLICATION_RIGHTS,         xpath("//oai:metadata/oai:dc/dc:rights/text()", String.class).namespaces(ns)).
                setProperty(Record.PUBLICATION_CREATORS,       xpath("string-join(//oai:metadata/oai:dc/dc:creator/text(),\";\")", String.class).namespaces(ns)).
                setProperty(Record.PUBLICATION_CONTRIBUTORS,   xpath("string-join(//oai:metadata/oai:dc/dc:contributor/text(),\";\")", String.class).namespaces(ns)).
                setProperty(Record.PUBLICATION_FORMAT,         xpath("substring-after(//oai:metadata/oai:dc/dc:format[1]/text(),\"/\")", String.class).namespaces(ns)).
                setProperty(Record.PUBLICATION_TYPE,           xpath("string-join(//oai:metadata/oai:dc/dc:type/text(),\";\")", String.class).namespaces(ns)).
                setProperty(Record.PUBLICATION_METADATA_FORMAT, constant("xml")).
                setProperty(Record.PUBLICATION_REFERENCE_URL, simple("${header.CamelFileParent}/.camel/${header.CamelFileNameOnly}"));


    }
}
