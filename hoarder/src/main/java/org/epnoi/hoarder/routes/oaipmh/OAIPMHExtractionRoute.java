package org.epnoi.hoarder.routes.oaipmh;

import org.apache.camel.model.RouteDefinition;
import org.epnoi.hoarder.routes.Expressions;
import org.epnoi.hoarder.routes.SourceProperty;

/**
 * Created by cbadenes on 27/11/15.
 */
public class OAIPMHExtractionRoute extends OAIPMHAbstractRoute{

    public static final String ROUTE = "direct:setCommonOaipmhXpathExpressions";

    public RouteDefinition definition(){
        return new RouteDefinition().
                from(ROUTE).
                setProperty(SourceProperty.SOURCE_PROTOCOL, Expressions.constant("oaipmh")).
                setProperty(SourceProperty.SOURCE_URI,                     Expressions.simple("http://www.epnoi.org/oaipmh/${property." + SourceProperty.SOURCE_NAME + "}")).
                setProperty(SourceProperty.PUBLICATION_TITLE,              Expressions.xpath("//oai:metadata/oai:dc/dc:title/text()",namespaces)).
                setProperty(SourceProperty.PUBLICATION_DESCRIPTION,        Expressions.xpath("//oai:metadata/oai:dc/dc:description/text()",namespaces)).
                setProperty(SourceProperty.PUBLICATION_PUBLISHED,          Expressions.xpath("//oai:header/oai:datestamp/text()",namespaces)).
                setProperty(SourceProperty.PUBLICATION_URI,                Expressions.xpath("//oai:header/oai:identifier/text()",namespaces)).
                setProperty(SourceProperty.PUBLICATION_URL,                Expressions.xpath("//oai:metadata/oai:dc/dc:identifier/text()",namespaces)).
                setProperty(SourceProperty.PUBLICATION_LANGUAGE,           Expressions.xpath("//oai:metadata/oai:dc/dc:language/text()",namespaces)).
                setProperty(SourceProperty.PUBLICATION_RIGHTS,             Expressions.xpath("//oai:metadata/oai:dc/dc:rights/text()",namespaces)).
                setProperty(SourceProperty.PUBLICATION_CREATORS,           Expressions.xpath("string-join(//oai:metadata/oai:dc/dc:creator/text(),\";\")",namespaces)).
                setProperty(SourceProperty.PUBLICATION_FORMAT,             Expressions.xpath("substring-after(//oai:metadata/oai:dc/dc:format[1]/text(),\"/\")",namespaces)).
                setProperty(SourceProperty.PUBLICATION_METADATA_FORMAT,    Expressions.constant("xml")).
                to(OAIPMHAvoidDeletedRoute.URI);
    }
}
