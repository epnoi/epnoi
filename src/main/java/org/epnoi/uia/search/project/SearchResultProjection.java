package org.epnoi.uia.search.project;

import java.util.List;

import org.epnoi.uia.search.SearchResult;

public class SearchResultProjection {
	List<String> elements;

	//----------------------------------------------------------------------------
	
	public List<String> getElements() {
		return elements;
	}

	//----------------------------------------------------------------------------
	
	public void setElements(List<String> elements) {
		this.elements = elements;
	}
	
	//----------------------------------------------------------------------------

	SearchResultProjection(SearchResult searchResult) {
		this.elements = searchResult.getElements();
	}

}
