package org.epnoi.hoarder.routes.oaipmh;

import org.apache.camel.model.RouteDefinition;
import org.epnoi.hoarder.routes.Expressions;

/**
 * Created by cbadenes on 27/11/15.
 */
public class OAIPMHAvoidDeletedRoute extends OAIPMHAbstractRoute {

    public static final String URI = "direct:avoidDeletedMessages";

    @Override
    public RouteDefinition definition() {
        RouteDefinition route = new RouteDefinition();
        route.from(URI).
                choice().
                when().xpath("//oai:header[@status=\"deleted\"]", String.class, namespaces).stop().
                end();
        return route;
    }
}
