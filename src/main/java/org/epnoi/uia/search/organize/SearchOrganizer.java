package org.epnoi.uia.search.organize;

import java.util.logging.Logger;

import org.epnoi.model.Resource;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.search.select.SearchSelectResult;
import org.epnoi.uia.search.select.SelectionResultTuple;

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
			
			
			System.out.println("peta ----------------> "+selectPair.getResourceURI());
			Resource resource = this.core.getInformationAccess().get(
					selectPair.getResourceURI(), selectPair.getType());

			OrganizationResultPair organizationPair = new OrganizationResultPair();
			organizationPair.setResource(resource);
			organizationPair.setScore(selectPair.getScore());
			searchOrganizationResult.getElements().add(organizationPair);
		}

		searchOrganizationResult.setFacets(searchSelection.getFacets());

		return searchOrganizationResult;
	}

	// ---------------------------------------------------------------------------
}