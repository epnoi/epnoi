package org.epnoi.uia.demo;

import java.util.List;
import java.util.logging.Logger;

import org.epnoi.model.Domain;
import org.epnoi.model.ResearchObject;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.harvester.filesystem.FilesystemHarvester;
import org.epnoi.uia.harvester.filesystem.FilesystemHarvesterParameters;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;

public class DemoDataLoader {
	Core core;
	private static final Logger logger = Logger.getLogger(DemoDataLoader.class
			.getName());

	FilesystemHarvester harvester = new FilesystemHarvester();

	// --------------------------------------------------------------------------------------------

	public void init(Core core) {
		this.core = core;
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

	// --------------------------------------------------------------------------------------------

	public void load() {
		_loadComputerGraphicsCorpus();
		_createTheSimpleDomain();
	}

	private void _createTheSimpleDomain() {
		Domain domain = new Domain();
		domain.setURI("http://simpledomain");
		domain.setExpression("sparqlexpression");
		domain.setLabel("simple domain");
		domain.setType(RDFHelper.PAPER_CLASS);
		domain.setResources("http://simpledomain");

		ResearchObject resources = new ResearchObject();
		resources.setURI("http://simpledomain/resources");

		this.core.getInformationHandler().put(resources,
				org.epnoi.model.Context.getEmptyContext());

		this.core.getInformationHandler().put(domain,
				org.epnoi.model.Context.getEmptyContext());

		List<String> domainURIs = this.core.getInformationHandler().getAll(
				RDFHelper.DOMAIN_CLASS);
		for (String domainURI : domainURIs) {
			this.core.getInformationHandler().remove(domainURI,
					RDFHelper.DOMAIN_CLASS);
			this.core.getInformationHandler().remove(domainURI + "/resources",
					RDFHelper.RESEARCH_OBJECT_CLASS);
		}

	}

	// --------------------------------------------------------------------------------------------

	private void _loadComputerGraphicsCorpus() {
		logger.info("Loading the computer graphics corpus");
		
		List<String> paperURIs = this.core.getInformationHandler().getAll(
				RDFHelper.PAPER_CLASS);
		for (String paperURI : paperURIs) {
			this.core.getInformationHandler().remove(paperURI,
					RDFHelper.PAPER_CLASS);
			
		}


		this.harvester.run();

	}
}
