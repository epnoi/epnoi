package org.epnoi.uia.informationhandler.wrapper;

import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.Item;
import org.epnoi.model.Paper;
import org.epnoi.model.Resource;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationstore.CassandraInformationStore;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreHelper;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.rdf.FeedRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;

import com.hp.hpl.jena.vocabulary.RDF;

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

		// System.out.println("joinPaper RDF----> "+joinPaper);

		informationStore = this.core.getInformationStoresByType(
				InformationStoreHelper.CASSANDRA_INFORMATION_STORE).get(0);

		Paper cassandraItem = (Paper) informationStore.get(selector);

		System.out.println("joinPaper CASSANDRA----> " + cassandraItem);

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

	@Override
	public boolean exists(String URI) {
		boolean exists;
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, RDFHelper.PAPER_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);
		exists = informationStore.exists(selector);
		if (exists) {

			informationStore = this.core.getInformationStoresByType(
					InformationStoreHelper.SOLR_INFORMATION_STORE).get(0);
			exists = informationStore.exists(selector);

			if (exists) {

				informationStore = this.core.getInformationStoresByType(
						InformationStoreHelper.CASSANDRA_INFORMATION_STORE)
						.get(0);
				// exists = informationStore.exists(selector);

				if (exists) {
					exists = /*
							 * (!((CassandraInformationStore) informationStore)
							 * .getContent(selector).isEmpty()) &&
							 */(!((CassandraInformationStore) informationStore)
							.getAnnotatedContent(selector).isEmpty());
				}
			}
		}

		return exists;
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
	public Content<String> getAnnotatedContent(Selector selector) {
		// TODO Auto-generated method stub
		return null;
	}

	// -------------------------------------------------------------------------------------
	
	@Override
	public void setAnnotatedContent(Selector selector,
			Content<String> annotatedContent) {
		// TODO Auto-generated method stub
		
	}

	// -------------------------------------------------------------------------------------
	
	

}