package org.epnoi.uia.informationsources.subscribers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.atmosphere.cpr.Broadcaster;
import org.epnoi.model.InformationSourceNotification;
import org.epnoi.model.InformationSourceNotificationsSet;
import org.epnoi.model.InformationSourceSubscription;
import org.epnoi.model.Resource;
import org.epnoi.model.modules.Core;
import org.epnoi.model.modules.InformationAccessListener;
import org.epnoi.uia.informationhandler.events.EventsHelper;

import flexjson.JSONSerializer;

public class EventBus implements InformationAccessListener {
	private static final Logger logger = Logger.getLogger(EventBus.class
			.getName());
	private Map<String, Broadcaster> subscribers;
	private Core core;

	public EventBus(Core core) {
		
		this.subscribers = new HashMap<String, Broadcaster>();
		this.core = core;
		this.core.getInformationHandler().subscribe(this, "whatever");
	}

	// ------------------------------------------------------------------------------------------------

	public synchronized void subscribe(String URI, Broadcaster broadcaster) {
		logger.info("subscribe======"+URI+"================================================================start");
		logger.info("");
		logger.info("");
	//	logger.info("Adding the subscription of " + URI
//				+ " with the atmosphere broadcaster " + broadcaster);
		this.subscribers.put(URI, broadcaster);
		logger.info("");
		logger.info("");
		logger.info("");
		
//		logger.info("Subscribers "+this.subscribers);
		logger.info("");
		logger.info("");
		logger.info("");
		logger.info("subscribe==========["+this.subscribers.size()+"]============================================================end");
	}

	// ------------------------------------------------------------------------------------------------

	public void notify(String eventType, Resource resource) {
		
		if (resource instanceof InformationSourceSubscription
				&& eventType.equals(EventsHelper.UPDATE)) {
			Broadcaster broadcaster = this.subscribers.get(resource.getURI());
			System.out.println("                " + resource.getURI()
					+ "         >" + broadcaster + " the subscribers are "
					+ this.subscribers);
			List<InformationSourceNotification> notifications;
			notifications = this.core.getInformationSourcesHandler()
					.retrieveNotifications(resource.getURI());

			InformationSourceNotificationsSet informationSourceNotificationSet = new InformationSourceNotificationsSet();
			informationSourceNotificationSet.setURI(resource.getURI());
			informationSourceNotificationSet.setNotifications(notifications);

			Date date = new Date(System.currentTimeMillis());
			informationSourceNotificationSet.setTimestamp(date.toString());

			// The JSON serialization is made here
			JSONSerializer serializer = new JSONSerializer();
			String serializaedInformationSourceNotificationSet = serializer
					.include("notifications").serialize(
							informationSourceNotificationSet);
			broadcaster.broadcast(serializaedInformationSourceNotificationSet);
		}
	}

	// ------------------------------------------------------------------------------------------------
}
