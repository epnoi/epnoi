package org.epnoi.informationhandler.wrappers;

import org.epnoi.model.*;
import org.epnoi.model.modules.Core;
import org.epnoi.model.modules.InformationStore;
import org.epnoi.model.modules.InformationStoreHelper;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.informationstore.CassandraInformationStore;
import org.epnoi.uia.informationstore.MapInformationStore;
import org.epnoi.uia.informationstore.SelectorHelper;

public class PaperWrapper implements Wrapper {
	Core core;

	// -------------------------------------------------------------------------------------------------------------

	public PaperWrapper(Core core) {
		this.core = core;
	}

	// -------------------------------------------------------------------------------------------------------------

	public void put(Resource resource, Context context) {

		InformationStore informationStore = this.core.getInformationHandler()
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);
		informationStore.put(resource, context);

		informationStore = this.core.getInformationHandler().getInformationStoresByType(
				InformationStoreHelper.SOLR_INFORMATION_STORE).get(0);
		informationStore.put(resource, context);

		informationStore = this.core.getInformationHandler().getInformationStoresByType(
				InformationStoreHelper.CASSANDRA_INFORMATION_STORE).get(0);
		informationStore.put(resource, context);

	}

	// -------------------------------------------------------------------------------------------------------------

	public Resource get(String URI) {
		Paper joinPaper = new Paper();
		InformationStore informationStore = this.core
				.getInformationHandler().getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, RDFHelper.PAPER_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);
		joinPaper = (Paper) informationStore.get(selector);

		// System.out.println("joinPaper RDF----> "+joinPaper);

		informationStore = this.core.getInformationHandler().getInformationStoresByType(
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
				.getInformationHandler().getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, RDFHelper.PAPER_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);
		informationStore.remove(selector);

		informationStore = this.core.getInformationHandler().getInformationStoresByType(
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
				.getInformationHandler().getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, RDFHelper.PAPER_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);
		selector.setProperty(SelectorHelper.ANNOTATED_CONTENT_URI, URI + "/"
				+ AnnotatedContentHelper.CONTENT_TYPE_OBJECT_XML_GATE);
		exists = informationStore.exists(selector);
		if (exists) {
/*
			informationStore = this.core.getInformationStoresByType(
					InformationStoreHelper.SOLR_INFORMATION_STORE).get(0);
			exists = informationStore.exists(selector);
*/
			if (exists) {

				informationStore = this.core.getInformationHandler().getInformationStoresByType(
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

		CassandraInformationStore informationStore = (CassandraInformationStore) this.core.getInformationHandler()
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
		MapInformationStore informationStore = (MapInformationStore) this.core.getInformationHandler()
				.getInformationStoresByType(
						InformationStoreHelper.MAP_INFORMATION_STORE).get(0);
		return informationStore.getAnnotatedContent(selector);
	}

	// -------------------------------------------------------------------------------------

	@Override
	public void setAnnotatedContent(Selector selector,
			Content<Object> annotatedContent) {
		MapInformationStore informationStore = (MapInformationStore) this.core.getInformationHandler()
				.getInformationStoresByType(
						InformationStoreHelper.MAP_INFORMATION_STORE).get(0);
		informationStore.setAnnotatedContent(selector, annotatedContent);

	}

	// -------------------------------------------------------------------------------------
	
	public static void main(String[] args) {
		
		/* 
		 
		 FOR_TEST 
		 
		Core core = CoreUtility.getUIACore();
		
		Paper domain = new Paper();
		domain.setTitle("title");
		domain.setDescription("Mi mama me mima, yo mimo a mi mama");
		
		String uRI = "http://www.epnoi.org/paperconlauri";
		domain.setURI(uRI);
		core.getInformationHandler().put(domain, Context.getEmptyContext());

		System.out.println("-------> "
				+ core.getInformationHandler().get(
						uRI, RDFHelper.PAPER_CLASS));

		if (core.getInformationHandler().contains(domain.getURI(),
				RDFHelper.PAPER_CLASS)) {
			System.out.println("The paper exists!");
		} else {
			System.out.println("It doesn't exist, something went wrong :(");
		}

		core.getInformationHandler().remove(domain);

		if (!core.getInformationHandler().contains(domain.getURI(),
				RDFHelper.PAPER_CLASS)) {
			System.out.println("The domain doesn't exist!");
		} else {
			System.out.println("It exists, something went wrong :(");
		}
*/
	}


}