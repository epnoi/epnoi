package org.epnoi.informationhandler.wrappers;

import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.model.Selector;
import org.epnoi.model.modules.Core;
import org.epnoi.model.modules.InformationStore;
import org.epnoi.model.modules.InformationStoreHelper;
import org.epnoi.model.rdf.SearchRDFHelper;
import org.epnoi.uia.informationstore.SelectorHelper;

import java.util.logging.Logger;


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

		InformationStore informationStore = this.core.getInformationHandler()
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);
		informationStore.put(resource, context);
		informationStore = this.core.getInformationHandler().getInformationStoresByType(
				InformationStoreHelper.CASSANDRA_INFORMATION_STORE).get(0);
		informationStore.put(resource, context);

	}


	// -------------------------------------------------------------------------------------

	public Resource get(String URI) {
		InformationStore informationStore = this.core.getInformationHandler()
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
			InformationStore informationStore = this.core.getInformationHandler()
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

		@Override
		public boolean exists(String URI) {
			// TODO Auto-generated method stub
			return false;
		}

		// -------------------------------------------------------------------------------------
		
		@Override
		public Content<String> getContent(Selector selector) {
			// TODO Auto-generated method stub
			return null;
		}

		// -------------------------------------------------------------------------------------
		
		@Override
		public void setContent(Selector selector, Content<String> content) {
			// TODO Auto-generated method stub
			
		}
		
		// -------------------------------------------------------------------------------------

		@Override
		public Content<Object> getAnnotatedContent(Selector selector) {
			// TODO Auto-generated method stub
			return null;
		}
		
		// -------------------------------------------------------------------------------------

		@Override
		public void setAnnotatedContent(Selector selector,
				Content<Object> annotatedContent) {
			// TODO Auto-generated method stub
			
		}

		// -------------------------------------------------------------------------------------

		

}
