package org.epnoi.uia.informationaccess.wrapper;

import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreHelper;

import epnoi.model.Context;
import epnoi.model.Resource;

public class FeedWrapper implements Wrapper {
	Core core;

	// -------------------------------------------------------------------------------------------------------------

	public FeedWrapper(Core core) {
		this.core = core;
	}

	// -------------------------------------------------------------------------------------------------------------
	public void put(Resource resource, Context context) {
		System.out.println("Entra con estos valores ");
		System.out.println("R " + resource);
		//System.out.println("C " + context);
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);
		// System.out.println("--------------------------------------------->  "+informationStore);
		informationStore.put(resource);
		informationStore = this.core.getInformationStoresByType(
				InformationStoreHelper.SOLR_INFORMATION_STORE).get(0);
		informationStore.put(resource, context);

	}

	public void put(Resource resource) {

		System.out
				.println("--------------------------------------------->  "
						+ this.core
								.getInformationStoresByType(InformationStoreHelper.RDF_INFORMATION_STORE));
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);
		// System.out.println("--------------------------------------------->  "+informationStore);
		informationStore.put(resource);
	}

	// -------------------------------------------------------------------------------------------------------------

	public Resource get(String URI) {
		// TODO Auto-generated method stub
		return null;
	}

	public void remove(String URI) {
		// TODO Auto-generated method stub

	}

}