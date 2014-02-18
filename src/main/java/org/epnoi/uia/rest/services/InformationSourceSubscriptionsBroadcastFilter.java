package org.epnoi.uia.rest.services;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.BroadcastFilter.BroadcastAction.ACTION;
import org.atmosphere.cpr.PerRequestBroadcastFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import epnoi.model.InformationSourceNotification;
import epnoi.model.InformationSourceNotificationsSet;

public class InformationSourceSubscriptionsBroadcastFilter implements PerRequestBroadcastFilter {

	private final Logger LOG = LoggerFactory.getLogger(InformationSourceSubscriptionsBroadcastFilter.class);

	@Override
	public BroadcastAction filter(Object originalMessage, Object message) {
		System.out.println("-------------");
		InformationSourceNotificationsSet event = (InformationSourceNotificationsSet) message;
		String json = JsonUtils.toJson(event);
		System.out.println("--------------------> "+json);
		return new BroadcastAction(ACTION.CONTINUE, event);
	}

	@Override
	public BroadcastAction filter(AtmosphereResource resource,
			Object originalMessage, Object message) {
	
		
		InformationSourceNotificationsSet event = (InformationSourceNotificationsSet) message;

		
		String json = JsonUtils.toJson(event);
		System.out.println("--------------------> "+json);
		return new BroadcastAction(json);

	}

}