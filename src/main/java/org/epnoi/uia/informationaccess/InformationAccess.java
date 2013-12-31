package org.epnoi.uia.informationaccess;

import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.parameterization.ParametersModel;

import epnoi.model.Context;
import epnoi.model.Resource;

public interface InformationAccess {

	public void put(Resource resource);
	
	public void put(Resource resource, Context context);

	public Resource get(String URI);
	
	public Resource get(String URI, String resourceType);

	public void init(ParametersModel parameters);

	public void addInformationStore(InformationStore informationStore);

	public void removeInformationStore(String URI);
	
	}
