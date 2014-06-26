package org.epnoi.uia.rest.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.atmosphere.cpr.Broadcaster;
import org.epnoi.model.InformationSourceNotification;
import org.epnoi.model.InformationSourceNotificationsSet;
import org.epnoi.model.Item;

import flexjson.JSONSerializer;

public class EventGenerator {

	private static final String GENERATOR_THREAD_NAME = "GeneratorThread";

	private volatile Thread generatorThread;

	/**
	 * Starts the generation of random events. Each event is broadcasted using
	 * the provided Broadcaster. This method does nothing if a Generator is
	 * already running.
	 */
	public synchronized void start(Broadcaster broadcaster, int interval) {
		if (generatorThread == null) {
			Generator generator = new Generator(broadcaster, interval);
			generatorThread = new Thread(generator, GENERATOR_THREAD_NAME);
			generatorThread.start();
		}
	}

	/**
	 * Stops the generation of random events. This method does nothing if no
	 * Generator was started before.
	 */
	public synchronized void stop() {
		if (generatorThread != null) {
			Thread tmpReference = generatorThread;
			generatorThread = null;
			tmpReference.interrupt();
		}
	}

	private class Generator implements Runnable {

		private final Random random;
		private final Broadcaster broadcaster;
		private final int interval;

		public Generator(Broadcaster broadcaster, int interval) {
			this.random = new Random();
			this.broadcaster = broadcaster;
			this.interval = interval;
		}

		@Override
		public void run() {
			Thread currentThread = Thread.currentThread();
			while (currentThread == generatorThread) {
				try {
					System.out.println("Generating notifications!");
					broadcaster.broadcast(_generateNotificationSet());
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					break;
				}
			}
		}

		private String _generateNotificationSet() {
			InformationSourceNotificationsSet informationSourceNotificationsSet = new InformationSourceNotificationsSet();
			informationSourceNotificationsSet.setTimestamp((new Date(System
					.currentTimeMillis())).toString());
			informationSourceNotificationsSet.setURI("La uri "
					+ random.nextInt());

			ArrayList<InformationSourceNotification> notifications = new ArrayList<InformationSourceNotification>();
			InformationSourceNotification notA = new InformationSourceNotification();
			InformationSourceNotification notB = new InformationSourceNotification();
			notA.setURI("notA");
			notB.setURI("notB");
			Item item = new Item();
			item.setURI("itemUri");
			notA.setResource(item);
			notifications.add(notA);
			notifications.add(notB);
			informationSourceNotificationsSet.setNotifications(notifications);

			JSONSerializer serializer = new JSONSerializer();
			String result = serializer.include("notifications").serialize(
					informationSourceNotificationsSet);
			System.out.println("result---> " + result);
			// return "{\"phonetype\":\"N95\",\"cat\":\"WP\"}";
			return result;
		}

	}

}
