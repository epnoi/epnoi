package org.epnoi.uia.informationaccess.wrapper;

import org.epnoi.model.Context;
import org.epnoi.model.Item;
import org.epnoi.model.Paper;
import org.epnoi.model.Resource;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreHelper;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.rdf.FeedRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;

public class PaperWrapper implements Wrapper {
	Core core;

	// -------------------------------------------------------------------------------------------------------------

	public PaperWrapper(Core core) {
		this.core = core;
	}

	// -------------------------------------------------------------------------------------------------------------

	public void put(Resource resource, Context context) {

		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);
		informationStore.put(resource, context);

		informationStore = this.core.getInformationStoresByType(
				InformationStoreHelper.SOLR_INFORMATION_STORE).get(0);
		informationStore.put(resource, context);

		informationStore = this.core.getInformationStoresByType(
				InformationStoreHelper.CASSANDRA_INFORMATION_STORE).get(0);
		informationStore.put(resource, context);

	}

	// -------------------------------------------------------------------------------------------------------------

	public Resource get(String URI) {
		Paper joinPaper = new Paper();
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, RDFHelper.PAPER_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);
		joinPaper = (Paper) informationStore.get(selector);

	//System.out.println("joinPaper RDF----> "+joinPaper);
		
		informationStore = this.core.getInformationStoresByType(
				InformationStoreHelper.CASSANDRA_INFORMATION_STORE).get(0);

		Paper cassandraItem = (Paper) informationStore.get(selector);

		//System.out.println("joinPaper CASSANDRA----> "+cassandraItem);
		
		joinPaper.setTitle(cassandraItem.getTitle());
		joinPaper.setAuthors(cassandraItem.getAuthors());
		joinPaper.setDescription(cassandraItem.getDescription());
		
		
		
		
		return joinPaper;
	}

	// -------------------------------------------------------------------------------------

	public void remove(String URI) {
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, RDFHelper.PAPER_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);
		informationStore.remove(selector);

		informationStore = this.core.getInformationStoresByType(
				InformationStoreHelper.CASSANDRA_INFORMATION_STORE).get(0);

		informationStore.remove(selector);

	}

	// -------------------------------------------------------------------------------------

	@Override
	public void update(Resource resource) {
		// TODO Auto-generated method stub

	}

	// -------------------------------------------------------------------------------------

}