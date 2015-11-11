package org.epnoi.uia.search.organize;

import org.epnoi.model.Resource;
import org.epnoi.model.modules.Core;
import org.epnoi.model.search.OrganizationResultPair;
import org.epnoi.model.search.SearchOrganizationResult;
import org.epnoi.model.search.SearchSelectResult;
import org.epnoi.model.search.SelectionResultTuple;

import java.util.logging.Logger;

public class SearchOrganizer {
	private static final Logger logger = Logger.getLogger(SearchOrganizer.class
			.getName());
	private Core core;

	// --------------------------------------------------------------------------

	public SearchOrganizer(Core core) {
		this.core = core;
	}

	// ---------------------------------------------------------------------------

	public SearchOrganizationResult organize(SearchSelectResult searchSelection) {

		logger.info("Organizing the searh select result ");
		SearchOrganizationResult searchOrganizationResult = new SearchOrganizationResult();
		for (SelectionResultTuple selectPair : searchSelection.getResources()) {

			System.out.println("peta ----------------> "
					+ selectPair.getResourceURI());

			if (this.core.getInformationHandler().contains(
					selectPair.getResourceURI(), selectPair.getType())) {
				Resource resource = this.core.getInformationHandler().get(
						selectPair.getResourceURI(), selectPair.getType());

				OrganizationResultPair organizationPair = new OrganizationResultPair();
				organizationPair.setResource(resource);
				organizationPair.setScore(selectPair.getScore());
				searchOrganizationResult.getElements().add(organizationPair);
			}
		}

		searchOrganizationResult.setFacets(searchSelection.getFacets());

		return searchOrganizationResult;
	}

	// ---------------------------------------------------------------------------
}