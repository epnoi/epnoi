package org.epnoi.hoarder.routes.rss;

import org.apache.camel.model.RouteDefinition;
import org.apache.commons.lang.StringUtils;
import org.epnoi.hoarder.routes.Expressions;
import org.epnoi.hoarder.routes.RouteMaker;
import org.epnoi.hoarder.routes.common.CommonRouteBuilder;
import org.epnoi.model.Record;
import org.epnoi.storage.model.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 27/11/15.
 */
@Component
public class RssRouteMaker implements RouteMaker {

    private static final Logger LOG = LoggerFactory.getLogger(RssRouteMaker.class);

    @Override
    public boolean accept(String protocol) {
        return protocol.equalsIgnoreCase("rss");
    }

    @Override
    public RouteDefinition build(Source source) {

        String separator = (source.getUrl().contains("?"))? "&" : "?";

        String url = StringUtils.replace(source.getUrl(),"rss://","http://");

        String uri = new StringBuilder("rss:").
                append(url).
                append(separator).
                append("splitEntries=true&consumer.initialDelay=1000&consumer.delay=2000&feedHeader=false&filter=true").
                toString();


        return new RouteDefinition().
                from(uri).
                marshal().rss().
                setProperty(Record.SOURCE_NAME, Expressions.constant(source.getName())).
                setProperty(Record.SOURCE_URL, Expressions.constant(url)).
                to(RssRouteBuilder.URI_RETRIEVE_METAINFORMATION).
                to(CommonRouteBuilder.URI_HTTP_DOWNLOAD_TO_FILE);
    }
}
