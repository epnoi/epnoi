package org.epnoi.model.search;

import java.util.ArrayList;
import java.util.List;

public class SearchOrganizationResult {
	List<OrganizationResultPair> elements;
	List<Facet> facets;

	// ----------------------------------------------------------------------------

	@Override
	public String toString() {
		return "SearchOrganizationResult [elements=" + elements + ", facets="
				+ facets + "]";
	}

	public SearchOrganizationResult() {
		this.elements = new ArrayList<>();
		this.facets = new ArrayList<>();

	}

	// ----------------------------------------------------------------------------

	public List<OrganizationResultPair> getElements() {
		return elements;
	}

	// ----------------------------------------------------------------------------

	public void setElements(List<OrganizationResultPair> elements) {
		this.elements = elements;
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
