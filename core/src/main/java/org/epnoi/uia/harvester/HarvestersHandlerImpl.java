package org.epnoi.uia.harvester;

import org.epnoi.model.Domain;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.modules.HarvestersHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@Component
public class HarvestersHandlerImpl implements HarvestersHandler {
    private static final Logger logger = Logger.getLogger(HarvestersHandlerImpl.class.getName());
    @Autowired
    private Core core;

    /*
     * URLHarvester urlHarvester = new URLHarvester();
     */
    // -----------------------------------------------------------------------------------
    @PostConstruct
    @Override
    public void init() throws EpnoiInitializationException {
        logger.info("Initializing the harvester handler");

		/*
         * URLHarvesterParameters urlHarvesterParameters = new
		 * URLHarvesterParameters();
		 * 
		 * urlHarvesterParameters.setParameter(URLHarvesterParameters.
		 * VERBOSE_PARAMETER, true);
		 * 
		 * urlHarvesterParameters.setParameter(URLHarvesterParameters.
		 * OVERWRITE_PARAMETER, true);
		 * 
		 * 
		 * 
		 * this.urlHarvester.init(core, urlHarvesterParameters);
		 */
    }

    // -----------------------------------------------------------------------------------
    @Override
    public void harvestURL(String url, Domain domain) {
        // this.urlHarvester.harvest(url, domain);
    }

    // -----------------------------------------------------------------------------------

}
