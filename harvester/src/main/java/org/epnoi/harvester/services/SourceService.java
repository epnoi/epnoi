package org.epnoi.harvester.services;

import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.SpringCamelContext;
import org.epnoi.harvester.routes.RouteDefinitionFactory;
import org.epnoi.storage.model.Source;
import org.epnoi.storage.UDM;
import org.epnoi.storage.URIGenerator;
import org.epnoi.storage.model.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by cbadenes on 01/12/15.
 */
@Component
public class SourceService {

    private static final Logger LOG = LoggerFactory.getLogger(SourceService.class);

    @Autowired
    SpringCamelContext camelContext;

    @Autowired
    RouteDefinitionFactory routeDefinitionFactory;

    @Autowired
    UDM udm;

    @Autowired
    URIGenerator uriGenerator;

    public SourceService(){

    }

    public Source create(Source source) throws Exception {

        // TODO check if route exists in database, then return

        LOG.info("creating a new domain associated to source: " + source);
        Domain domain = new Domain();
        domain.setUri(uriGenerator.newDomain());
        domain.setName(source.getName());
        domain.setDescription("attached to source: " + source.getUri());
        udm.saveDomain(domain);
        LOG.info("Domain: " + domain + " created attached to source: " + source);


        // Create a new route for harvesting this source
        RouteDefinition route = routeDefinitionFactory.newRoute(source,domain);
        // TODO Save route in ddbb
        // Add route to camel-context
        LOG.info("adding route to harvest: " + route);
        camelContext.addRouteDefinition(route);

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