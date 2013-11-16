package org.epnoi.uia.informationaccess;

import org.epnoi.uia.parameterization.ParametersModel;
import org.epnoi.uia.rest.services.response.InformationStore;

import epnoi.model.Resource;

public interface InformationAccess {

	public void put(Resource resource);

	public Resource get(String URI);

	public void init(ParametersModel parameters);

	public void addInformationStore(InformationStore informationStore);

	public void removeInformationStore(String URI);
}
