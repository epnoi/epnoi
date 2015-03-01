package org.epnoi.uia.informationhandler.wrapper;

import java.util.Arrays;
import java.util.HashMap;

import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.model.WikipediaPage;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationstore.CassandraInformationStore;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreHelper;
import org.epnoi.uia.informationstore.MapInformationStore;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.cassandra.WikipediaPageCassandraDAO;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.parameterization.MapInformationStoreParameters;

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
				/*
				 * exists = !this.core .getInformationHandler()
				 * .getAnnotatedContent( URI, URI + "/first/" +
				 * AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE)
				 * .isEmpty();
				 */
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
	public Content<String> getAnnotatedContent(Selector selector) {

		MapInformationStore informationStore = (MapInformationStore) this.core
				.getInformationStoresByType(
						InformationStoreHelper.MAP_INFORMATION_STORE).get(0);

		return informationStore.getAnnotatedContent(selector);
	}

	// -------------------------------------------------------------------------------------

	@Override
	public void setAnnotatedContent(Selector selector,
			Content<String> annotatedContent) {
		MapInformationStore informationStore = (MapInformationStore) this.core
				.getInformationStoresByType(
						InformationStoreHelper.MAP_INFORMATION_STORE).get(0);
		informationStore.setAnnotatedContent(selector, annotatedContent);

	}

	// -------------------------------------------------------------------------------------
	
	public static void main(String[] args) {
		System.out.println("WikipediaPage Cassandra Test--------------");
		System.out
				.println("Initialization --------------------------------------------");
		WikipediaPageCassandraDAO wikipediaPageCassandraDAO = new WikipediaPageCassandraDAO();

		wikipediaPageCassandraDAO.init();

		System.out.println(" --------------------------------------------");

		WikipediaPage wikipediaPage = new WikipediaPage();
		wikipediaPage.setURI("http://externalresourceuri");
		wikipediaPage.setTerm("Proof Term");
		wikipediaPage.setTermDefinition("Proof Term is whatever bla bla bla");
		wikipediaPage.setSections(Arrays.asList("first", "middle section",
				"references"));
		wikipediaPage.setSectionsContent(new HashMap<String, String>());
		wikipediaPage.getSectionsContent().put("first",
				"This is the content of the first section");
		wikipediaPage.getSectionsContent().put("middle section",
				"This is the content of the middle section");
		wikipediaPage.getSectionsContent().put("references",
				"This is the content for the references");

		wikipediaPageCassandraDAO.create(wikipediaPage,
				Context.getEmptyContext());

		System.out
				.println("Reading the wikipedia page-------------------------------------------");
		System.out.println(" >> "
				+ wikipediaPageCassandraDAO.read("http://externalresourceuri"));

		WikipediaPage page = (WikipediaPage) wikipediaPageCassandraDAO
				.read("http://en.wikipedia.org/wiki/Glossary_of_American_football");

		System.out.println("page> " + page);

		for (String content : page.getSections()) {
			System.out
					.println("-----------------------------------------------------------------");
			System.out.println("---> " + content);
			System.out
					.println("---> " + page.getSectionsContent().get(content));
		}

	}


}