package org.epnoi.uia.search.select;

import java.util.List;

import epnoi.model.Resource;

public class SearchSelectResult {
	List<String> resources;

	// --------------------------------------------------------------------

	public SearchSelectResult(List<String> queryResults) {
		this.resources = queryResults;
	}

	// --------------------------------------------------------------------

	public List<String> getResources() {
		return resources;
	}

	// --------------------------------------------------------------------

	public void setResources(List<String> resources) {
		this.resources = resources;
	}

	// --------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "SearchSelectResult [resources=" + resources + "]";
	}
	
	// --------------------------------------------------------------------
}
