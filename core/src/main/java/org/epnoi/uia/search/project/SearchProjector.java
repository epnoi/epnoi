package org.epnoi.uia.search.project;

import org.epnoi.model.modules.Core;
import org.epnoi.model.search.SearchOrganizationResult;
import org.epnoi.model.search.SearchResult;

public class SearchProjector {
	Core core;

	// ------------------------------------------------------------------------

	public SearchProjector(Core core) {
		this.core = core;
	}

	// ------------------------------------------------------------------------

	public SearchResult project(
			SearchOrganizationResult searchOrganizationResult) {

		return new SearchResult(searchOrganizationResult);

	}

	// ------------------------------------------------------------------------

}
