package org.epnoi.model.search;

import java.util.ArrayList;
import java.util.List;

public class SearchSelectResult {
	List<SelectionResultTuple> resources;
	List<Facet> facets;

	// --------------------------------------------------------------------
	
	public SearchSelectResult() {
		this.resources = new ArrayList<SelectionResultTuple>();
		this.facets = new ArrayList<Facet>();
	}

	// --------------------------------------------------------------------

	public SearchSelectResult(List<SelectionResultTuple> queryResults) {
		this.resources = queryResults;
	}

	// --------------------------------------------------------------------

	public List<SelectionResultTuple> getResources() {
		return resources;
	}

	// --------------------------------------------------------------------

	public void setResources(List<SelectionResultTuple> resources) {
		this.resources = resources;
	}

	// --------------------------------------------------------------------

	@Override
	public String toString() {
		return "SearchSelectResult [resources=" + resources + "]";
	}

	// --------------------------------------------------------------------

	public List<Facet> getFacets() {
		return facets;
	}

	// --------------------------------------------------------------------
	
	public void setFacets(List<Facet> facets) {
		this.facets = facets;
	}
	
	// --------------------------------------------------------------------

}
