package org.epnoi.uia.search.project;

import org.epnoi.uia.search.SearchResult;

public class SearchProjector {
	public SearchResultProjection project(SearchResult selectResult) {
		return new SearchResultProjection(selectResult);
	}
}
