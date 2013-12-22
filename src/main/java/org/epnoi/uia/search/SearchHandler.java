package org.epnoi.uia.search;

import java.util.logging.Logger;

import org.epnoi.uia.search.organize.SearchOrganizer;
import org.epnoi.uia.search.project.SearchProjector;
import org.epnoi.uia.search.select.SearchSelector;
import org.epnoi.uia.search.select.SelectExpression;

public class SearchHandler {
	private static final Logger logger = Logger.getLogger(SearchHandler.class
			.getName());
	private SearchSelector selector;
	private SearchOrganizer organizer;
	private SearchProjector projector;

	// --------------------------------------------------------------------------------------

	public void init() {
		this.selector = new SearchSelector();
		this.organizer = new SearchOrganizer();
		this.projector = new SearchProjector();
	}

	// --------------------------------------------------------------------------------------

	SearchResult search(SelectExpression selectExpression,
			SearchContext searchContext) {
		logger.info("Handling a search request with the following parameters:");
		logger.info("SelectExpression: " + selectExpression);
		logger.info("SearchContext: " + searchContext);
		
		return new SearchResult();
	}

}
