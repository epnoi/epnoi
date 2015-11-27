package org.epnoi.hoarder.routes.rss;

import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.SpringCamelContext;
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
public class RSSSourceRouteMaker implements RouteMaker {

    private static final Logger LOG = LoggerFactory.getLogger(RSSSourceRouteMaker.class);

    @Autowired
    SpringCamelContext camelContext;

    @PostConstruct
    public void init() throws Exception {
        LOG.info("registering route with common expressions to retrieve meta-information from RSS repositories..");

        // Common meta-information expressions from RSS
        camelContext.addRouteDefinition(new RSSExtractionRoute().definition());

        LOG.info("RSS Common Routes added");
    }


    @Override
    public boolean accept(String protocol) {
        return protocol.equalsIgnoreCase("rss");
    }

    @Override
    public RouteDefinition build(String url) {

        return null;
    }
}
