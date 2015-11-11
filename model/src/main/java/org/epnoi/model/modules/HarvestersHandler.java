package org.epnoi.model.modules;

import org.epnoi.model.Domain;
import org.epnoi.model.exceptions.EpnoiInitializationException;

public interface HarvestersHandler {
	void init() throws EpnoiInitializationException;
	

	void harvestURL(String url, Domain domain);
}