package org.epnoi.harvester.eventbus;

import org.epnoi.harvester.services.SourceService;
import org.epnoi.model.Event;
import org.epnoi.model.Resource;
import org.epnoi.storage.model.Source;
import org.epnoi.model.modules.RoutingKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 01/12/15.
 */
@Component
public class SourceCreatedEventHandler extends AbstractEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SourceCreatedEventHandler.class);

    @Autowired
    SourceService sourceService;

    public SourceCreatedEventHandler() {
        super(RoutingKey.of(Resource.Type.SOURCE, Resource.State.CREATED));
    }

    @Override
    public void handle(Event event) {
        LOG.info("New Source event received: " + event);
        try{
            sourceService.create(event.to(Source.class));
        } catch (Exception e){
            // TODO Notify to event-bus when source has not been added
            LOG.error("Error adding new source: " + event, e);
        }
    }
}
