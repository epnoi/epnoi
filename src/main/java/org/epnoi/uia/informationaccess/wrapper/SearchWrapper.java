package org.epnoi.uia.informationaccess.wrapper;

import java.util.logging.Logger;

import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreHelper;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.rdf.SearchRDFHelper;

import epnoi.model.Context;
import epnoi.model.Resource;

public class SearchWrapper implements Wrapper {
	private static final Logger logger = Logger.getLogger(SearchWrapper.class
			.getName());

	Core core;

	// -------------------------------------------------------------------------------------

	public SearchWrapper(Core core) {
		this.core = core;
	}

	// -------------------------------------------------------------------------------------

	public void put(Resource resource, Context context) {

		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);
		informationStore.put(resource, context);
		informationStore = this.core.getInformationStoresByType(
				InformationStoreHelper.CASSANDRA_INFORMATION_STORE).get(0);
		informationStore.put(resource, context);

	}


	// -------------------------------------------------------------------------------------

	public Resource get(String URI) {
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.CASSANDRA_INFORMATION_STORE)
				.get(0);

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, SearchRDFHelper.SEARCH_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);
		return informationStore.get(selector);

	}
	
	// -------------------------------------------------------------------------------------

		public void remove(String URI) {
			InformationStore informationStore = this.core
					.getInformationStoresByType(
							InformationStoreHelper.CASSANDRA_INFORMATION_STORE)
					.get(0);

			Selector selector = new Selector();
			selector.setProperty(SelectorHelper.TYPE, SearchRDFHelper.SEARCH_CLASS);
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
