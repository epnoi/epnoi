package org.epnoi.uia.informationaccess.wrapper;

import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreHelper;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.rdf.InformationSourceRDFHelper;

import epnoi.model.Context;
import epnoi.model.Resource;

public class InformationSourceWrapper implements Wrapper {
	Core core;

	// -------------------------------------------------------------------------------------------------------------

	public InformationSourceWrapper(Core core) {
		this.core = core;
	}

	// -------------------------------------------------------------------------------------------------------------


	public void put(Resource resource, Context context) {
		// InformationSource informationSource = (InformationSource) resource;

		InformationStore informationStore = core.getInformationStoresByType(
				InformationStoreHelper.RDF_INFORMATION_STORE).get(0);
		informationStore.put(resource, context);
	}

	// -------------------------------------------------------------------------------------------------------------

	public Resource get(String URI) {
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE,
				InformationSourceRDFHelper.INFORMATION_SOURCE_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);
		return informationStore.get(selector);
	}

	// -------------------------------------------------------------------------------------------------------------

	public void remove(String URI) {
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE,
				InformationSourceRDFHelper.INFORMATION_SOURCE_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);
		informationStore.remove(selector);
	}

	// -------------------------------------------------------------------------------------

	@Override
	public void update(Resource resource) {
		InformationStore informationStore = core.getInformationStoresByType(
				InformationStoreHelper.RDF_INFORMATION_STORE).get(0);
		informationStore.update(resource);

	}

	// -------------------------------------------------------------------------------------

}
