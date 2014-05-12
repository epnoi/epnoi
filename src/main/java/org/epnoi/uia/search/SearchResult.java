package org.epnoi.uia.search;

import java.util.List;

import org.epnoi.uia.search.organize.OrganizationResultPair;
import org.epnoi.uia.search.organize.SearchOrganizationResult;
import org.epnoi.uia.search.select.Facet;

import epnoi.model.Resource;

public class SearchResult {
	List<OrganizationResultPair> resources;
	List<Facet> facets;

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
