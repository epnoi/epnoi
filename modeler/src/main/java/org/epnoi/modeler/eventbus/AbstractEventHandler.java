package org.epnoi.modeler.eventbus;

import org.epnoi.model.modules.BindingKey;
import org.epnoi.model.modules.EventBus;
import org.epnoi.model.modules.EventBusSubscriber;
import org.epnoi.model.modules.RoutingKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Created by cbadenes on 11/01/16.
 */
public abstract class AbstractEventHandler implements EventBusSubscriber {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractEventHandler.class);

    protected final RoutingKey routingKey;

    @Autowired
    protected EventBus eventBus;

    public AbstractEventHandler(RoutingKey routingKey){
        this.routingKey = routingKey;
    }

    @PostConstruct
    public void init(){
        LOG.info("Trying to register as subscriber of '" + routingKey + "' events ..");
        eventBus.subscribe(this, BindingKey.of(routingKey, "modeler"));
        LOG.info("registered successfully");
    }

}
