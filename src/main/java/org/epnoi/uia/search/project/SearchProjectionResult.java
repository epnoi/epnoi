package org.epnoi.uia.search.project;

import java.util.List;

import org.epnoi.uia.search.organize.OrganizationResultPair;
import org.epnoi.uia.search.organize.SearchOrganizationResult;
import org.epnoi.uia.search.select.Facet;

public class SearchProjectionResult {
	List<OrganizationResultPair> elements;
	List<Facet> facets;

	// ----------------------------------------------------------------------------

	public List<OrganizationResultPair> getElements() {
		return elements;
	}

	// ----------------------------------------------------------------------------

	public void setElements(List<OrganizationResultPair> elements) {
		this.elements = elements;
	}

	// ----------------------------------------------------------------------------

	SearchProjectionResult(SearchOrganizationResult selectResult) {
		this.elements = selectResult.getElements();
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

}
