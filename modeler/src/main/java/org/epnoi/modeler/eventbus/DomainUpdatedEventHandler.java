package org.epnoi.modeler.eventbus;

import org.epnoi.model.Event;
import org.epnoi.model.Resource;
import org.epnoi.model.modules.RoutingKey;
import org.epnoi.modeler.services.TopicModelingService;
import org.epnoi.modeler.services.WordEmbeddingService;
import org.epnoi.storage.model.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 11/01/16.
 */
@Component
public class DomainUpdatedEventHandler extends AbstractEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DomainUpdatedEventHandler.class);

    @Autowired
    TopicModelingService topicModelingService;

    @Autowired
    WordEmbeddingService wordEmbeddingService;

    public DomainUpdatedEventHandler() {
        super(RoutingKey.of(Resource.Type.DOMAIN, Resource.State.UPDATED));
    }

    @Override
    public void handle(Event event) {
        LOG.info("Domain updated event received: " + event);
        try{
            Domain domain = event.to(Domain.class);

            topicModelingService.buildModel(domain);

            wordEmbeddingService.buildModel(domain);

        } catch (Exception e){
            // TODO Notify to event-bus when source has not been added
            LOG.error("Error scheduling a new topic model from domain: " + event, e);
        }
    }
}