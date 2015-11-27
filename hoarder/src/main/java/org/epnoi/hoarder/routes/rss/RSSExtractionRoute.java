package org.epnoi.hoarder.routes.rss;

import org.apache.camel.model.RouteDefinition;
import org.epnoi.hoarder.routes.Expressions;
import org.epnoi.hoarder.routes.SourceProperty;

/**
 * Created by cbadenes on 27/11/15.
 */
public class RSSExtractionRoute extends RSSAbstractRoute {

    public static final String ROUTE = "direct:setCommonRssXpathExpressions";

    @Override
    public RouteDefinition definition() {
        return new RouteDefinition().
                from(ROUTE).
                setProperty(SourceProperty.SOURCE_PROTOCOL, Expressions.constant("rss")).
                setProperty(SourceProperty.SOURCE_URI,                 Expressions.simple("http://www.epnoi.org/rss/${property." + SourceProperty.SOURCE_NAME + "}")).
                setProperty(SourceProperty.PUBLICATION_TITLE,          Expressions.xpath("//rss:item/rss:title/text()", namespaces)).
                setProperty(SourceProperty.PUBLICATION_DESCRIPTION,    Expressions.xpath("//rss:item/rss:description/text()",namespaces)).
                setProperty(SourceProperty.PUBLICATION_PUBLISHED,      Expressions.xpath("//rss:item/dc:date/text()",namespaces)).
                setProperty(SourceProperty.PUBLICATION_URI,            Expressions.xpath("//rss:item/rss:link/text()",namespaces)).
                setProperty(SourceProperty.PUBLICATION_URL,            Expressions.xpath("//rss:item/rss:link/text()",namespaces)).
                setProperty(SourceProperty.PUBLICATION_LANGUAGE,       Expressions.xpath("//rss:channel/dc:language/text()",namespaces)).
                setProperty(SourceProperty.PUBLICATION_RIGHTS,         Expressions.xpath("//rss:channel/dc:rights/text()",namespaces)).
                setProperty(SourceProperty.PUBLICATION_CREATORS,       Expressions.xpath("string-join(//rss:channel/dc:creator/text(),\";\")",namespaces)).
                setProperty(SourceProperty.PUBLICATION_FORMAT,         Expressions.constant("htm")).
                setProperty(SourceProperty.PUBLICATION_METADATA_FORMAT,Expressions.constant("xml"));
    }
}
