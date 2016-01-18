package org.epnoi.hoarder.services;

import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.SpringCamelContext;
import org.epnoi.hoarder.routes.RouteDefinitionFactory;
import org.epnoi.storage.model.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public Source create(Source source) throws Exception {

        LOG.info("adding source: " + source);

        // TODO if exist in database return

        // Create a new route for this source
        RouteDefinition route = routeDefinitionFactory.newRoute(source);

        // TODO Save route in ddbb

        // Add route to camel-context
        LOG.info("adding route to hoarding: " + route);
        camelContext.addRouteDefinition(route);

        // TODO Notify the creation of a new hoarder route (to handle cluster recovery)
        //eventBus.post(Event.from());

        return source;
    }

    public Source update(String uri,Source source){
        throw new RuntimeException("Method does not implemented yet");
    }


    public Source remove(String uri){
        throw new RuntimeException("Method does not implemented yet");
    }

    public List<Source> list(){
        throw new RuntimeException("Method does not implemented yet");
    }


    public Source get(String id){
        throw new RuntimeException("Method does not implemented yet");
    }


}
