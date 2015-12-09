package org.epnoi.learner.relations.corpus.parallel;

import gate.Document;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.epnoi.learner.relations.corpus.RelationalSentencesCorpusCreationParameters;
import org.epnoi.learner.relations.extractor.parallel.RelationalSentenceCandidateToRelationalSentenceMapper;
import org.epnoi.model.Context;
import org.epnoi.model.RelationalSentence;
import org.epnoi.model.RelationalSentencesCorpus;
import org.epnoi.model.Selector;
import org.epnoi.model.commons.Parameters;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Component
public class RelationalSentencesCorpusCreator {
    private static final Logger logger = Logger.getLogger(RelationalSentencesCorpusCreator.class.getName());
    @Autowired
    private Core core;
    @Autowired
    private RelationalSentencesCorpusCreationParameters parameters;

    private Parameters<Object> runtimeParameters;

    private RelationalSentencesCorpus corpus;

    private boolean storeResult;
    private boolean verbose;

    private int MAX_SENTENCE_LENGTH;


    private static final String JOB_NAME = "RELATIONAL_SENTENCES_CORPUS_CREATION";

    // ----------------------------------------------------------------------------------------------------------------------


    @PostConstruct
    public void init()
            throws EpnoiInitializationException {
        logger.info("Initializing the RelationalSentencesCorpusCreator with the following parameters "
                + parameters.toString());

        this.corpus = new RelationalSentencesCorpus();

        this.storeResult = (boolean) parameters.getParameterValue(RelationalSentencesCorpusCreationParameters.STORE);

        this.verbose = (boolean) parameters.getParameterValue(RelationalSentencesCorpusCreationParameters.VERBOSE);
        this.MAX_SENTENCE_LENGTH = (int) parameters
                .getParameterValue(RelationalSentencesCorpusCreationParameters.MAX_SENTENCE_LENGTH_PARAMETER);

    }

    // ----------------------------------------------------------------------------------------------------------------------

    public void createCorpus(Parameters<Object> runtimeParameters) {
        this.runtimeParameters = runtimeParameters;
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


        SparkConf sparkConf = new SparkConf().setMaster("local[12]").setAppName(JOB_NAME);

        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);


        Broadcast<RelationalSentencesCorpusCreationParameters> parametersBroadcast = sparkContext.broadcast((RelationalSentencesCorpusCreationParameters) this.parameters);

        // First we must create the RDD with the URIs of the resources to be
        // included in the creation of the corpus
        JavaRDD<String> corpusURIs = sparkContext.parallelize(URIs);

        System.out.println("init!!!!!");
        // THen we obtain the URIs of the annotated content documents that are
        // stored at the UIA

        JavaRDD<String> annotatedContentURIs = corpusURIs.flatMap(uri -> {
            UriToSectionsAnnotatedContentURIsFlatMapper mapper = new UriToSectionsAnnotatedContentURIsFlatMapper(parametersBroadcast.getValue());
            return mapper.call(uri);
        });

        System.out.println("..> " + annotatedContentURIs.collect());

        JavaRDD<Document> annotatedDocuments = annotatedContentURIs.flatMap(uri -> {
            UriToAnnotatedDocumentFlatMapper flatMapper = new UriToAnnotatedDocumentFlatMapper(parametersBroadcast.getValue());
            return flatMapper.call(uri);
        });


        JavaRDD<Sentence> annotatedDocumentsSentences = annotatedDocuments
                .flatMap(new DocumentToSentencesFlatMapper());
    /*
        for (Sentence sentence : annotatedDocumentsSentences.collect()) {
			System.out.println("-------> " + sentence);
		}
*/

        JavaRDD<RelationalSentenceCandidate> relationalSentencesCandidates =
                annotatedDocumentsSentences.flatMap(new
                        SentenceToRelationalSentenceCandidateFlatMapper());


        //relationalSentencesCandidates.collect();
        /*
          JavaRDD<RelationalSentence> relationalSentences =
		  relationalSentencesCandidates.map(new
		  RelationalSentenceMapFunction());
*/
        JavaRDD<RelationalSentence> relationalSentences =
                relationalSentencesCandidates.map(new
                        RelationalSentenceCandidateToRelationalSentenceMapper());


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
/*WHAT SHOULD BE
        List<String> wikipediaPages = WikipediaPagesRetriever.getWikipediaArticles(core);

        return wikipediaPages;
*/
        return Arrays.asList("http://en.wikipedia.org/wiki/Autism");
    }

    // ----------------------------------------------------------------------------------------------------------------------

/*
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

        parameters.setParameter(RelationalSentencesCorpusCreationParameters.UIA_PATH, "http://localhost:8080/epnoi/rest");

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
        */
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

//        relationSentencesCorpusCreator.createCorpus();
/*
		System.out.println("Checking if the Relational Sentence Corpus can be retrieved");

		RelationalSentencesCorpus relationalSentenceCorpus = (RelationalSentencesCorpus) core.getInformationHandler()
				.get(relationalCorpusURI, RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS);
		System.out.println("The readed relational sentences corpus " + relationalSentenceCorpus);
		logger.info("Stopping the Relation Sentences Corpus Creator");
	*/
    //   }

}
