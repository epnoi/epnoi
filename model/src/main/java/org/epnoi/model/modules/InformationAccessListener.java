package org.epnoi.model.modules;

import org.epnoi.model.Resource;

public interface InformationAccessListener {
	public void notify(String eventType, Resource resource);
}
