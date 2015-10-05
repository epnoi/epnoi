package org.epnoi.model.modules;

import org.epnoi.model.search.SearchContext;
import org.epnoi.model.search.SearchResult;
import org.epnoi.model.search.SelectExpression;

public interface SearchHandler {
	public SearchResult search(SelectExpression selectExpression,
			SearchContext searchContext);
	public void init(Core core);
}