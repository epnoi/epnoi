package org.epnoi.hoarder.routes;

import org.apache.camel.model.RouteDefinition;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by cbadenes on 27/11/15.
 */
@Component
public class Router {

    private static final Logger LOG = LoggerFactory.getLogger(Router.class);

    @Autowired
    List<RouteMaker> sourceBuilders;

    public RouteDefinition newSource(String url){

        String protocol = StringUtils.substringBefore(url,":").toLowerCase();

        List<RouteMaker> handlers = sourceBuilders.stream().filter(builder -> builder.accept(protocol)).collect(Collectors.toList());

        if (handlers == null || handlers.isEmpty()){
            throw new RuntimeException("Route Builder not found for protocol: " + protocol);
        }else if (handlers.size() > 1){
            LOG.warn("More than one builder for handling '" + protocol + "' sources: " + handlers);
        }

        return handlers.get(0).build(url);
    }

}
