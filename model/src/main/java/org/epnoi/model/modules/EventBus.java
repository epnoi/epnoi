package org.epnoi.model.modules;

import org.epnoi.model.Event;

public interface EventBus {

	void subscribe(EventBusSubscriber subscriber, BindingKey bindingKey);

	void unsubscribe(EventBusSubscriber subscriber);

	void post(Event event, RoutingKey routingKey);

}