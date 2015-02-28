package org.epnoi.uia.informationstore;

import java.util.List;

import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.uia.parameterization.InformationStoreParameters;
import org.epnoi.uia.search.SearchContext;
import org.epnoi.uia.search.select.SearchSelectResult;
import org.epnoi.uia.search.select.SelectExpression;

public class MapInformationStore implements InformationStore {
	private InformationStoreParameters parameters;

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(InformationStoreParameters parameters) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean test() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Resource get(Selector selector) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> query(String queryExpression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchSelectResult query(SelectExpression selectionExpression,
			SearchContext searchContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void put(Resource resource, Context context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(Selector selector) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean exists(Selector selector) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update(Resource resource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InformationStoreParameters getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

}
