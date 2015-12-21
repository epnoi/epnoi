package org.epnoi.harvester.routes.pdf;

import org.apache.camel.builder.RouteBuilder;
import org.epnoi.model.Record;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 01/12/15.
 */
@Component
public class PdfRouteBuilder extends RouteBuilder {

    public static final String URI_RETRIEVE_METAINFORMATION = "direct:pdf.metainformation.retrieve";

    @Override
    public void configure() throws Exception {

        //TODO this is only a sample.
        from(URI_RETRIEVE_METAINFORMATION).
                setProperty(Record.SOURCE_PROTOCOL,            constant("pdf")).
                setProperty(Record.SOURCE_URI,                 simple("http://www.epnoi.org/pdf/${property." + Record.SOURCE_NAME + "}")).
                setProperty(Record.SOURCE_NAME,                constant("acm-siggraph-2006-2014")).
                setProperty(Record.SOURCE_URL,                 simple("${header.CamelFileParent}")).
                setProperty(Record.PUBLICATION_TITLE,          simple("${header.CamelFileNameOnly}")).
                setProperty(Record.PUBLICATION_DESCRIPTION,    simple("${header.CamelFileAbsolutePath}")).
                setProperty(Record.PUBLICATION_PUBLISHED,      simple("${header.CamelFileParent}")).
                setProperty(Record.PUBLICATION_URI,            simple("${header.CamelFileAbsolutePath}")).
                setProperty(Record.PUBLICATION_URL,            simple("${header.CamelFileAbsolutePath}")).
                setProperty(Record.PUBLICATION_URL_LOCAL,      simple("${header.CamelFileAbsolutePath}")).
                setProperty(Record.PUBLICATION_LANGUAGE,       constant("en")).
                setProperty(Record.PUBLICATION_RIGHTS,         constant("ACM Publishing License Agreement")).
                setProperty(Record.PUBLICATION_CREATORS,       constant("Shaopeng, Wu")).
                setProperty(Record.PUBLICATION_FORMAT,         constant("pdf")).
                setProperty(Record.PUBLICATION_METADATA_FORMAT,constant("xml")).
                setProperty(Record.PUBLICATION_REFERENCE_URL,  simple("${header.CamelFileAbsolutePath}"));



    }
}