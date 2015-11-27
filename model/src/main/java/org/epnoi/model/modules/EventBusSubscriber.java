package org.epnoi.model.modules;

import org.epnoi.model.Event;

public interface EventBusSubscriber {

    void handle(Event event);

}
