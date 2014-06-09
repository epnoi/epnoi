package org.epnoi.uia.search.select;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;

public class SearchSelectResult {
	List<SelectionResultPair> resources;
	List<Facet> facets;

	// --------------------------------------------------------------------
	
	public SearchSelectResult() {
		this.resources = new ArrayList<SelectionResultPair>();
		this.facets = new ArrayList<Facet>();
	}

	// --------------------------------------------------------------------

	public SearchSelectResult(List<SelectionResultPair> queryResults) {
		this.resources = queryResults;
	}

	// --------------------------------------------------------------------

	public List<SelectionResultPair> getResources() {
		return resources;
	}

	// --------------------------------------------------------------------

	public void setResources(List<SelectionResultPair> resources) {
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
