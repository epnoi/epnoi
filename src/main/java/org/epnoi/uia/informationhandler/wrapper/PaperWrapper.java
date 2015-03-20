package org.epnoi.uia.informationhandler.wrapper;

import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.Paper;
import org.epnoi.model.Resource;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationstore.CassandraInformationStore;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreHelper;
import org.epnoi.uia.informationstore.MapInformationStore;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
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

		// System.out.println("joinPaper RDF----> "+joinPaper);

		informationStore = this.core.getInformationStoresByType(
				InformationStoreHelper.CASSANDRA_INFORMATION_STORE).get(0);

		Paper cassandraItem = (Paper) informationStore.get(selector);

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
		selector.setProperty(SelectorHelper.ANNOTATED_CONTENT_URI, URI + "/"
				+ AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE);
		exists = informationStore.exists(selector);
		if (exists) {

			informationStore = this.core.getInformationStoresByType(
					InformationStoreHelper.SOLR_INFORMATION_STORE).get(0);
			exists = informationStore.exists(selector);

			if (exists) {

				informationStore = this.core.getInformationStoresByType(
						InformationStoreHelper.MAP_INFORMATION_STORE).get(0);
				// exists = informationStore.exists(selector);

				if (exists) {

					/*
					 * System.out.println("------> " + selector);
					 * System.out.println("AC--------> " +
					 * ((CassandraInformationStore) informationStore)
					 * .getAnnotatedContent(selector));
					 */

					Content<Object> annotatedContent = (((MapInformationStore) informationStore)
							.getAnnotatedContent(selector));
					exists = annotatedContent != null
							&& !annotatedContent.isEmpty();
				}
			}
		}

		return exists;
	}

	// -------------------------------------------------------------------------------------

	@Override
	public Content<String> getContent(Selector selector) {
		// System.out.println("Entra y este es el selector "+selector);

		CassandraInformationStore informationStore = (CassandraInformationStore) this.core
				.getInformationStoresByType(
						InformationStoreHelper.CASSANDRA_INFORMATION_STORE)
				.get(0);

		return informationStore.getContent(selector);

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

	// -------------------------------------------------------------------------------------

}