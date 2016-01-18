package org.epnoi.hoarder.eventbus;

import org.epnoi.hoarder.services.SourceService;
import org.epnoi.model.Event;
import org.epnoi.model.Resource;
import org.epnoi.storage.model.Source;
import org.epnoi.model.modules.RoutingKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 27/11/15.
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
        LOG.info("Source created event received: " + event);
        try {
            sourceService.create(event.to(Source.class));
        } catch (RuntimeException e){
            LOG.warn(e.getMessage());
        } catch (Exception e){
            // TODO Notify to event-bus source not added
            LOG.error("Error adding new source: " + event, e);
        }
    }
}
