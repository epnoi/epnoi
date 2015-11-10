package org.epnoi.model.modules;

import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.search.SearchContext;
import org.epnoi.model.search.SearchResult;
import org.epnoi.model.search.SelectExpression;

public interface SearchHandler {

	void init() throws EpnoiInitializationException;

	SearchResult search(SelectExpression selectExpression,
			SearchContext searchContext);

}