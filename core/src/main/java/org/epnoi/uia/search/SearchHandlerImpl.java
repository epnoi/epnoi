package org.epnoi.uia.search;

import java.util.logging.Logger;

import org.epnoi.model.modules.Core;
import org.epnoi.model.modules.SearchHandler;
import org.epnoi.model.search.SearchContext;
import org.epnoi.model.search.SearchOrganizationResult;
import org.epnoi.model.search.SearchResult;
import org.epnoi.model.search.SearchSelectResult;
import org.epnoi.model.search.SelectExpression;
import org.epnoi.uia.search.organize.SearchOrganizer;
import org.epnoi.uia.search.project.SearchProjector;
import org.epnoi.uia.search.select.SearchSelector;

public class SearchHandlerImpl implements SearchHandler {
	private static final Logger logger = Logger.getLogger(SearchHandlerImpl.class
			.getName());
	private Core core;
	private SearchSelector selector;
	private SearchOrganizer organizer;
	private SearchProjector projector;

	// --------------------------------------------------------------------------------------

	public SearchHandlerImpl(Core core) {

		logger.info("Initializing the search handler");
		this.core = core;
		this.selector = new SearchSelector(this.core);
		this.organizer = new SearchOrganizer(this.core);
		this.projector = new SearchProjector(this.core);
	}

	public void init(Core core){
		this.core=core;
	}
	
	// --------------------------------------------------------------------------------------

	public SearchResult search(SelectExpression selectExpression,
			SearchContext searchContext) {
		logger.info("Handling a search request with the following parameters:");
		logger.info("SelectExpression: " + selectExpression);
		logger.info("SearchContext: " + searchContext);
		SearchSelectResult searchSelectResult = this.selector.select(
				selectExpression, searchContext);
		SearchOrganizationResult searchOrganizationResult = this.organizer
				.organize(searchSelectResult);
		SearchResult searchResult = this.projector
				.project(searchOrganizationResult);

		return searchResult;
	}

}
