package org.epnoi.uia.informationhandler.events;

import org.epnoi.model.Resource;

public interface InformationAccessListener {
	public void notify(String eventType, Resource resource);
}
