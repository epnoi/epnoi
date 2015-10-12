package org.epnoi.learner.relations.corpus.parallel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.epnoi.learner.relations.corpus.RelationalSentencesCorpusCreationParameters;
import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.KnowledgeBase;
import org.epnoi.model.OffsetRangeSelector;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.RelationalSentence;
import org.epnoi.model.RelationalSentencesCorpus;
import org.epnoi.model.Selector;
import org.epnoi.model.WikipediaPage;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.nlp.gate.NLPAnnotationsConstants;
import org.epnoi.uia.commons.GateUtils;
import org.epnoi.uia.commons.WikipediaPagesRetriever;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.informationstore.SelectorHelper;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.DocumentContent;

public class RelationalSentencesCorpusCreator {
	private static final Logger logger = Logger.getLogger(RelationalSentencesCorpusCreator.class.getName());

	private Core core;
	private RelationalSentencesCorpus corpus;
	private KnowledgeBase knowledgeBase;
	private RelationalSentencesCorpusCreationParameters parameters;
	private boolean storeResult;
	private boolean verbose;
	private int MAX_SENTENCE_LENGTH = 100;
	private long nonRelationalSentencesCounter = 0;

	// ----------------------------------------------------------------------------------------------------------------------

	public void init(Core core, RelationalSentencesCorpusCreationParameters parameters)
			throws EpnoiInitializationException {
		logger.info("Initializing the RelationalSentencesCorpusCreator with the following parameters "
				+ parameters.toString());
		this.core = core;
		this.parameters = parameters;
		this.corpus = new RelationalSentencesCorpus();

		try {
			this.knowledgeBase = core.getKnowledgeBaseHandler().getKnowledgeBase();
		} catch (EpnoiResourceAccessException e) {
			throw new EpnoiInitializationException(e.getMessage());
		}

		this.storeResult = (boolean) parameters.getParameterValue(RelationalSentencesCorpusCreationParameters.STORE);

		this.verbose = (boolean) parameters.getParameterValue(RelationalSentencesCorpusCreationParameters.VERBOSE);
		this.MAX_SENTENCE_LENGTH = (int) parameters
				.getParameterValue(RelationalSentencesCorpusCreationParameters.MAX_SENTENCE_LENGTH_PARAMETER);

	}

	// ----------------------------------------------------------------------------------------------------------------------

	public void createCorpus() {

		logger.info("Creating a relational sencences corpus with the following parameters:");
		logger.info(this.parameters.toString());
		// This should be done in parallel!!
		_searchWikipediaCorpus();
		

		corpus.setURI((String) this.parameters.getParameterValue(
				RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER));
		corpus.setDescription((String) this.parameters.getParameterValue(
				RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_DESCRIPTION_PARAMETER));
		corpus.setType((String) this.parameters.getParameterValue(
				RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_TYPE_PARAMETER));

		if (this.verbose) {
			RelationalSentencesCorpusViewer.showRelationalSentenceCorpusInfo(corpus);
		}

		if (this.storeResult) {
			_storeCorpus();
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------

	private void _storeCorpus() {
		core.getInformationHandler().remove(this.corpus.getURI(), RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS);
		core.getInformationHandler().put(this.corpus, Context.getEmptyContext());
	}

	

	// ----------------------------------------------------------------------------------------------------------------------

	private void _searchWikipediaCorpus() {
		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, RDFHelper.WIKIPEDIA_PAGE_CLASS);
		// String uri = "http://en.wikipedia.org/wiki/AccessibleComputing";
		int nullCounts = 1;
		int sectionsCount = 1;
		int count = 1;

		// logger.info("Retrieving the URIs of the Wikipedia articles ");

		List<String> wikipediaPages = WikipediaPagesRetriever.getWikipediaArticles(core);
	

	}

	// ----------------------------------------------------------------------------------------------------------------------

	private Map<String, Annotation> _createTermsAnnotationsTable(DocumentContent sentenceContent,
			AnnotationSet sentenceAnnotationsSet, Long sentenceStartOffset, Set<String> sentenceTerms) {
		HashMap<String, Annotation> termsAnnotationsTable = new HashMap<String, Annotation>();
		for (Annotation termAnnotation : sentenceAnnotationsSet.get(NLPAnnotationsConstants.TERM_CANDIDATE)) {
			Long startOffset = termAnnotation.getStartNode().getOffset() - sentenceStartOffset;
			Long endOffset = termAnnotation.getEndNode().getOffset() - sentenceStartOffset;

			String term = "";
			try {
				// First of all we retrieve the surface form of the term

				term = sentenceContent.getContent(startOffset, endOffset).toString();

			} catch (Exception e) {
				term = "";

			}

			// We stem the surface form (we left open the possibility of
			// different stemming results so we consider a set of stemmed
			// forms)
			_addTermToTermsTable(sentenceTerms, termsAnnotationsTable, termAnnotation, term);
		}
		return termsAnnotationsTable;
	}

	// ----------------------------------------------------------------------------------------------------------------------

	private void _addTermToTermsTable(Set<String> sentenceTerms, HashMap<String, Annotation> termsAnnotationsTable,
			Annotation termAnnotation, String term) {
		if (term.length() > 2) {
			for (String stemmedTerm : this.knowledgeBase.stem(term)) {

				termsAnnotationsTable.put(stemmedTerm, termAnnotation);
				sentenceTerms.add(stemmedTerm);

			}

			termsAnnotationsTable.put(term, termAnnotation);
			sentenceTerms.add(term);
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------

	// ----------------------------------------------------------------------------------------------------------------------

	private String _extractURI(String URI, String section, String annotationType) {

		String cleanedSection = section.replaceAll("\\s+$", "").replaceAll("\\s+", "_");

		return URI + "/" + cleanedSection + "/" + annotationType;
	}

	// ----------------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		logger.info("Starting the Relation Sentences Corpus Creator");

		RelationalSentencesCorpusCreator relationSentencesCorpusCreator = new RelationalSentencesCorpusCreator();

		Core core = CoreUtility.getUIACore();

		RelationalSentencesCorpusCreationParameters parameters = new RelationalSentencesCorpusCreationParameters();

		String relationalCorpusURI = "http://epnoi.org/relationalSentencesCorpus";

		parameters.setParameter(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER,
				relationalCorpusURI);

		parameters.setParameter(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_TYPE_PARAMETER,
				RelationHelper.HYPERNYM);

		parameters.setParameter(
				RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_DESCRIPTION_PARAMETER,
				"DrInventor first review relational sentences corpus");

		parameters.setParameter(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER,
				relationalCorpusURI);

		parameters.setParameter(RelationalSentencesCorpusCreationParameters.MAX_SENTENCE_LENGTH_PARAMETER, 80);

		parameters.setParameter(RelationalSentencesCorpusCreationParameters.STORE, false);

		parameters.setParameter(RelationalSentencesCorpusCreationParameters.VERBOSE, true);

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

		System.out.println("Checking if the Relational Sentence Corpus can be retrieved");

		RelationalSentencesCorpus relationalSentenceCorpus = (RelationalSentencesCorpus) core.getInformationHandler()
				.get(relationalCorpusURI, RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS);
		System.out.println("The readed relational sentences corpus " + relationalSentenceCorpus);
		logger.info("Stopping the Relation Sentences Corpus Creator");
	}

	

}
