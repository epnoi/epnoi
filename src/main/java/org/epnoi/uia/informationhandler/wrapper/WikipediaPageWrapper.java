package org.epnoi.uia.informationhandler.wrapper;

import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.model.WikipediaPage;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreHelper;
import org.epnoi.uia.informationstore.MapInformationStore;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;

public class WikipediaPageWrapper implements Wrapper {
	Core core;

	// -------------------------------------------------------------------------------------------------------------

	public WikipediaPageWrapper(Core core) {
		this.core = core;
	}

	// -------------------------------------------------------------------------------------------------------------

	public void put(Resource resource, Context context) {

		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);
		informationStore.put(resource, context);
		/*
		 * informationStore = this.core.getInformationStoresByType(
		 * InformationStoreHelper.SOLR_INFORMATION_STORE).get(0);
		 * informationStore.put(resource, context);
		 */
		informationStore = this.core.getInformationStoresByType(
				InformationStoreHelper.CASSANDRA_INFORMATION_STORE).get(0);
		informationStore.put(resource, context);

	}

	// -------------------------------------------------------------------------------------------------------------

	public Resource get(String URI) {

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE,
				RDFHelper.WIKIPEDIA_PAGE_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);

		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.CASSANDRA_INFORMATION_STORE)
				.get(0);

		WikipediaPage cassandraWikipediaPage = (WikipediaPage) informationStore
				.get(selector);

		return cassandraWikipediaPage;
	}

	// -------------------------------------------------------------------------------------

	public void remove(String URI) {
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE,
				RDFHelper.WIKIPEDIA_PAGE_CLASS);
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

		// System.out.println(" checking the existence > " + URI);

		boolean exists;
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE,
				RDFHelper.WIKIPEDIA_PAGE_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);
		exists = informationStore.exists(selector);

		if (exists) {
			informationStore = this.core.getInformationStoresByType(
					InformationStoreHelper.CASSANDRA_INFORMATION_STORE).get(0);
			exists = informationStore.exists(selector);

			if (exists) {

				String annotatedContentURI = URI + "/first/"
						+ AnnotatedContentHelper.CONTENT_TYPE_OBJECT_XML_GATE;

				selector.setProperty(SelectorHelper.ANNOTATED_CONTENT_URI,
						annotatedContentURI);
				exists = this.core.getInformationHandler().getAnnotatedContent(
						selector) != null;
	
			}
			
		}

		selector = null;
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
	public Content<Object> getAnnotatedContent(Selector selector) {

		MapInformationStore informationStore = (MapInformationStore) this.core
				.getInformationStoresByType(
						InformationStoreHelper.MAP_INFORMATION_STORE).get(0);

		return informationStore.getAnnotatedContent(selector);
	}

	// -------------------------------------------------------------------------------------

	@Override
	public void setAnnotatedContent(Selector selector,
			Content<Object> annotatedContent) {
		MapInformationStore informationStore = (MapInformationStore) this.core
				.getInformationStoresByType(
						InformationStoreHelper.MAP_INFORMATION_STORE).get(0);
		informationStore.setAnnotatedContent(selector, annotatedContent);

	}

}