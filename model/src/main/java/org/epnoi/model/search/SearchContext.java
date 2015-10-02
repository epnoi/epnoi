package org.epnoi.model.search;

import java.util.ArrayList;
import java.util.List;

public class SearchContext {
	private List<String> facets = new ArrayList<>();
	private List<String> filterQueries = new ArrayList<>();

	// -------------------------------------------------------------------------------------

	public List<String> getFilterQueries() {
		return filterQueries;
	}
	
	// -------------------------------------------------------------------------------------

	public void setFilterQueries(List<String> filterQueries) {
		this.filterQueries = filterQueries;
	}
	
	// -------------------------------------------------------------------------------------

	public List<String> getFacets() {
		return facets;
	}

	// -------------------------------------------------------------------------------------

	public void setFacets(List<String> facets) {
		this.facets = facets;
	}

	// -------------------------------------------------------------------------------------
}
