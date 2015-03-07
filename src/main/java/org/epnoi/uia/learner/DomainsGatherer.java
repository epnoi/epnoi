package org.epnoi.uia.learner;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.epnoi.uia.commons.Parameters;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.learner.terms.TermsExtractor;

public class DomainsGatherer {
	private static final Logger logger = Logger.getLogger(DomainsGatherer.class
			.getName());
	private Core core;
	private List<String> consideredDomains;
	private String targetDomain;
	private String consideredResources;
	private OntologyLearningParameters parameters;
	private DomainsTable domainsTable;

	// -----------------------------------------------------------------------------------

	public void init(Core core, OntologyLearningParameters parameters) {
		logger.info("Initializing the TermExtractor for the domains ");
		this.core = core;
		this.parameters = parameters;

		this.consideredDomains = (List<String>) this.parameters
				.getParameterValue(OntologyLearningParameters.CONSIDERED_DOMAINS);
		this.consideredResources = (String) this.parameters
				.getParameterValue(OntologyLearningParameters.CONSIDERED_RESOURCES);

		this.domainsTable = new DomainsTable();
	}

	// -----------------------------------------------------------------------------------

	public DomainsTable gather() {

		for (String domain : this.consideredDomains) {
			List<String> foundURIs = core.getAnnotationHandler().getLabeledAs(
					domain, this.consideredResources);
System.out.println("FOUND> "+foundURIs.size());
			this.domainsTable.getDomains().put(domain,
					_cleanResources(foundURIs));
		}
		this.domainsTable.setTargetDomain(targetDomain);
		return this.domainsTable;
	}

	// -----------------------------------------------------------------------------------

	private List<String> _cleanResources(List<String> foundURIs) {
		List<String> cleanedURIs = new ArrayList<String>();
		for (String uri : foundURIs) {
			if (core.getInformationHandler().contains(uri,
					this.consideredResources)) {
				cleanedURIs.add(uri);
			}
		}
		return cleanedURIs;
	}
}
