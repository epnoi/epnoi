package org.epnoi.harvester.routes;

import org.apache.camel.model.RouteDefinition;
import org.epnoi.storage.model.Source;
import org.epnoi.storage.model.Domain;

/**
 * Created by cbadenes on 01/12/15.
 */
public interface RouteMaker {

    boolean accept(String protocol);

    RouteDefinition build(Source source);
}
