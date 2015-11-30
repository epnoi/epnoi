package org.epnoi.model.modules;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import org.epnoi.model.Event;

public interface EventBusSubscriber {

    @AllowConcurrentEvents
    @Subscribe
    void handle(Event event);

}
