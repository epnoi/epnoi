package org.epnoi.learner.relations.corpus.parallel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.epnoi.learner.relations.corpus.RelationalSentencesCorpusCreationParameters;
import org.epnoi.model.Context;
import org.epnoi.model.KnowledgeBase;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.RelationalSentence;
import org.epnoi.model.RelationalSentencesCorpus;
import org.epnoi.model.Selector;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.commons.WikipediaPagesRetriever;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.informationstore.SelectorHelper;

import gate.Document;

public class RelationalSentencesCorpusCreator {
	private static final Logger logger = Logger.getLogger(RelationalSentencesCorpusCreator.class.getName());

	private Core core;
	private RelationalSentencesCorpus corpus;
	private RelationalSentencesCorpusCreationParameters parameters;
	private boolean storeResult;
	private boolean verbose;

	private int MAX_SENTENCE_LENGTH;


	private static final String JOB_NAME = "RELATIONAL_SENTENCES_CORPUS";

	// ----------------------------------------------------------------------------------------------------------------------

	public void init(Core core, RelationalSentencesCorpusCreationParameters parameters)
			throws EpnoiInitializationException {
		logger.info("Initializing the RelationalSentencesCorpusCreator with the following parameters "
				+ parameters.toString());
		this.core = core;
		this.parameters = parameters;
		this.corpus = new RelationalSentencesCorpus();

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
		List<String> URIs = _collectCorpusURIs();

		corpus.setUri((String) this.parameters.getParameterValue(
				RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER));
		corpus.setDescription((String) this.parameters.getParameterValue(
				RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_DESCRIPTION_PARAMETER));
		corpus.setType((String) this.parameters.getParameterValue(
				RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_TYPE_PARAMETER));

		corpus.setSentences(_findRelationalSentences(URIs));

		if (this.verbose) {
			RelationalSentencesCorpusViewer.showRelationalSentenceCorpusInfo(corpus);
		}

		if (this.storeResult) {
			_storeCorpus();
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------

	private List<RelationalSentence> _findRelationalSentences(List<String> URIs) {
		

		SparkConf sparkConf = new SparkConf().setMaster("local[8]").setAppName(JOB_NAME);

		JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

		// First we must create the RDD with the URIs of the resources to be
		// included in the creation of the corpus
		JavaRDD<String> corpusURIs = sparkContext.parallelize(URIs);

		System.out.println("init!!!!!");
		// THen we obtain the URIs of the annotated content documents that are
		// stored at the UIA

		JavaRDD<String> annotatedContentURIs = corpusURIs.flatMap(new SectionsAnnotatedContentURIsFlatMapFunction());

		System.out.println("..> " + annotatedContentURIs.collect());

		JavaRDD<Document> annotatedDocuments = annotatedContentURIs.flatMap(new DocumentRetrievalFlatMapFunction());

		

		JavaRDD<Sentence> annotatedDocumentsSentences = annotatedDocuments
				.flatMap(new DocumentToSentencesFlatMapFunction());
	/*	
		for (Sentence sentence : annotatedDocumentsSentences.collect()) {
			System.out.println("-------> " + sentence);
		}
*/
		
		 JavaRDD<RelationalSentenceCandidate> relationalSentencesCandidates =
		  annotatedDocumentsSentences .flatMap(new
		 RelationalSentenceCandidateFlatMapFunction());
		 
		 //relationalSentencesCandidates.collect();
		
		  JavaRDD<RelationalSentence> relationalSentences =
		  relationalSentencesCandidates.map(new
		  RelationalSentenceMapFunction());
		  
		 //System.out.println("------>"+relationalSentences.collect());
		 
		return relationalSentences.collect();
	}

	private void _storeCorpus() {
		core.getInformationHandler().remove(this.corpus.getUri(), RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS);
		core.getInformationHandler().put(this.corpus, Context.getEmptyContext());
	}

	// ----------------------------------------------------------------------------------------------------------------------

	private List<String> _collectCorpusURIs() {
		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, RDFHelper.WIKIPEDIA_PAGE_CLASS);
		// String uri = "http://en.wikipedia.org/wiki/AccessibleComputing";

		// logger.info("Retrieving the URIs of the Wikipedia articles ");

		List<String> wikipediaPages = WikipediaPagesRetriever.getWikipediaArticles(core);

		return wikipediaPages;

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
/*
		System.out.println("Checking if the Relational Sentence Corpus can be retrieved");

		RelationalSentencesCorpus relationalSentenceCorpus = (RelationalSentencesCorpus) core.getInformationHandler()
				.get(relationalCorpusURI, RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS);
		System.out.println("The readed relational sentences corpus " + relationalSentenceCorpus);
		logger.info("Stopping the Relation Sentences Corpus Creator");
	*/
	}

}