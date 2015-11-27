package org.epnoi.hoarder.routes.oaipmh;

import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.commons.lang3.StringUtils;
import org.epnoi.hoarder.routes.RouteMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by cbadenes on 27/11/15.
 */
@Component
public class OAIPMHRouteMaker implements RouteMaker {

    private static final Logger LOG = LoggerFactory.getLogger(OAIPMHRouteMaker.class);

    @Autowired
    SpringCamelContext camelContext;

    @PostConstruct
    public void init() throws Exception {
        LOG.info("registering route with common expressions to retrieve meta-information from OAI-PMH repositories..");

        // Common OAI-PMH Retrieve Expressions
        camelContext.addRouteDefinition(new OAIPMHExtractionRoute().definition());

        // Avoid Deleted OAI-PMH resources
        camelContext.addRouteDefinition(new OAIPMHAvoidDeletedRoute().definition());

        LOG.info("OAI-PMH Common Routes added");
    }

    @Override
    public boolean accept(String protocol) {
        return protocol.equalsIgnoreCase("amqp");
    }

    @Override
    public RouteDefinition build(String url) {

        String separator = (url.contains("?"))? "&" : "?";

        String uri = new StringBuilder(url).append(separator).append("initialDelay=1000&delay=60000").toString();

        String sourceName   = StringUtils.substringBetween(url,"//","/"); //upm
        String sourceUrl    = StringUtils.substringBefore(url, "?"); //"http://oa.upm.es/perl/oai2"

        // TODO Create a valid xPath according to the domain: OAIPMHPubByIdRoute, OAIPMHPubByRelRoute, OAIPMHPubByViewRoute
        return new OAIPMHSourceRoute(uri,sourceName,sourceUrl).definition();
    }
}
