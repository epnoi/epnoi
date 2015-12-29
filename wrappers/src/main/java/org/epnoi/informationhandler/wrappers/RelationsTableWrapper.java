package org.epnoi.informationhandler.wrappers;

import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.model.Selector;
import org.epnoi.model.modules.Core;
import org.epnoi.model.modules.InformationStore;
import org.epnoi.model.modules.InformationStoreHelper;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.informationstore.SelectorHelper;

public class RelationsTableWrapper implements Wrapper {
	Core core;

	// -------------------------------------------------------------------------------------------------------------

	public RelationsTableWrapper(Core core) {
		this.core = core;
	}

	// -------------------------------------------------------------------------------------------------------------

	public void put(Resource resource, Context context) {

		InformationStore informationStore = this.core.getInformationHandler()
				.getInformationStoresByType(
						InformationStoreHelper.CASSANDRA_INFORMATION_STORE)
				.get(0);
		informationStore.put(resource, context);

	}

	// -------------------------------------------------------------------------------------------------------------

	public Resource get(String URI) {
		System.out.println("table uri "+URI);
		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, RDFHelper.RELATIONS_TABLE_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);

		InformationStore informationStore = this.core.getInformationHandler()
				.getInformationStoresByType(
						InformationStoreHelper.CASSANDRA_INFORMATION_STORE)
				.get(0);

		Resource resource =informationStore.get(selector);
		System.out.println("the resource was "+selector+ " and returned "+ resource);
	return resource;
	}

	// -------------------------------------------------------------------------------------

	public void remove(String URI) {

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, RDFHelper.RELATIONS_TABLE_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);

		InformationStore informationStore = this.core.getInformationHandler()
				.getInformationStoresByType(
						InformationStoreHelper.CASSANDRA_INFORMATION_STORE)
				.get(0);

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

		InformationStore informationStore = this.core.getInformationHandler()
				.getInformationStoresByType(
						InformationStoreHelper.CASSANDRA_INFORMATION_STORE)
				.get(0);
		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, RDFHelper.RELATIONS_TABLE_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);
		return informationStore.exists(selector);

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
