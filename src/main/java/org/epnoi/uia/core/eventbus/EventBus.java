package org.epnoi.uia.core.eventbus;

import java.util.Date;
import java.util.logging.Logger;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;

public class EventBus {
	private static final Logger logger = Logger.getLogger(EventBus.class
			.getName());
	private AsyncEventBus bus = null;
	private DeadEventsSubscriber deadEventsSubscriber = new DeadEventsSubscriber();

	// ---------------------------------------------------------------------------------------------------------
	public EventBus() {
		this.bus = new AsyncEventBus(
				java.util.concurrent.Executors.newCachedThreadPool());
		this.bus.register(this.deadEventsSubscriber);

	}

	// ---------------------------------------------------------------------------------------------------------

	public void subscribe(EventBusSubscriber subscriber) {
		logger.info("Subscribing" + subscriber);
		this.bus.register(subscriber);
	}

	// ---------------------------------------------------------------------------------------------------------

	public void publish(EventBusPublisher publisher, Event event) {
		logger.info("The publisher " + publisher + " publis " + event);
		this.bus.post(event);
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
	
	public static void main(String[] args) {
		System.out.println("Starting the bus test");

		EventBus eventBus = new EventBus();

		TestSubscriber subs = new TestSubscriber(eventBus);

		eventBus.publish(new TestPublisher(eventBus), new Event(
				"no body should be listening"));
		eventBus.subscribe(subs);
		TestPublisher pub = new TestPublisher(eventBus);

		pub.doSomething();

	}

}

class TestPublisher implements EventBusPublisher {
	EventBus eventBus = null;

	public TestPublisher(EventBus eventBus) {
		this.eventBus = eventBus;
		// TODO Auto-generated constructor stub
	}

	public void doSomething() {
		while (true) {
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Posting! ");
			this.eventBus.publish(this, new Event((new Date()).toString()));
		}
	}
}

class TestSubscriber implements EventBusSubscriber {
	EventBus eventBus = null;

	@AllowConcurrentEvents
	@Subscribe
	public void onEvent(Event event) {
		System.out.println("Reacting to the event ! " + event);
	}

	public TestSubscriber(EventBus eventBus) {
		this.eventBus = eventBus;
		// TODO Auto-generated constructor stub
	}

}

class TestEvent {
	private String message = null;

	public TestEvent(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "TestEvent [message=" + message + "]";
	}

}
