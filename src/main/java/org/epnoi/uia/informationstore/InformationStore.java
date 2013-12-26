package org.epnoi.uia.informationstore;

import java.util.List;

import org.epnoi.uia.parameterization.InformationStoreParameters;

import epnoi.model.Context;
import epnoi.model.Resource;

public interface InformationStore {
	public void close();

	public void init(InformationStoreParameters parameters);

	public boolean test();

	public Resource get(String URI);

	public List<String> query(String queryExpression);

	public void put(Resource resource);

	public void put(Resource resource, Context context);

	public InformationStoreParameters getParameters();
}
