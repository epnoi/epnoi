package org.epnoi.uia.harvester.wikipedia;

import gate.Document;

import java.util.List;
import java.util.logging.Logger;

import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Content;
import org.epnoi.model.WikipediaPage;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.exceptions.EpnoiInitializationException;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreHelper;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.learner.nlp.TermCandidatesFinder;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;

public class WikipediaHarvesterResultsReader {
	private static final Logger logger = Logger
			.getLogger(WikipediaHarvesterResultsReader.class.getName());

	private Core core;
	private TermCandidatesFinder termCandidatesFinder;

	// ----------------------------------------------------------------------------------------------------------------------

	public void init(Core core) throws EpnoiInitializationException {
		this.core = core;

		this.termCandidatesFinder = new TermCandidatesFinder();
		this.termCandidatesFinder.init();

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

				selector.setProperty(SelectorHelper.URI, uri);
				for (String section : wikipediaPage.getSections()) {
					String extractedURI = _extractURI(uri, section,
							AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE);
					selector.setProperty(SelectorHelper.ANNOTATED_CONTENT_URI,
							extractedURI);
					// System.out.println("selector >" + selector);
					Content<Object> annotatedContent = this.core
							.getInformationHandler().getAnnotatedContent(
									selector);

					if (annotatedContent != null) {
						Document annotatedDocument = (Document) annotatedContent
								.getContent();
						System.out.println(extractedURI + " ------------> "
								+ annotatedDocument.getAnnotations());

					} else {
						System.out.println("The section " + section + " of "
								+ uri + " was null");
						nullCounts++;
					}
				}

			}

		}
		logger.info("The number of nulls is " + nullCounts);
	}

	// ----------------------------------------------------------------------------------------------------------------------

	public List<String> getWikipediaArticles() {
		logger.info("Retrieving the URIs of the Wikipedia articles ");

		InformationStore informationStore = this.core
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
