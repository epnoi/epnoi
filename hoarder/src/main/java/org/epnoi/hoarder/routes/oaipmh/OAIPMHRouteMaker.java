package org.epnoi.hoarder.routes.oaipmh;

import org.apache.camel.model.RouteDefinition;
import org.apache.commons.lang3.StringUtils;
import org.epnoi.hoarder.routes.RouteMaker;
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
    public RouteDefinition build(String url) {

        String separator = (url.contains("?"))? "&" : "?";

        String uri = new StringBuilder(url).append(separator).append("initialDelay=1000&delay=60000").toString();

        String sourceName   = StringUtils.substringBetween(url,"//","/"); //upm
        String sourceUrl    = StringUtils.substringBefore(url, "?"); //"http://oa.upm.es/perl/oai2"

        // TODO Create a valid xPath according to the domain: OAIPMHPubByIdRoute, OAIPMHPubByRelRoute, OAIPMHPubByViewRoute
        return new OAIPMHRoute(uri,sourceName,sourceUrl,routeBuilder.getNs()).definition();
    }
}
