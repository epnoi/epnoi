package org.epnoi.uia.informationhandler.wrapper;

import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.model.Term;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreHelper;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;

public class TermWrapper implements Wrapper {
	Core core;

	// -------------------------------------------------------------------------------------------------------------

	public TermWrapper(Core core) {
		this.core = core;
	}

	// -------------------------------------------------------------------------------------------------------------

	public void put(Resource resource, Context context) {

		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.CASSANDRA_INFORMATION_STORE)
				.get(0);
		informationStore.put(resource, context);
		
		informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE)
				.get(0);
		informationStore.put(resource, context);

	}

	// -------------------------------------------------------------------------------------------------------------

	public Resource get(String URI) {

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, RDFHelper.TERM_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);

		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.CASSANDRA_INFORMATION_STORE)
				.get(0);

		Term cassandraItem = (Term) informationStore.get(selector);

		return cassandraItem;
	}

	// -------------------------------------------------------------------------------------

	public void remove(String URI) {
		

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, RDFHelper.TERM_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);
	

		InformationStore informationStore = this.core.getInformationStoresByType(
				InformationStoreHelper.CASSANDRA_INFORMATION_STORE).get(0);

		informationStore.remove(selector);
		
		informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE)
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
	
		InformationStore informationStore = this.core.getInformationStoresByType(
				InformationStoreHelper.CASSANDRA_INFORMATION_STORE)
				.get(0);
		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, RDFHelper.TERM_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);
		return informationStore.exists(selector);

	}

	// -------------------------------------------------------------------------------------

}