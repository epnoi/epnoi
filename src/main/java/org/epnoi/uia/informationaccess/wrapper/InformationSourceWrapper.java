package org.epnoi.uia.informationaccess.wrapper;

import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreHelper;

import epnoi.model.Context;
import epnoi.model.InformationSource;
import epnoi.model.Resource;

public class InformationSourceWrapper implements Wrapper {
	Core core;

	// -------------------------------------------------------------------------------------------------------------

	public InformationSourceWrapper(Core core) {
		this.core = core;
	}

	// -------------------------------------------------------------------------------------------------------------

	public void put(Resource resource, Context context) {
		
	}

	public void put(Resource resource) {
		//InformationSource informationSource = (InformationSource) resource;

		InformationStore informationStore = core.getInformationStoresByType(
				InformationStoreHelper.RDF_INFORMATION_STORE).get(0);
		informationStore.put(resource);
	}

	// -------------------------------------------------------------------------------------------------------------

	public Resource get(String URI) {
		// TODO Auto-generated method stub
		return null;
	}

}
