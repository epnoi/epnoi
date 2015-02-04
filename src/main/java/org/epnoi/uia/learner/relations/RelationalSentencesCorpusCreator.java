package org.epnoi.uia.learner.relations;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.DocumentContent;
import gate.Factory;
import gate.util.InvalidOffsetException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Content;
import org.epnoi.model.OffsetRangeSelector;
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
import org.epnoi.uia.learner.nlp.gate.NLPAnnotationsHelper;
import org.epnoi.uia.learner.nlp.wordnet.WordNetParameters;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;

public class RelationalSentencesCorpusCreator {
	private static final Logger logger = Logger
			.getLogger(RelationalSentencesCorpusCreator.class.getName());
	private Core core;

	private RelationalSentencesCorpus corpus;
	private CuratedRelationsTable curatedRelationsTable;

	// ----------------------------------------------------------------------------------------------------------------------

	public void init(Core core,
			RelationalSentencesCorpusCreationParameters parameters)
			throws EpnoiInitializationException {
		this.core = core;
		this.corpus = new RelationalSentencesCorpus();

		WordNetParameters wordNetParameters = (WordNetParameters) parameters
				.getParameterValue(RelationalSentencesCorpusCreationParameters.WORDNET_PARAMETERS);

		CuratedRelationsTableCreator curatedRelationsTableCreator = new CuratedRelationsTableCreator();
		curatedRelationsTableCreator.init(wordNetParameters);
		this.curatedRelationsTable = curatedRelationsTableCreator.build();

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

			System.out.println(count++ + " Retrieving " + uri);

			WikipediaPage wikipediaPage = (WikipediaPage) this.core
					.getInformationHandler().get(uri,
							RDFHelper.WIKIPEDIA_PAGE_CLASS);
			// System.out.println(">" + wikipediaPage);

			selector.setProperty(SelectorHelper.URI, uri);
			// System.out.println("sections> " + wikipediaPage.getSections());
			for (String section : wikipediaPage.getSections()) {
				selector.setProperty(
						SelectorHelper.ANNOTATED_CONTENT_URI,
						_extractURI(
								uri,
								section,
								AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE));
				// System.out.println("selector >" + selector);
				Content<String> annotatedContent = this.core
						.getInformationHandler().getAnnotatedContent(selector);

				if (annotatedContent != null) {
					/*
					 * System.out.println("NOT NULL The section " + section +
					 * " of " + uri + " was not null");
					 */
					Document annotatedContentDocument = GateUtils
							.deserializeGATEDocument(annotatedContent
									.getContent());
					_searchDocument(annotatedContentDocument);
					Factory.deleteResource(annotatedContentDocument);

				} else {
					System.out.println("The section " + section + " of " + uri
							+ " was null");
					nullCounts++;
				}
			}

		}

