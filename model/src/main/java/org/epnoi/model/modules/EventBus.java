package org.epnoi.model.modules;

import org.epnoi.model.Event;

public interface EventBus {

	void init();

	void subscribe(EventBusSubscriber subscriber);

	void publish(EventBusPublisher publisher, Event event);

	void destroy();

}