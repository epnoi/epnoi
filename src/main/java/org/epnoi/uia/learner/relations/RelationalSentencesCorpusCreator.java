package org.epnoi.uia.learner.relations;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.util.InvalidOffsetException;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Content;
import org.epnoi.model.WikipediaPage;
import org.epnoi.uia.commons.GateUtils;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.exceptions.EpnoiInitializationException;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreHelper;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;

public class RelationalSentencesCorpusCreator {
	private static final Logger logger = Logger
			.getLogger(RelationalSentencesCorpusCreator.class.getName());
	private Core core;

	private RelationalSentencesCorpus corpus;

	// ----------------------------------------------------------------------------------------------------------------------

	public void init(Core core,
			RelationSentencesCorpusCreationParameters parameters)
			throws EpnoiInitializationException {
		this.core = core;
		this.corpus = new RelationalSentencesCorpus();
	}

	// ----------------------------------------------------------------------------------------------------------------------

	public RelationalSentencesCorpus createCorpus() {
		// This should be done in parallel!!
		_searchWikipediaCorpus();
		searchReutersCorpus();

		return this.corpus;

	}

	// ----------------------------------------------------------------------

	private void searchReutersCorpus() {
		// TODO Auto-generated method stub

	}

	// ----------------------------------------------------------------------

	private void _searchWikipediaCorpus() {
		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE,
				RDFHelper.WIKIPEDIA_PAGE_CLASS);
		// String uri = "http://en.wikipedia.org/wiki/AccessibleComputing";
		int nullCounts = 1;
		int count = 1;
		
		for (String uri : getWikipediaArticles()) {
		if(!this.core.getInformationHandler().contains(uri, RDFHelper.WIKIPEDIA_PAGE_CLASS)){
			System.out.println("Este medio esta medio no esta "+uri);
		}		
		
		
		}
		System.exit(0);
		/*
		for (String uri : getWikipediaArticles()) {

			
			
			System.out.println(count++ + " Retrieving " + uri);
			System.out.println(this.core.getInformationHandler().contains(uri, RDFHelper.WIKIPEDIA_PAGE_CLASS));
			WikipediaPage wikipediaPage = (WikipediaPage) this.core
					.getInformationHandler().get(uri,
							RDFHelper.WIKIPEDIA_PAGE_CLASS);
			System.out.println(">" + wikipediaPage);

			selector.setProperty(SelectorHelper.URI, uri);
			System.out.println("sections> " + wikipediaPage.getSections());
			for (String section : wikipediaPage.getSections()) {
				selector.setProperty(
						SelectorHelper.ANNOTATED_CONTENT_URI,
						_extractURI(
								uri,
								section,
								AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE));
				System.out.println("selector >" + selector);
				Content<String> annotatedContent = this.core
						.getInformationHandler().getAnnotatedContent(selector);

				if (annotatedContent != null) {
					System.out.println("NOT NULL The section " + section
							+ " of " + uri + " was not null");
					
					  Document annotatedContentDocument = GateUtils
					  .deserializeGATEDocument(annotatedContent .getContent());
					  _processDocument(annotatedContentDocument);
					 
				} else {
					System.out.println("The section " + section + " of " + uri
							+ " was null");
					nullCounts++;
				}
			}

		}

		System.out.println("The number of nulls is " + nullCounts);
*/
	}

	// ----------------------------------------------------------------------

	public void _processDocument(Document document) {

		AnnotationSet sentenceAnnotations = document.getAnnotations().get(
				"Sentence");

		Iterator<Annotation> sentencesIt = sentenceAnnotations.iterator();
		while (sentencesIt.hasNext()) {
			Annotation sentenceAnnotation = sentencesIt.next();
			String sentenceContent = "";
			try {
				sentenceContent = document
						.getContent()
						.getContent(
								sentenceAnnotation.getStartNode().getOffset(),
								sentenceAnnotation.getEndNode().getOffset())
						.toString();
			} catch (InvalidOffsetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println("Sentence content:> " + sentenceContent);
		}

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

	private String _extractURI(String URI, String section, String annotationType) {

		String cleanedSection = section.replaceAll("\\s+$", "").replaceAll(
				"\\s+", "_");

		return URI + "/" + cleanedSection + "/" + annotationType;
	}

	// ----------------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		System.out.println("Starting the Relation Sentences Corpus Creator");

		RelationalSentencesCorpusCreator relationSentencesCorpusCreator = new RelationalSentencesCorpusCreator();

		Core core = CoreUtility.getUIACore();

		RelationSentencesCorpusCreationParameters parameters = new RelationSentencesCorpusCreationParameters();
		try {
			relationSentencesCorpusCreator.init(core, parameters);
		} catch (EpnoiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}

		RelationalSentencesCorpus corpus = relationSentencesCorpusCreator
				.createCorpus();

		System.out.println("The result is " + corpus);

		System.out.println("Stopping the Relation Sentences Corpus Creator");
	}

}