		System.out.println("The number of nulls is " + nullCounts);

	}

	// ----------------------------------------------------------------------

	public void _searchDocument(Document document) {

		AnnotationSet sentenceAnnotations = document.getAnnotations().get(
				"Sentence");
		DocumentContent sentenceContent = null;
		Iterator<Annotation> sentencesIt = sentenceAnnotations.iterator();
		while (sentencesIt.hasNext()) {
			Annotation sentenceAnnotation = sentencesIt.next();

			try {
				Long startOffset = sentenceAnnotation.getStartNode()
						.getOffset();
				Long endOffset = sentenceAnnotation.getEndNode().getOffset();

				sentenceContent = document.getContent().getContent(startOffset,
						endOffset);

				AnnotationSet sentencesAnnotations = document.getAnnotations();
				_testSentence(startOffset, sentenceContent,
						sentencesAnnotations.getContained(startOffset,
								endOffset));
			} catch (InvalidOffsetException e) {

				e.printStackTrace();
			}

		}

	}

	// ----------------------------------------------------------------------------------------------------------------------

	private void _testSentence(Long sentenceStartOffset,
			DocumentContent sentenceContent,
			AnnotationSet sentenceAnnotationsSet) {
		Set<String> sentenceTerms = new java.util.HashSet<String>();
		HashMap<String, Annotation> termsAnnotationsTable = new HashMap<>();

		for (Annotation termAnnotation : sentenceAnnotationsSet
				.get(NLPAnnotationsHelper.TERM_CANDIDATE)) {
			Long startOffset = termAnnotation.getStartNode().getOffset()
					- sentenceStartOffset;
			Long endOffset = termAnnotation.getEndNode().getOffset()
					- sentenceStartOffset;

			/*
			 * System.out.println("startOffset >" + startOffset + "| endOffset "
			 * + endOffset + ")");
			 */
			try {
				String term = sentenceContent
						.getContent(startOffset, endOffset).toString();

				String stemmedTerm = this.curatedRelationsTable.stemTerm(term);

				if (stemmedTerm == null) {
					// System.out.println("--------------------Z EL QUE DA NULL ERA > "+termCandiateContet);
				} else {
					termsAnnotationsTable.put(stemmedTerm, termAnnotation);
					sentenceTerms.add(stemmedTerm);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		for (String term : sentenceTerms) {
			Set<String> termHypernyms = this.curatedRelationsTable
					.getHypernyms(term);
			termHypernyms.retainAll(sentenceTerms);
			if (termHypernyms.size() > 0) {
				/*
				 * System.out .println(termCandidate +
				 * "==============================================================================>>> "
				 * + termCandidateHypernyms);
				 */
				Annotation sourceTermAnnotation = termsAnnotationsTable
						.get(term);

				// Note that the offset is relative to the beginning of the
				// sentence
				OffsetRangeSelector source = new OffsetRangeSelector(
						sourceTermAnnotation.getStartNode().getOffset()
								- sentenceStartOffset, sourceTermAnnotation
								.getEndNode().getOffset() - sentenceStartOffset);

				for (String destinationTerm : termHypernyms) {

					Annotation destinationTermAnnotation = termsAnnotationsTable
							.get(destinationTerm);

					// Note that the offset is relative to the beginning of the
					// sentence
					OffsetRangeSelector target = new OffsetRangeSelector(
							destinationTermAnnotation.getStartNode()
									.getOffset() - sentenceStartOffset,
							destinationTermAnnotation.getEndNode().getOffset()
									- sentenceStartOffset);

					RelationalSentence relationalSentence = new RelationalSentence(
							source, target, sentenceContent.toString());

					corpus.getSentences().add(relationalSentence);
				}

			}
		}

		// System.out.println("The set was " + termCandidates);
	}

	// ----------------------------------------------------------------------------------------------------------------------

	public List<String> getWikipediaArticles() {
		logger.info("Retrieving the URIs of the Wikipedia articles ");
		List<String> wikipediaArticlesURI = new ArrayList<>();
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

		for (String uri : queryResults) {
			if (this.core.getInformationHandler().contains(uri,
					RDFHelper.WIKIPEDIA_PAGE_CLASS)) {
				wikipediaArticlesURI.add(uri);
			}

		}

		logger.info("The number of retrived Wikipeda articles are "
				+ queryResults.size());
		return wikipediaArticlesURI;
	}

	// ----------------------------------------------------------------------------------------------------------------------

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

		RelationalSentencesCorpusCreationParameters parameters = new RelationalSentencesCorpusCreationParameters();

		WordNetParameters wordnetParameters = new WordNetParameters();
		String filepath = "/epnoi/epnoideployment/wordnet/dictWN3.1/";
		wordnetParameters.setParameter(WordNetParameters.DICTIONARY_LOCATION,
				filepath);

		parameters.setParameter(
				RelationalSentencesCorpusCreationParameters.WORDNET_PARAMETERS,
				wordnetParameters);

		try {
			relationSentencesCorpusCreator.init(core, parameters);
		} catch (EpnoiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}

		RelationalSentencesCorpus relationalSentencesCorpus = relationSentencesCorpusCreator
				.createCorpus();

		System.out.println("The result is " + relationalSentencesCorpus);

		System.out.println("Stopping the Relation Sentences Corpus Creator");
	}

}
