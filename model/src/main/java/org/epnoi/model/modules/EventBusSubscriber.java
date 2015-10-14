package org.epnoi.model.modules;

import org.epnoi.model.Event;

public interface EventBusSubscriber {

    String topic();

    String group();

    void onEvent(Event event);

}
