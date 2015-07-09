package org.epnoi.uia.demo;

import java.util.logging.Logger;

import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.harvester.filesystem.FilesystemHarvester;
import org.epnoi.uia.harvester.filesystem.FilesystemHarvesterParameters;

public class DemoDataLoader {
	Core core;
	private static final Logger logger = Logger.getLogger(DemoDataLoader.class
			.getName());

	FilesystemHarvester harvester = new FilesystemHarvester();

	//--------------------------------------------------------------------------------------------
	
	public void init(Core core){
		this.core=core;
		FilesystemHarvesterParameters parameters = _generateHarvesterParameters();

		try {
			harvester.init(core, parameters);
		} catch (EpnoiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private FilesystemHarvesterParameters _generateHarvesterParameters() {
		FilesystemHarvesterParameters parameters = new FilesystemHarvesterParameters();

		parameters.setParameter(FilesystemHarvesterParameters.CORPUS_LABEL,
				"CGTestCorpus");

		parameters.setParameter(FilesystemHarvesterParameters.CORPUS_URI,
				"http://CGTestCorpus");
		parameters.setParameter(FilesystemHarvesterParameters.VERBOSE, true);

		parameters.setParameter(FilesystemHarvesterParameters.OVERWRITE, true);

		parameters.setParameter(FilesystemHarvesterParameters.FILEPATH,
				"/opt/epnoi/epnoideployment/firstReviewResources/CGCorpus");
		return parameters;
	}
	
	//--------------------------------------------------------------------------------------------
	
	public void load() {
		_loadComputerGraphicsCorpus();
	}

	//--------------------------------------------------------------------------------------------
	
	private void _loadComputerGraphicsCorpus() {
		logger.info("Loading the computer graphics corpus");

		this.harvester.run();

	}
}
