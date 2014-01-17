package org.epnoi.uia.informationsource;

import java.util.List;

import epnoi.model.InformationSourceNotification;

public interface InformationSourcesHandler {
	public List<InformationSourceNotification> retrieveNotifications(String informationSourceSubscription);

}
