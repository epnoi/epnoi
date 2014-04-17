package org.epnoi.uia.informationaccess.events;

import epnoi.model.Resource;

public interface InformationAccessListener {
	public void notify(String eventType, Resource resource);
}
