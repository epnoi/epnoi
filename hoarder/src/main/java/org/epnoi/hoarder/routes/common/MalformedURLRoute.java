package org.epnoi.hoarder.routes.common;

import org.apache.camel.model.RouteDefinition;
import org.epnoi.hoarder.routes.CommonRoute;
import org.epnoi.hoarder.routes.processors.ErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;

/**
 * Created by cbadenes on 27/11/15.
 */
@Component
public class MalformedURLRoute implements CommonRoute {

    @Autowired
    ErrorHandler errorHandler;

    @Override
    public RouteDefinition definition() {
        RouteDefinition route = new RouteDefinition();
        route.onException(MalformedURLException.class).process(errorHandler).stop();
        return route;
    }
}
