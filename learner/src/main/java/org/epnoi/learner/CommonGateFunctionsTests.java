package org.epnoi.learner;

import gate.Document;
import gate.corpora.DocumentContentImpl;
import gate.util.InvalidOffsetException;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.model.modules.Core;
import org.epnoi.nlp.gate.NLPAnnotationsConstants;
import org.epnoi.uia.core.CoreUtility;

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

public void testSentence(){
    Document document=null;
    try {

        document = core.getNLPHandler().process("Autism is a neurodevelopmental disorder characterized by impaired social interaction, verbal and non-verbal communication, and restricted and repetitive behavior. Parents usually notice signs in the first two years of their child's life.");
    } catch (EpnoiResourceAccessException e) {
        e.printStackTrace();
    }


    gate.Annotation sentence = document.getAnnotations().get(NLPAnnotationsConstants.SENTENCE).iterator().next();
        System.out.println(sentence);
    //System.out.println(document);
    System.out.println("dc"+document.getContent());
    try {
        document.edit(sentence.getStartNode().getOffset(), sentence.getEndNode().getOffset(), new DocumentContentImpl(""));

    } catch (InvalidOffsetException e) {
        e.printStackTrace();
    }
    System.out.println("dc"+document.getContent());

/*
    try {
        document.edit(0L, startOffset, new DocumentContentImpl(""));

        document.edit(endOffset + 1, document.getAnnotations().lastNode().getOffset(), new DocumentContentImpl(""));
    } catch (InvalidOffsetException e) {
        e.printStackTrace();
    }
    */
}


    public void test(List<String> uris) {
/*

        SparkConf sparkConf = new SparkConf().setMaster("local[8]").setAppName(JOB_NAME);

        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        // First we must create the RDD with the URIs of the resources to be
        // included in the creation of the corpus
        JavaRDD<String> corpusURIs = sparkContext.parallelize(uris);

        System.out.println("init!!!!!");
        // THen we obtain the URIs of the annotated content documents that are
        // stored at the UIA
        JavaRDD<String> annotatedContentURIs = corpusURIs.flatMap(new UriToSectionsAnnotatedContentURIsFlatMapper());
        JavaRDD<Document> annotatedDocuments = annotatedContentURIs.flatMap(new UriToAnnotatedDocumentFlatMapper());


        JavaRDD<Sentence> annotatedDocumentsSentences = annotatedDocuments
                .flatMap(new DocumentToSentencesFlatMapper());


        Sentence sentence = annotatedDocumentsSentences.collect().get(0);
        Document document = sentence.getContainedAnnotations().getDocument();

        Long startOffset = sentence.getAnnotation().getStartNode().getOffset();
        Long endOffset = sentence.getAnnotation().getEndNode().getOffset();
        System.out.println("A______>>  "+document.getContent());
        System.out.println("S length > "+(endOffset-startOffset));
        System.out.println("lengafter"+document.getContent().size());
        Document newDocument = null;
        try {
            document.edit(0L, startOffset, new DocumentContentImpl(""));
            System.out.println("lengbefore>" + document.getContent().size());
            document.edit(endOffset -startOffset, document.getAnnotations().lastNode().getOffset(), new DocumentContentImpl(""));
        } catch (InvalidOffsetException e) {
            e.printStackTrace();
        }

        System.out.println("A______>>  "+document);



        for (Sentence sentence : annotatedDocumentsSentences.collect()) {
			System.out.println("-------> " + sentence);
		}


        JavaRDD<RelationalSentenceCandidate> relationalSentencesCandidates =
                annotatedDocumentsSentences .flatMap(new
                        SentenceToRelationalSentenceCandidateFlatMapper());
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
//relationSentencesCorpusCreator.testSentence();
    }

}

