package org.epnoi.model.modules;

import org.epnoi.model.Event;

public interface EventBus {

	void subscribe(EventBusSubscriber subscriber);

	void publish(EventBusPublisher publisher, Event event);

}