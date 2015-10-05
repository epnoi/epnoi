package org.epnoi.model.modules;

import org.epnoi.model.Domain;
import org.epnoi.model.exceptions.EpnoiInitializationException;

public interface HarvestersHandler {
	public void init(Core core) throws EpnoiInitializationException;
	

	// -----------------------------------------------------------------------------------

	public void harvestURL(String url, Domain domain);
}