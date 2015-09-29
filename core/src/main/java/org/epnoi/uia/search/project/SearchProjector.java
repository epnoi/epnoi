package org.epnoi.uia.search.project;

import org.epnoi.uia.core.Core;
import org.epnoi.uia.search.SearchResult;
import org.epnoi.uia.search.organize.SearchOrganizationResult;

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
