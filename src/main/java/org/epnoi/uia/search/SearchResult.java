package org.epnoi.uia.search;

import java.util.List;

import org.epnoi.uia.search.select.SearchSelectResult;

public class SearchResult {
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
	
	public SearchResult(SearchSelectResult searchSelectResult) {
	this.elements=searchSelectResult.getResources();
	}

	//----------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "SearchResult [elements=" + elements + "]";
	}
}
