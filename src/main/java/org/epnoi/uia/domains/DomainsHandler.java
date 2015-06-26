package org.epnoi.uia.domains;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.epnoi.model.Domain;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.learner.DomainsTable;
import org.epnoi.uia.learner.OntologyLearningWorkflowParameters;

public class DomainsHandler {
	private static final Logger logger = Logger.getLogger(DomainsHandler.class
			.getName());
	private Core core;

	// -----------------------------------------------------------------------------------

	public void init(Core core) {
		logger.info("Initializing the DomainsHandler");
		this.core = core;
	}

	// -----------------------------------------------------------------------------------

	public List<String> gather(Domain domain) {
		logger.info("Gathering the domain URIs");

		logger.info("Gathering the domain " + domain);
		List<String> foundURIs = core.getAnnotationHandler().getLabeledAs(
				domain.getURI(), domain.getType());
		logger.info("Found initially " + foundURIs.size()
				+ " elements in the domain " + domain.getURI());

		List<String> cleanedURI = _cleanMissingResources(foundURIs, domain);
		return cleanedURI;
	}

	// -----------------------------------------------------------------------------------
	/**
	 * Method that removes from a list of resources URIs those that are not stored in the UIA.
	 * @param foundURIs List of URIs that were initially found for the domain
	 * @param domain The current domain
	 * @return
	 */
	private List<String> _cleanMissingResources(List<String> foundURIs,
			Domain domain) {
		List<String> cleanedURIs = new ArrayList<String>();
		for (String uri : foundURIs) {
			if (core.getInformationHandler().contains(uri, domain.getType())) {
				cleanedURIs.add(uri);
			}
		}

		return cleanedURIs;
	}
}
