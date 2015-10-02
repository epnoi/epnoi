package org.epnoi.learner;

import java.util.List;
import java.util.logging.Logger;

import org.epnoi.learner.automata.OntologyLearningWorkflowParameters;
import org.epnoi.model.Domain;
import org.epnoi.uia.core.Core;

public class DomainsGatherer {
	private static final Logger logger = Logger.getLogger(DomainsGatherer.class
			.getName());
	private Core core;
	private List<Domain> consideredDomains;
	private String targetDomain;

	private OntologyLearningWorkflowParameters parameters;
	private DomainsTable domainsTable;

	// -----------------------------------------------------------------------------------

	public void init(Core core, OntologyLearningWorkflowParameters parameters) {
		logger.info("Initializing the DomainsGatherer with the following parameters: ");
		logger.info(parameters.toString());
		this.core = core;
		this.parameters = parameters;

		this.consideredDomains = (List<Domain>) this.parameters
				.getParameterValue(OntologyLearningWorkflowParameters.CONSIDERED_DOMAINS);

		this.domainsTable = new DomainsTable();
		this.targetDomain = (String) this.parameters
				.getParameterValue(OntologyLearningWorkflowParameters.TARGET_DOMAIN);
	}

	// -----------------------------------------------------------------------------------

	public DomainsTable gather() {
		logger.info("Gathering the DomainsTable");
		for (Domain domain : this.consideredDomains) {
			this.domainsTable.addDomain(domain);
			logger.info("Gathering the domain " + domain);

			List<String> foundURIs = core.getDomainsHandler().gather(domain);
			logger.info("Found initially " + foundURIs.size()
					+ " elements in the domain");

			this.domainsTable.addDomainResources(domain.getURI(), foundURIs);

		}
		this.domainsTable.setTargetDomain(targetDomain);
		return this.domainsTable;
	}
	
	// -----------------------------------------------------------------------------------

	public DomainsTable gather(Domain domain) {
		logger.info("Gathering the DomainsTable");
		this.domainsTable.addDomain(domain);
		logger.info("Gathering the domain " + domain);

		List<String> foundURIs = core.getDomainsHandler().gather(domain);
		logger.info("Found initially " + foundURIs.size()
				+ " elements in the domain");

		this.domainsTable.addDomainResources(domain.getURI(), foundURIs);

		this.domainsTable.setTargetDomain(targetDomain);
		return this.domainsTable;
	}

}
