package org.epnoi.uia.harvester;

import java.util.logging.Logger;

import org.epnoi.model.Domain;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.harvester.url.URLHarvester;
import org.epnoi.uia.harvester.url.URLHarvesterParameters;

public class HarvestersHandler {
	private static final Logger logger = Logger
			.getLogger(HarvestersHandler.class.getName());
	private Core core;
	URLHarvester urlHarvester = new URLHarvester();

	// -----------------------------------------------------------------------------------

	public void init(Core core) throws EpnoiInitializationException {
		logger.info("Initializing the HarvestersHandler");
		this.core = core;

		URLHarvesterParameters urlHarvesterParameters = new URLHarvesterParameters();

		urlHarvesterParameters.setParameter(URLHarvesterParameters.VERBOSE_PARAMETER, true);

		urlHarvesterParameters.setParameter(URLHarvesterParameters.OVERWRITE_PARAMETER,
				true);

		
		
		this.urlHarvester.init(core, urlHarvesterParameters);

	}

	// -----------------------------------------------------------------------------------

	public void harvestURL(String url, Domain domain) {
		this.urlHarvester.harvest(url, domain);
	}

	// -----------------------------------------------------------------------------------

}
