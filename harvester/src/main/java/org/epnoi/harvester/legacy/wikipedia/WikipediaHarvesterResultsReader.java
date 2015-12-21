package org.epnoi.harvester.legacy.wikipedia;

import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Content;
import org.epnoi.model.Selector;
import org.epnoi.model.WikipediaPage;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.modules.InformationStore;
import org.epnoi.model.modules.InformationStoreHelper;
import org.epnoi.model.parameterization.VirtuosoInformationStoreParameters;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.informationstore.SelectorHelper;

import java.util.List;
import java.util.logging.Logger;

public class WikipediaHarvesterResultsReader {
	private static final Logger logger = Logger
			.getLogger(WikipediaHarvesterResultsReader.class.getName());

	private Core core;


	// ----------------------------------------------------------------------------------------------------------------------

	public void init(Core core) throws EpnoiInitializationException {
		this.core = core;

	}

	// ----------------------------------------------------------------------------------------------------------------------

	public void createCorpus() {

		logger.info("Creating a relational sencences corpus with the following parameters:");

		// This should be done in parallel!!
		_searchWikipediaCorpus();

	}

	// ----------------------------------------------------------------------------------------------------------------------

	// ----------------------------------------------------------------------------------------------------------------------

	private void _searchWikipediaCorpus() {
		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE,
				RDFHelper.WIKIPEDIA_PAGE_CLASS);
		// String uri = "http://en.wikipedia.org/wiki/AccessibleComputing";
		int nullCounts = 1;
		int notNullCounts = 1;
		int count = 1;
		List<String> wikipediaPages = getWikipediaArticles();
		System.out.println(wikipediaPages.size()
				+ " wikipedia pages were retrieved");
		for (String uri : wikipediaPages) {

			if (this.core.getInformationHandler().contains(uri,
					RDFHelper.WIKIPEDIA_PAGE_CLASS)) {

				// System.out.println(count++ + " Retrieving " + uri);
				logger.info("Analyzing " + count++ + "> " + uri);
				WikipediaPage wikipediaPage = (WikipediaPage) this.core
						.getInformationHandler().get(uri,
								RDFHelper.WIKIPEDIA_PAGE_CLASS);

				//selector.setProperty(SelectorHelper.URI, uri);
				for (String section : wikipediaPage.getSections()) {
					String extractedURI = _extractURI(uri, section,
							AnnotatedContentHelper.CONTENT_TYPE_OBJECT_XML_GATE);
					selector.setProperty(SelectorHelper.ANNOTATED_CONTENT_URI,
							extractedURI);
					// System.out.println("selector >" + selector);
					Content<Object> annotatedContent = this.core
							.getInformationHandler().getAnnotatedContent(
									selector);

					if (annotatedContent != null) {
						
					//	 Document annotatedDocument = (Document)
					//	  annotatedContent .getContent();
						  
					//	 System.out.println(extractedURI + " ------------> " +
						// annotatedDocument.getAnnotations());
						
					//	System.out.println("este es not null---> "+annotatedDocument.toXml());
						notNullCounts++;
					} else {
						/*
						 * System.out.println("The section " + section + " of "
						 * + uri + " was null");
						 */
						nullCounts++;
					}
				}

			}

		}
		logger.info("The number of nulls is " + nullCounts
				+ " the number of not nulls is " + notNullCounts);
	}

	// ----------------------------------------------------------------------------------------------------------------------

	public List<String> getWikipediaArticles() {
		logger.info("Retrieving the URIs of the Wikipedia articles ");

		InformationStore informationStore = this.core.getInformationHandler()
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		String queryExpression = "SELECT DISTINCT  ?uri FROM <{GRAPH}>"
				+ " { ?uri a <{WIKIPEDIA_PAPER_CLASS}> " + "}";

		queryExpression = queryExpression.replace(
				"{GRAPH}",
				((VirtuosoInformationStoreParameters) informationStore
						.getParameters()).getGraph()).replace(
				"{WIKIPEDIA_PAPER_CLASS}", RDFHelper.WIKIPEDIA_PAGE_CLASS);

		List<String> queryResults = informationStore.query(queryExpression);

		logger.info("The number of retrived Wikipeda articles are "
				+ queryResults.size());
		System.exit(0);
		return queryResults;
	}

	// ----------------------------------------------------------------------------------------------------------------------

	private String _extractURI(String URI, String section, String annotationType) {

		String cleanedSection = section.replaceAll("\\s+$", "").replaceAll(
				"\\s+", "_");

		return URI + "/" + cleanedSection + "/" + annotationType;
	}

	// ----------------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		logger.info("Starting the Relation Sentences Corpus Creator");

		WikipediaHarvesterResultsReader relationSentencesCorpusCreator = new WikipediaHarvesterResultsReader();

		Core core = CoreUtility.getUIACore();

		try {
			relationSentencesCorpusCreator.init(core);
		} catch (EpnoiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		relationSentencesCorpusCreator.createCorpus();

		logger.info("Stopping the Relation Sentences Corpus Creator");
	}

	// ----------------------------------------------------------------------------------------------------------------------

}
