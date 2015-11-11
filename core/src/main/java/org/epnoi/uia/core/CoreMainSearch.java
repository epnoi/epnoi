package org.epnoi.uia.core;

import org.epnoi.model.modules.Core;
import org.epnoi.model.search.SearchContext;
import org.epnoi.model.search.SearchResult;
import org.epnoi.model.search.SelectExpression;

import java.util.logging.Logger;

public class CoreMainSearch {
	// ---------------------------------------------------------------------------------
	private static final Logger logger = Logger.getLogger(CoreMainSearch.class
			.getName());

	
	// ----------------------------------------------------------------------------------------

	public static void main(String[] args) {

		Core core = CoreUtility.getUIACore();
		System.exit(0);

		SelectExpression selectExpression = new SelectExpression();

		selectExpression.setSolrExpression("content:clothes");

		SearchContext searchContext = new SearchContext();
		searchContext.getFacets().add("date");
//		searchContext.getFilterQueries().add("date:\"2013-12-06T17:54:21Z\"");
		// searchContext.getFilterQueries().add("date:\"2014-03-04T17:56:05Z\"");

		SearchResult searchResult = core.getSearchHandler().search(
				selectExpression, searchContext);
		
		System.out.println("Search result --> "+searchResult);
		System.out.println("Facets ---> " + searchResult.getFacets().size());

	}
}