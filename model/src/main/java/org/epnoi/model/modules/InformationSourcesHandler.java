package org.epnoi.model.modules;

import java.util.List;

import org.epnoi.model.InformationSourceNotification;



public interface InformationSourcesHandler {
	public List<InformationSourceNotification> retrieveNotifications(String informationSourceSubscription);
	
}
