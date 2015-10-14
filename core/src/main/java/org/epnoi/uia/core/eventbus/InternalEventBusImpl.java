package org.epnoi.uia.core.eventbus;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import org.epnoi.model.Event;
import org.epnoi.model.modules.EventBus;
import org.epnoi.model.modules.EventBusPublisher;
import org.epnoi.model.modules.EventBusSubscriber;

import java.io.IOException;
import java.util.logging.Logger;

public class InternalEventBusImpl implements EventBus {
	private static final Logger logger = Logger.getLogger(InternalEventBusImpl.class.getName());
	private AsyncEventBus bus = null;
	private DeadEventsSubscriber deadEventsSubscriber = new DeadEventsSubscriber();

	// ---------------------------------------------------------------------------------------------------------
	public InternalEventBusImpl() {
		this.bus = new AsyncEventBus(java.util.concurrent.Executors.newCachedThreadPool());
		this.bus.register(this.deadEventsSubscriber);

	}

	// ---------------------------------------------------------------------------------------------------------

	/* (non-Javadoc)
	 * @see org.epnoi.uia.core.eventbus.EventBusImpl#subscribe(org.epnoi.uia.core.eventbus.EventBusSubscriber)
	 */
	@Override
	public void subscribe(EventBusSubscriber subscriber) {
		logger.info("Subscribing" + subscriber);
		this.bus.register(subscriber);
	}

	// ---------------------------------------------------------------------------------------------------------

	/* (non-Javadoc)
	 * @see org.epnoi.uia.core.eventbus.EventBusImpl#publish(org.epnoi.uia.core.eventbus.EventBusPublisher, org.epnoi.uia.core.eventbus.Event)
	 */
	@Override
	public void publish(EventBusPublisher publisher, Event event) {
		logger.info("The publisher " + publisher + " publis " + event);
		this.bus.post(event);
	}

	// ---------------------------------------------------------------------------------------------------------

	@Override
	public void destroy() throws IOException {

	}

	// ---------------------------------------------------------------------------------------------------------

	class DeadEventsSubscriber {
		@AllowConcurrentEvents
		@Subscribe
		public void handleDeadEvent(DeadEvent deadEvent) {
			System.out.println("dead event handling!!!!! ------> "
					+ deadEvent.getEvent());

		}
	}

	// ---------------------------------------------------------------------------------------------------------

}


