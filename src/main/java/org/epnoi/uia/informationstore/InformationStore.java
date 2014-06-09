package org.epnoi.uia.informationstore;

import java.util.List;

import org.epnoi.uia.parameterization.InformationStoreParameters;
import org.epnoi.uia.search.SearchContext;
import org.epnoi.uia.search.select.SearchSelectResult;
import org.epnoi.uia.search.select.SearchSelector;
import org.epnoi.uia.search.select.SelectExpression;

import epnoi.model.Context;
import epnoi.model.Resource;

public interface InformationStore {
	public void close();

	public void init(InformationStoreParameters parameters);

	public boolean test();

	public Resource get(Selector selector);

	public List<String> query(String queryExpression);
	
	public SearchSelectResult query(SelectExpression selectionExpression, SearchContext searchContext);

	public void put(Resource resource, Context context);
	
	public void remove(Selector selector);
	
	public void update(Resource resource);

	public InformationStoreParameters getParameters();
}
