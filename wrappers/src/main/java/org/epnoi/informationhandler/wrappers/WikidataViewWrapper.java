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

public class WikidataViewWrapper implements Wrapper {
	private Core core;

	WikidataViewWrapper(Core core) {
		this.core = core;
	}

	// ------------------------------------------------------------------------

	@Override
	public void put(Resource resource, Context context) {
		InformationStore informationStore = this.core.getInformationHandler()
				.getInformationStoresByType(
						InformationStoreHelper.CASSANDRA_INFORMATION_STORE)
				.get(0);
		informationStore.put(resource, context);
		
		informationStore = this.core.getInformationHandler()
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE)
				.get(0);
		informationStore.put(resource, context);
	}

	// ------------------------------------------------------------------------

	@Override
	public void remove(String URI) {
		InformationStore informationStore = this.core.getInformationHandler()
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, RDFHelper.WIKIDATA_VIEW_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);
		informationStore.remove(selector);

		informationStore = this.core.getInformationHandler().getInformationStoresByType(
				InformationStoreHelper.CASSANDRA_INFORMATION_STORE).get(0);

		informationStore.remove(selector);
	}

	// ------------------------------------------------------------------------

	@Override
	public void update(Resource resource) {
		// TODO Auto-generated method stub

	}

	// ------------------------------------------------------------------------

	@Override
	public Resource get(String URI) {
		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, RDFHelper.WIKIDATA_VIEW_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);
		
		InformationStore informationStore = this.core.getInformationHandler()
				.getInformationStoresByType(
						InformationStoreHelper.CASSANDRA_INFORMATION_STORE)
				.get(0);

		Resource cassandraItem = informationStore.get(selector);

		return cassandraItem;
	}

	// ------------------------------------------------------------------------

	@Override
	public boolean exists(String URI) {
		// TODO Auto-generated method stub
		return false;
	}

	// ------------------------------------------------------------------------

	@Override
	public Content<String> getContent(Selector selector) {
		// TODO Auto-generated method stub
		return null;
	}

	// ------------------------------------------------------------------------

	@Override
	public void setContent(Selector selector, Content<String> content) {
		// TODO Auto-generated method stub

	}

	// ------------------------------------------------------------------------

	@Override
	public Content<Object> getAnnotatedContent(Selector selector) {
		// TODO Auto-generated method stub
		return null;
	}

	// ------------------------------------------------------------------------

	@Override
	public void setAnnotatedContent(Selector selector,
			Content<Object> annotatedContent) {
		// TODO Auto-generated method stub

	}

	// ------------------------------------------------------------------------

}
