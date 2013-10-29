package org.epnoi.uia.informationstore;

import org.epnoi.uia.parameterization.InformationStoreParameters;

public interface InformationStore {
	public void close();

	public void init(InformationStoreParameters parameters);
	
	public boolean test();
	
	public InformationStoreParameters getParameters();
}
