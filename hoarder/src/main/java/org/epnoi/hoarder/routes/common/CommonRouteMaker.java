package org.epnoi.hoarder.routes.common;

import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.SpringCamelContext;
import org.epnoi.hoarder.routes.CommonRoute;
import org.epnoi.hoarder.routes.RouteMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by cbadenes on 27/11/15.
 */
@Component
public class CommonRouteMaker implements RouteMaker {

    private static final Logger LOG = LoggerFactory.getLogger(CommonRouteMaker.class);

    @Autowired
    SpringCamelContext camelContext;

    @Autowired
    List<CommonRoute> commonRoutes;

    @PostConstruct
    public void init() throws Exception {
        LOG.info("registering common routes ..");

        for (CommonRoute commonRoute: commonRoutes){
            LOG.info("Registering common-route: " + commonRoute);
            camelContext.addRouteDefinition(commonRoute.definition());
        }

        LOG.info("Common Routes added");
    }

    @Override
    public boolean accept(String protocol) {
        return false;
    }

    @Override
    public RouteDefinition build(String url) {
        throw new RuntimeException("This maker does not create new routes");
    }
}