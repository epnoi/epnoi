package org.epnoi.uia.search.organize;

import java.util.logging.Logger;

import org.epnoi.uia.search.SearchHandler;
import org.epnoi.uia.search.SearchResult;
import org.epnoi.uia.search.select.SearchSelectResult;

public class SearchOrganizer {
	private static final Logger logger = Logger.getLogger(SearchOrganizer.class
			.getName());

	// ---------------------------------------------------------------------------
	
	public SearchResult organize(SearchSelectResult searchSelection) {
		logger.info("Organizing the searh select result " + searchSelection);
		return new SearchResult(searchSelection);
	}
}