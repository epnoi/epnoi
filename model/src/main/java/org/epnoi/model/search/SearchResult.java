package org.epnoi.model.search;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
	List<OrganizationResultPair> resources;
	List<Facet> facets;

	public SearchResult(){
		resources=new ArrayList<OrganizationResultPair>();
		this.facets = new ArrayList<Facet>();
	}
	
	// ----------------------------------------------------------------------------

	public List<Facet> getFacets() {
		return facets;
	}
	
	// ----------------------------------------------------------------------------

	public void setFacets(List<Facet> facets) {
		this.facets = facets;
	}

	// ----------------------------------------------------------------------------
	
	public List<OrganizationResultPair> getResources() {
		return resources;
	}

	// ----------------------------------------------------------------------------

	public void setResources(List<OrganizationResultPair> resources) {
		this.resources = resources;
	}

	// ----------------------------------------------------------------------------

	public SearchResult(SearchOrganizationResult searchOrganizationResult) {
		this.resources = searchOrganizationResult.getElements();
		this.facets= searchOrganizationResult.getFacets();
	}

	// ----------------------------------------------------------------------------

	@Override
	public String toString() {
		return "SearchResult [resources=" + resources + ", facets=" + facets
				+ "]";
	}
	
	// ----------------------------------------------------------------------------
}
