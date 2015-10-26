package org.epnoi.learner;

import gate.Document;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.epnoi.learner.relations.corpus.RelationalSentencesCorpusCreationParameters;
import org.epnoi.learner.relations.corpus.parallel.*;
import org.epnoi.model.*;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.commons.WikipediaPagesRetriever;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.informationstore.SelectorHelper;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


public class CommonGateFunctionsTests {
    private static final Logger logger = Logger.getLogger(CommonGateFunctionsTests.class.getName());

    private Core core;


    private int MAX_SENTENCE_LENGTH = 4;


    private static final String JOB_NAME = "GATE_FUNCTIONS_TESTS";

    // ----------------------------------------------------------------------------------------------------------------------

    public void init(Core core)
            throws EpnoiInitializationException {
        logger.info("Initializing the RelationalSentencesCorpusCreator with the following parameters");
        this.core = core;

    }

    // ----------------------------------------------------------------------------------------------------------------------


    public void test(List<String> uris) {


        SparkConf sparkConf = new SparkConf().setMaster("local[8]").setAppName(JOB_NAME);

        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        // First we must create the RDD with the URIs of the resources to be
        // included in the creation of the corpus
        JavaRDD<String> corpusURIs = sparkContext.parallelize(uris);

        System.out.println("init!!!!!");
        // THen we obtain the URIs of the annotated content documents that are
        // stored at the UIA
        JavaRDD<String> annotatedContentURIs = corpusURIs.flatMap(new SectionsAnnotatedContentURIsFlatMapFunction());
        JavaRDD<Document> annotatedDocuments = annotatedContentURIs.flatMap(new DocumentRetrievalFlatMapFunction());



        JavaRDD<Sentence> annotatedDocumentsSentences = annotatedDocuments
                .flatMap(new DocumentToSentencesFlatMapFunction());

        System.out.println(annotatedDocumentsSentences.collect().get(0));
	/*
		for (Sentence sentence : annotatedDocumentsSentences.collect()) {
			System.out.println("-------> " + sentence);
		}


        JavaRDD<RelationalSentenceCandidate> relationalSentencesCandidates =
                annotatedDocumentsSentences .flatMap(new
                        RelationalSentenceCandidateFlatMapFunction());
*/
        //relationalSentencesCandidates.collect();

        /*
        JavaRDD<RelationalSentence> relationalSentences =
                relationalSentencesCandidates.map(new
                        RelationalSentenceMapFunction());
*/
        //System.out.println("------>"+relationalSentences.collect());

     //   return relationalSentences.collect();
    }
    // ----------------------------------------------------------------------------------------------------------------------


    public static void main(String[] args) {
        logger.info("Starting the Relation Sentences Corpus Creator");

        CommonGateFunctionsTests relationSentencesCorpusCreator = new CommonGateFunctionsTests();

        Core core = CoreUtility.getUIACore();



        try {
            relationSentencesCorpusCreator.init(core);
        } catch (EpnoiInitializationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(-1);
        }


        relationSentencesCorpusCreator.test(Arrays.asList("http://en.wikipedia.org/wiki/Autism"));

    }

}

