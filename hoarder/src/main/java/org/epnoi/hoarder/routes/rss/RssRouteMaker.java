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
public class RssRouteMaker implements RouteMaker {

    private static final Logger LOG = LoggerFactory.getLogger(RssRouteMaker.class);

    @Autowired
    SpringCamelContext camelContext;

    @Override
    public boolean accept(String protocol) {
        return protocol.equalsIgnoreCase("rss");
    }

    @Override
    public RouteDefinition build(String url) {

        throw new RuntimeException("Method does not implemented yet!");
    }
}
