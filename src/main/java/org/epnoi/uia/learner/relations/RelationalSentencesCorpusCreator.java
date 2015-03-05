package org.epnoi.uia.learner.relations;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.DocumentContent;
import gate.Factory;
import gate.util.InvalidOffsetException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Content;
import org.epnoi.model.Context;
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
import org.epnoi.uia.learner.nlp.TermCandidatesFinder;
import org.epnoi.uia.learner.nlp.gate.NLPAnnotationsHelper;
import org.epnoi.uia.learner.nlp.wordnet.WordNetParameters;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;

public class RelationalSentencesCorpusCreator {
	private static final Logger logger = Logger
			.getLogger(RelationalSentencesCorpusCreator.class.getName());
	
	private Core core;
	private TermCandidatesFinder termCandidatesFinder;
	private RelationalSentencesCorpus corpus;
	private CuratedRelationsTable curatedRelationsTable;
	RelationalSentencesCorpusCreationParameters parameters;
	private boolean storeResult;
	private boolean verbose;
	private int MAX_SENTENCE_LENGTH = 600;

	// ----------------------------------------------------------------------------------------------------------------------

	public void init(Core core,
			RelationalSentencesCorpusCreationParameters parameters)
			throws EpnoiInitializationException {
		this.core = core;
		this.parameters = parameters;
		this.corpus = new RelationalSentencesCorpus();
		this.termCandidatesFinder = new TermCandidatesFinder();
		this.termCandidatesFinder.init();
		WordNetParameters wordNetParameters = (WordNetParameters) parameters
				.getParameterValue(RelationalSentencesCorpusCreationParameters.WORDNET_PARAMETERS);

		CuratedRelationsTableCreator curatedRelationsTableCreator = new CuratedRelationsTableCreator();
		curatedRelationsTableCreator.init(wordNetParameters);
		this.curatedRelationsTable = curatedRelationsTableCreator.build();

		this.storeResult = (boolean) parameters
				.getParameterValue(RelationalSentencesCorpusCreationParameters.STORE_RESULT_PARAMETER);

		this.verbose = (boolean) parameters
				.getParameterValue(RelationalSentencesCorpusCreationParameters.VERBOSE_PARAMETER);

	}

	// ----------------------------------------------------------------------------------------------------------------------

	public void createCorpus() {

		logger.info("Creating a relational sencences corpus with the following parameters:");
		logger.info(this.parameters.toString());
		// This should be done in parallel!!
		_searchWikipediaCorpus();
		_searchReutersCorpus();

		corpus.setURI((String) this.parameters
				.getParameterValue(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER));
		corpus.setDescription((String) this.parameters
				.getParameterValue(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_DESCRIPTION_PARAMETER));
		corpus.setType((String) this.parameters
				.getParameterValue(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_TYPE_PARAMETER));

		if (this.verbose) {
			_showRelationalSentenceCorpusInfo();
		}

		if (this.storeResult) {
			core.getInformationHandler().remove(this.corpus.getURI(),
					RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS);
			core.getInformationHandler().put(this.corpus,
					Context.getEmptyContext());
		}

	}

	// ----------------------------------------------------------------------------------------------------------------------

	private void _showRelationalSentenceCorpusInfo() {
		System.out
				.println("------------------------------------------------------------------------------------------");
		System.out.println("Information about the corpus "
				+ this.corpus.getURI());
		System.out.println("Relations type: " + this.corpus.getType());
		System.out.println("Corpus description: "
				+ this.corpus.getDescription());
		System.out.println("It has " + this.corpus.getSentences().size()
				+ " relational sentences");
		/*
		 * for (RelationalSentence relationalSencente :
		 * this.corpus.getSentences()) {
		 * _showRelationalSentenceInfo(relationalSencente); }
		 */
		double average = 0.;
		for (RelationalSentence relationalSencente : this.corpus.getSentences()) {
			average += relationalSencente.getSentence().length();
		}
		System.out.println("The average length is " + average
				/ this.corpus.getSentences().size());

		System.out
				.println("------------------------------------------------------------------------------------------");

	}

	// ----------------------------------------------------------------------------------------------------------------------

