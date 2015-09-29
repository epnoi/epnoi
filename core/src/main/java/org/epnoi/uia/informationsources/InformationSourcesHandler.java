package org.epnoi.uia.informationsources;

import java.util.List;

import org.epnoi.model.InformationSourceNotification;



public interface InformationSourcesHandler {
	public List<InformationSourceNotification> retrieveNotifications(String informationSourceSubscription);
	
}
