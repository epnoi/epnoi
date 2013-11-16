package org.epnoi.uia.informationstore;

import org.epnoi.uia.parameterization.InformationStoreParameters;

import epnoi.model.Resource;

public interface InformationStore {
	public void close();

	public void init(InformationStoreParameters parameters);
	
	public boolean test();
	
	public Resource get(String URI);
	
	public void put(Resource resource);
	
	public InformationStoreParameters getParameters();
}
