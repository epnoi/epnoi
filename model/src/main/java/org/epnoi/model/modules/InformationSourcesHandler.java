package org.epnoi.model.modules;

import org.epnoi.model.InformationSourceNotification;
import org.epnoi.model.exceptions.EpnoiInitializationException;

import java.util.List;


public interface InformationSourcesHandler {
	void init() throws EpnoiInitializationException;
	List<InformationSourceNotification> retrieveNotifications(String informationSourceSubscription);
	
}
