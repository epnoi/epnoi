package org.epnoi.learner;

import org.epnoi.model.Domain;
import org.epnoi.model.modules.Core;

import java.util.List;
import java.util.logging.Logger;

public class DomainsTableCreator {
	private static final Logger logger = Logger.getLogger(DomainsTableCreator.class
			.getName());
	private Core core;
	private List<Domain> consideredDomains;
	private String targetDomain;

	private LearningParameters parameters;
	private DomainsTable domainsTable;

	// -----------------------------------------------------------------------------------

	public void init(Core core, LearningParameters parameters) {
		logger.info("Initializing the DomainsTableCreator with the following parameters: ");
		logger.info(parameters.toString());
		this.core = core;
		this.parameters = parameters;

		this.consideredDomains = (List<Domain>) this.parameters
				.getParameterValue(LearningParameters.CONSIDERED_DOMAINS);

		this.domainsTable = new DomainsTable();
		this.targetDomain = (String) this.parameters
				.getParameterValue(LearningParameters.TARGET_DOMAIN_URI);
	}

	// -----------------------------------------------------------------------------------

	public DomainsTable create() {
		logger.info("Creating the DomainsTable");
		for (Domain domain : this.consideredDomains) {
			this.domainsTable.addDomain(domain);
			logger.info("Creating the domain " + domain);

			List<String> foundURIs = core.getDomainsHandler().gather(domain);
			logger.info("Found initially " + foundURIs.size()
					+ " elements in the domain");

			this.domainsTable.addDomainResources(domain.getUri(), foundURIs);

		}
		this.domainsTable.setTargetDomain(targetDomain);
		return this.domainsTable;
	}
	
	// -----------------------------------------------------------------------------------

	public DomainsTable create(Domain domain) {
		logger.info("Creating the DomainsTable");
		this.domainsTable.addDomain(domain);
		logger.info("Creating the domain " + domain);

		List<String> foundURIs = core.getDomainsHandler().gather(domain);
		logger.info("Found initially " + foundURIs.size()
				+ " elements in the domain");

		this.domainsTable.addDomainResources(domain.getUri(), foundURIs);

		this.domainsTable.setTargetDomain(targetDomain);
		return this.domainsTable;
	}

}