	private void _showRelationalSentenceInfo(
			RelationalSentence relationalSencente) {

		String source = relationalSencente
				.getSentence()
				.subSequence(
						relationalSencente.getSource().getStart().intValue(),
						relationalSencente.getSource().getEnd().intValue())
				.toString();

		String target = relationalSencente
				.getSentence()
				.subSequence(
						relationalSencente.getTarget().getStart().intValue(),
						relationalSencente.getTarget().getEnd().intValue())
				.toString();

		System.out.println("[" + source + "," + target + "]>"
				+ relationalSencente.getSentence());

	}

	private void _searchReutersCorpus() {
		// TODO Auto-generated method stub

	}

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
					selector.setProperty(
							SelectorHelper.ANNOTATED_CONTENT_URI,
							_extractURI(
									uri,
									section,
									AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE));
					// System.out.println("selector >" + selector);
					Content<String> annotatedContent = this.core
							.getInformationHandler().getAnnotatedContent(
									selector);

					if (annotatedContent != null) {
						/*
						 * System.out.println("NOT NULL The section " + section
						 * + " of " + uri + " was not null");
						 */
						Document annotatedContentDocument = GateUtils
								.deserializeGATEDocument(annotatedContent
										.getContent());
						_searchDocument(annotatedContentDocument);
						Factory.deleteResource(annotatedContentDocument);

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

	public boolean _isValid(Annotation sentenceAnnotation) {
		System.out.println("--------------------------> "
				+ sentenceAnnotation.getFeatures());
		if (sentenceAnnotation.getFeatures() != null
				&& sentenceAnnotation.getFeatures().get("string") != null
				&& sentenceAnnotation.getFeatures().get("string").toString()
						.length() < MAX_SENTENCE_LENGTH) {
			System.out.println("--------------------------> "
					+ sentenceAnnotation.getFeatures().get("string").toString()
							.length());
		}

		return (sentenceAnnotation.getFeatures() != null
				&& sentenceAnnotation.getFeatures().get("string") != null && sentenceAnnotation
				.getFeatures().get("string").toString().length() < MAX_SENTENCE_LENGTH);

	}

	// ----------------------------------------------------------------------------------------------------------------------

	public void _searchDocument(Document document) {

		AnnotationSet sentenceAnnotations = document.getAnnotations().get(
				"Sentence");
		DocumentContent sentenceContent = null;
		AnnotationSet sentencesAnnotations = document.getAnnotations();
		Iterator<Annotation> sentencesIt = sentenceAnnotations.iterator();
		while (sentencesIt.hasNext()) {
			Annotation sentenceAnnotation = sentencesIt.next();

			try {
				Long sentenceStartOffset = sentenceAnnotation.getStartNode()
						.getOffset();
				Long sentenceEndOffset = sentenceAnnotation.getEndNode()
						.getOffset();

				sentenceContent = document.getContent().getContent(
						sentenceStartOffset, sentenceEndOffset);

				if (sentenceContent.size() < MAX_SENTENCE_LENGTH) {

					_testSentence(sentenceStartOffset, sentenceContent,
							sentencesAnnotations.getContained(
									sentenceStartOffset, sentenceEndOffset));
				}
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
		// This table stores the string representation of each sentence terms
		// and their corresponding annotation
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
			// For each term we retrieve its well-known hypernyms
			Set<String> termHypernyms = this.curatedRelationsTable
					.getHypernyms(term);
			termHypernyms.retainAll(sentenceTerms);

			// If the intersection of the well-known hypernyms and the terms
			// that belong to the sencence, this is a relational sentence
			if (termHypernyms.size() > 0) {

				Annotation sourceTermAnnotation = termsAnnotationsTable
						.get(term);

				// Note that the offset is relative to the beginning of the
				// sentence
				OffsetRangeSelector source = new OffsetRangeSelector(
						sourceTermAnnotation.getStartNode().getOffset()
								- sentenceStartOffset, sourceTermAnnotation
								.getEndNode().getOffset() - sentenceStartOffset);
				// For each target term a relational sentence is created
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

					Document annotatedContent = termCandidatesFinder
							.findTermCandidates(sentenceContent.toString());

					RelationalSentence relationalSentence = new RelationalSentence(
							source, target, sentenceContent.toString(),
							annotatedContent.toXml());

					corpus.getSentences().add(relationalSentence);
				}

			}
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

	// ----------------------------------------------------------------------------------------------------------------------

	private String _extractURI(String URI, String section, String annotationType) {

		String cleanedSection = section.replaceAll("\\s+$", "").replaceAll(
				"\\s+", "_");

		return URI + "/" + cleanedSection + "/" + annotationType;
	}

	// ----------------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		logger.info("Starting the Relation Sentences Corpus Creator");

		RelationalSentencesCorpusCreator relationSentencesCorpusCreator = new RelationalSentencesCorpusCreator();

		Core core = CoreUtility.getUIACore();

		RelationalSentencesCorpusCreationParameters parameters = new RelationalSentencesCorpusCreationParameters();

		WordNetParameters wordnetParameters = new WordNetParameters();
		String filepath = "/epnoi/epnoideployment/wordnet/dictWN3.1/";
		String relationalCorpusURI = "http://drInventorFirstReview/relationalSentencesCorpus";
		wordnetParameters.setParameter(WordNetParameters.DICTIONARY_LOCATION,
				filepath);

		parameters.setParameter(
				RelationalSentencesCorpusCreationParameters.WORDNET_PARAMETERS,
				wordnetParameters);

		parameters
				.setParameter(
						RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER,
						relationalCorpusURI);

		parameters
				.setParameter(
						RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_TYPE_PARAMETER,
						RelationHelper.HYPERNYM);

		parameters
				.setParameter(
						RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_DESCRIPTION_PARAMETER,
						"DrInventor first review relational sentences corpus");

		parameters
				.setParameter(
						RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER,
						relationalCorpusURI);
		
		
		parameters
		.setParameter(
				RelationalSentencesCorpusCreationParameters.MAX_SENTENCE_LENGTH_PARAMETER,
				1000);

		parameters
				.setParameter(
						RelationalSentencesCorpusCreationParameters.STORE_RESULT_PARAMETER,
						true);

		parameters.setParameter(
				RelationalSentencesCorpusCreationParameters.VERBOSE_PARAMETER,
				true);

		try {
			relationSentencesCorpusCreator.init(core, parameters);
		} catch (EpnoiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		/*
		 * RelationalSentencesCorpus testRelationalSentenceCorpus =
		 * relationSentencesCorpusCreator .createTestCorpus();
		 * 
		 * System.out.println("testCorpus>" + testRelationalSentenceCorpus);
		 * 
		 * core.getInformationHandler().put(testRelationalSentenceCorpus,
		 * Context.getEmptyContext());
		 * 
		 * System.out.println(core.getInformationHandler().get(
		 * testRelationalSentenceCorpus.getURI()));
		 * 
		 * System.exit(0);
		 */

		relationSentencesCorpusCreator.createCorpus();

		System.out
				.println("Checking if the Relational Sentence Corpus can be retrieved");

		RelationalSentencesCorpus relationalSentenceCorpus = (RelationalSentencesCorpus) core
				.getInformationHandler().get(relationalCorpusURI,
						RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS);
		System.out.println("The readed relational sentences corpus "
				+ relationalSentenceCorpus);
		logger.info("Stopping the Relation Sentences Corpus Creator");
	}

	// ----------------------------------------------------------------------------------------------------------------------

	public RelationalSentencesCorpus createTestCorpus() {
		String relationalSentenceURI = "http://thetestcorpus/drinventor";
		RelationalSentencesCorpus relationalSentencesCorpus = new RelationalSentencesCorpus();
		relationalSentencesCorpus.setDescription("The test corpus");
		relationalSentencesCorpus.setURI(relationalSentenceURI);
		// relationalSentencesCorpus.setType(RelationHelper.HYPERNYM);

		Document annotatedContentA = termCandidatesFinder
				.findTermCandidates("A dog is a canine");
		RelationalSentence relationalSentenceA = new RelationalSentence(
				new OffsetRangeSelector(2L, 5L), new OffsetRangeSelector(11L,
						17L), "A dog is a canine", annotatedContentA.toXml());

		Document annotatedContentB = termCandidatesFinder
				.findTermCandidates("A dog, is a canine (and other things!)");

		RelationalSentence relationalSentenceB = new RelationalSentence(
				new OffsetRangeSelector(2L, 5L), new OffsetRangeSelector(12L,
						18L), "A dog, is a canine (and other things!)",
				annotatedContentB.toXml());

		relationalSentencesCorpus.getSentences().add(relationalSentenceA);

		relationalSentencesCorpus.getSentences().add(relationalSentenceB);
		return relationalSentencesCorpus;
	}

}
