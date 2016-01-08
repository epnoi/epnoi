package org.epnoi.harvester.routes.rss;

import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.language.ConstantExpression;
import org.epnoi.harvester.routes.RouteMaker;
import org.epnoi.harvester.routes.common.CommonRouteBuilder;
import org.epnoi.model.Record;
import org.epnoi.model.Source;
import org.epnoi.storage.model.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

/**
 * Created by cbadenes on 04/01/16.
 */
@Component
public class RssRouteMaker implements RouteMaker {

    private static final Logger LOG = LoggerFactory.getLogger(RssRouteMaker.class);

    @Value("${epnoi.hoarder.storage.path}")
    protected String basedir;

    @Override
    public boolean accept(String protocol) {
        return protocol.equalsIgnoreCase("rss");
    }

    @Override
    public RouteDefinition build(Source source, Domain domain) {

        String uri = new StringBuilder().
                append("file:").
                append(Paths.get(basedir).toFile().getAbsolutePath()).
                append("/rss/").
                append(source.name()).
                append("?recursive=true&include=.*.xml&doneFileName=${file:name}.done").
                toString();

        LOG.debug("URI created for harvesting purposes: " + uri);

        return new RouteDefinition().
                from(uri).
                setProperty(Record.SOURCE_URI,  new ConstantExpression(source.getUri())).
                setProperty(Record.DOMAIN_URI,  new ConstantExpression(domain.getUri())).
                to(RssRouteBuilder.URI_RETRIEVE_METAINFORMATION).
                to(CommonRouteBuilder.URI_RO_BUILD);
    }
}
