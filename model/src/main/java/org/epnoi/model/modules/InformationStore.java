package org.epnoi.model.modules;

import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.model.Selector;
import org.epnoi.model.parameterization.InformationStoreParameters;
import org.epnoi.model.search.SearchContext;
import org.epnoi.model.search.SearchSelectResult;
import org.epnoi.model.search.SelectExpression;

import java.util.List;

public interface InformationStore {
	public void close();

	public void init(InformationStoreParameters parameters);

	public boolean test();

	public Resource get(Selector selector);

	public List<String> query(String queryExpression);

	public SearchSelectResult query(SelectExpression selectionExpression,
			SearchContext searchContext);

	public void put(Resource resource, Context context);

	public void remove(Selector selector);

	public boolean exists(Selector selector);

	public void update(Resource resource);

	public InformationStoreParameters getParameters();
}
