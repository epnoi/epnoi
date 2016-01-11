package org.epnoi.hoarder.routes;

import org.apache.camel.model.RouteDefinition;
import org.epnoi.storage.model.Source;

/**
 * Created by cbadenes on 27/11/15.
 */
public interface RouteMaker {

    boolean accept(String protocol);

    RouteDefinition build(Source url);
}
