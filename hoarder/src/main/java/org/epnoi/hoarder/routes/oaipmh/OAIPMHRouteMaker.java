package org.epnoi.hoarder.routes.oaipmh;

import org.apache.camel.model.RouteDefinition;
import org.apache.commons.lang3.StringUtils;
import org.epnoi.hoarder.routes.RouteMaker;
import org.epnoi.model.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 27/11/15.
 */
@Component
public class OAIPMHRouteMaker implements RouteMaker {

    private static final Logger LOG = LoggerFactory.getLogger(OAIPMHRouteMaker.class);

    @Autowired
    OAIPMHRouteBuilder routeBuilder;

    @Override
    public boolean accept(String protocol) {
        return protocol.equalsIgnoreCase("oaipmh");
    }

    @Override
    public RouteDefinition build(Source source) {

        String separator = (source.getUrl().contains("?"))? "&" : "?";

        String uri = new StringBuilder(source.getUrl()).append(separator).append("initialDelay=1000&delay=60000").toString();

        String sourceName   = source.name();
        String sourceUrl    = source.server(); //"oa.upm.es/perl/oai2"

        return new OAIPMHRoute(uri,sourceName,sourceUrl,routeBuilder.getNs()).definition();
    }
}
