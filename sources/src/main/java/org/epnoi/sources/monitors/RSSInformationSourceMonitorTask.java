package org.epnoi.sources.monitors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.atmosphere.cpr.Broadcaster;
import org.epnoi.model.InformationSourceNotification;
import org.epnoi.model.InformationSourceNotificationsSet;
import org.epnoi.sources.generators.RSSInformationSourceRandomGenerator;

import flexjson.JSONSerializer;

class RSSInformationSourceMonitorTask implements Runnable {
	private Broadcaster broadcaster;
	private RSSInformationSourceMonitor monitor;
	private String informationSourceSubscriptionURI;
	private RSSInformationSourceRandomGenerator generator;

	private static final Logger logger = Logger
			.getLogger(RSSInformationSourceMonitorTask.class.getName());

	// ----------------------------------------------------------------------------------------

	public RSSInformationSourceMonitorTask(String informationSourceSubscriptionURI, Broadcaster broadcaster,
			RSSInformationSourceMonitor monitor) {
		this.broadcaster = broadcaster;
		this.monitor = monitor;
		this.informationSourceSubscriptionURI=informationSourceSubscriptionURI;
		this.generator= new RSSInformationSourceRandomGenerator();

	}

	// ----------------------------------------------------------------------------------------

	public void run() {
		logger.info("Starting a monitoring task for the information source subscription: "
				+ this.informationSourceSubscriptionURI);
		watch(this.informationSourceSubscriptionURI, this.broadcaster);

	}

	// ----------------------------------------------------------------------------------------

	public void watch(String informationSourceSubscriptionURI,
			Broadcaster broadcaster) {
		logger.info("Starting a watch fucntion ------------------------------------------------------------------");
		List<InformationSourceNotification> notifications;
		try {
			if (this.monitor.getCore() != null) {
				notifications = this.monitor
						.getCore()
						.getInformationSourcesHandler()
						.retrieveNotifications(informationSourceSubscriptionURI);
				
				
			} else {
				notifications = new ArrayList<InformationSourceNotification>();
			}

			InformationSourceNotificationsSet informationSourceNotificationSet = new InformationSourceNotificationsSet();
			informationSourceNotificationSet.setURI(informationSourceSubscriptionURI);
			informationSourceNotificationSet.setNotifications(notifications);
		
			Date date = new Date(System.currentTimeMillis());
			informationSourceNotificationSet.setTimestamp(date.toString());
			
			//The JSON serialization is made here
		
			JSONSerializer serializer = new JSONSerializer();
			String serializaedInformationSourceNotificationSet = serializer.include("notifications").serialize(
					informationSourceNotificationSet);

			broadcaster.broadcast(serializaedInformationSourceNotificationSet);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	// ----------------------------------------------------------------------------------------
	/*
	 * private void handleError(String errorMessage, String exceptionMessage) {
	 * if (exceptionMessage != null) { logger.severe(errorMessage); } else {
	 * logger.severe(errorMessage); logger.severe("The exception message was: "
	 * + errorMessage); } this.harvester.cancelTask(this.manifest.getURI()); }
	 */

}