package org.epnoi.uia.informationhandler.wrapper;

import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreHelper;
import org.epnoi.uia.informationstore.Selector;

public class FeedWrapper implements Wrapper {
	Core core;

	// -------------------------------------------------------------------------------------------------------------

	public FeedWrapper(Core core) {
		this.core = core;
	}

	// -------------------------------------------------------------------------------------------------------------
	public void put(Resource resource, Context context) {
		/*
		System.out.println("Entra con estos valores ");
		System.out.println("R " + resource);
		System.out.println("C " + context);
		*/
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);
		// System.out.println("--------------------------------------------->  "+informationStore);
		informationStore.put(resource, context);
		informationStore = this.core.getInformationStoresByType(
				InformationStoreHelper.SOLR_INFORMATION_STORE).get(0);
		/*
		System.out
				.println("]------------------------------------------------------------"
						+ informationStore);
*/
		informationStore.put(resource, context);
		
		//-----------------
		
		informationStore = this.core.getInformationStoresByType(
				InformationStoreHelper.CASSANDRA_INFORMATION_STORE).get(0);
		/*
		System.out
				.println("]------------------------------------------------------------"
						+ informationStore);
						*/
		informationStore.put(resource, context);

	}

	// ------------------------------------------------------------------------------------

	// -------------------------------------------------------------------------------------------------------------

	public Resource get(String URI) {
		// TODO Auto-generated method stub
		return null;
	}

	// -------------------------------------------------------------------------------------

	public void remove(String URI) {
		// TODO Auto-generated method stub

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