package org.epnoi.hoarder.services;

import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.SpringCamelContext;
import org.epnoi.hoarder.routes.RouteDefinitionFactory;
import org.epnoi.model.Resource;
import org.epnoi.model.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cbadenes on 21/10/15.
 */
@Component
public class SourceService {

    private static final Logger LOG = LoggerFactory.getLogger(SourceService.class);

    @Autowired
    SpringCamelContext camelContext;

    @Autowired
    RouteDefinitionFactory routeDefinitionFactory;

    public SourceService(){

    }

    public Resource newSource(Source source) throws Exception {
        LOG.info("adding source: " + source);

        // Check if exist in database

        // Create a new route for this url
        RouteDefinition route = routeDefinitionFactory.newRoute(source);

        // Add route to ddbb and notify to event.bus to rest of cluster
        // TODO Handle cluster actions

        // Add route to camel-context
        LOG.info("adding route to hoarding: " + route);
        camelContext.addRouteDefinition(route);

        return source;
    }


    public List<Source> listSources(){
        LOG.debug("listing Sources ..");
        List<Source> sourcesList = new ArrayList<>();

        return sourcesList;
    }


    public Source getSource(String id){
        LOG.debug("getting Source '" + id + "'");
        return null;
    }


}
