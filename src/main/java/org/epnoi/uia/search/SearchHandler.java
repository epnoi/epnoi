package org.epnoi.uia.search;

import java.util.logging.Logger;

import org.epnoi.uia.core.Core;
import org.epnoi.uia.search.organize.SearchOrganizer;
import org.epnoi.uia.search.project.SearchProjector;
import org.epnoi.uia.search.project.SearchResultProjection;
import org.epnoi.uia.search.select.SearchSelectResult;
import org.epnoi.uia.search.select.SearchSelector;
import org.epnoi.uia.search.select.SelectExpression;

public class SearchHandler {
	private static final Logger logger = Logger.getLogger(SearchHandler.class
			.getName());
	private Core core;
	private SearchSelector selector;
	private SearchOrganizer organizer;
	private SearchProjector projector;

	// --------------------------------------------------------------------------------------

	public SearchHandler(Core core) {

		logger.info("Initializing the search handler");
		this.core=core;
		this.selector = new SearchSelector(this.core);
		this.organizer = new SearchOrganizer();
		this.projector = new SearchProjector();
	}
	
	// --------------------------------------------------------------------------------------


	public SearchResultProjection search(SelectExpression selectExpression,
			SearchContext searchContext) {
		logger.info("Handling a search request with the following parameters:");
		logger.info("SelectExpression: " + selectExpression);
		logger.info("SearchContext: " + searchContext);
		SearchSelectResult selectResult = this.selector
				.select(selectExpression);
		SearchResult searchResult = this.organizer.organize(selectResult);
		SearchResultProjection searchResultProjection = this.projector
				.project(searchResult);

		return searchResultProjection;
	}

}
