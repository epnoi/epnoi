package org.epnoi.uia.informationaccess.wrapper;

import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreHelper;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.rdf.FeedRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.SearchRDFHelper;

import epnoi.model.Context;
import epnoi.model.Resource;

public class ItemWrapper implements Wrapper {
	Core core;

	// -------------------------------------------------------------------------------------------------------------

	public ItemWrapper(Core core) {
		this.core = core;
	}

	// -------------------------------------------------------------------------------------------------------------
	public void put(Resource resource, Context context) {
		System.out.println("Entra con estos valores ");
		System.out.println("R " + resource);
		System.out.println("C " + context);
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
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE)
				.get(0);

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, FeedRDFHelper.ITEM_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);
		return informationStore.get(selector);

	}
	
	// -------------------------------------------------------------------------------------

	public void remove(String URI) {
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE)
				.get(0);

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, FeedRDFHelper.ITEM_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);
		informationStore.remove(selector);

	}

	// -------------------------------------------------------------------------------------

		@Override
		public void update(Resource resource) {
			// TODO Auto-generated method stub
			
		}

		// -------------------------------------------------------------------------------------


}