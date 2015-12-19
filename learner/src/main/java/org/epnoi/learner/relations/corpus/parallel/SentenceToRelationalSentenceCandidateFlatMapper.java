package org.epnoi.learner.relations.corpus.parallel;



import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import gate.Annotation;
import gate.AnnotationSet;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.epnoi.learner.relations.corpus.RelationalSentencesCorpusCreationParameters;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.clients.thrift.KnowledgeBaseServiceClient;
import org.epnoi.model.commons.Parameters;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.nlp.gate.NLPAnnotationsConstants;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.*;


public class SentenceToRelationalSentenceCandidateFlatMapper
        implements FlatMapFunction<Sentence, RelationalSentenceCandidate> {

    private Parameters parameters;

    private final int MIN_TERM_LENGTH = 2;
    private Map<String, List<String>> stemmingTable = new HashMap<>();
    private Map<String, List<String>> hypernymsTable = new HashMap<>();
    private KnowledgeBaseServiceClient knowledgeBaseServiceClient;


    SentenceToRelationalSentenceCandidateFlatMapper(Parameters parameters){
        this.parameters=parameters;
    }

    @Override
    public Iterable<RelationalSentenceCandidate> call(Sentence currentSentence) throws Exception {
        if (_hasEnoughCandidates(currentSentence)) {
_initKnowledgeBaseServiceClient();
            Iterable<RelationalSentenceCandidate> relationalSentenceCandidates=_testSentence(currentSentence);
            _closeKnowledgeBaseServiceClient();
            return relationalSentenceCandidates;
        } else {
            return new ArrayList<>();
        }

    }

    private void _closeKnowledgeBaseServiceClient() {
        knowledgeBaseServiceClient.close();
    }

    private void _initKnowledgeBaseServiceClient() {
        knowledgeBaseServiceClient= new KnowledgeBaseServiceClient();
        Integer thriftPort = (Integer)parameters.getParameterValue(RelationalSentencesCorpusCreationParameters.THRIFT_PORT);
        try {
            knowledgeBaseServiceClient.init("localhost", thriftPort);
        } catch (EpnoiInitializationException e) {
            e.printStackTrace();
        }
    }

    private boolean _hasEnoughCandidates(Sentence sentence) {

        return sentence.getContainedAnnotations().get(NLPAnnotationsConstants.TERM_CANDIDATE).size() > 2;
    }


    private List<RelationalSentenceCandidate> _testSentence(Sentence sentence) {
        List<RelationalSentenceCandidate> relationalSentencesCandidates = new ArrayList<>();
        Long sentenceStartOffset = sentence.getAnnotation().getStartNode().getOffset();
        Long sentenceEndOffset = sentence.getAnnotation().getEndNode().getOffset();

        Set<String> sentenceTerms = _initSentenceTerms(sentence);

        // This table stores the string representation of each sentence terms
        // and their corresponding annotation
        Map<String, Annotation> termsAnnotationsTable = _initTermsAnnotationsTable(sentence);
        // System.out.println("ternsAbb"+termsAnnotationsTable);
        Iterator<String> termsIt = sentenceTerms.iterator();
        boolean found = false;
        while (termsIt.hasNext()) {
            String term = termsIt.next();
            if (term != null && term.length() > MIN_TERM_LENGTH) {
                // For each term we retrieve its well-known hypernyms

                List<String> termHypernyms = _hypernyms(term);
                /*
				 * System.out.println("term> "+ term); System.out.println(
				 * "term hypernyms> "+termHypernyms); System.out.println(
				 * "sentence terms> "+sentenceTerms); System.out.println(
				 * "Sentence content "+sentence.getContent());
				 */
                termHypernyms.retainAll(sentenceTerms);
                // termHypernyms.removeAll(this.knowledgeBase.stem(term));

                // If the intersection of the well-known hypernyms and the terms
                // that belong to the sentence, this is a relational sentence
                if (termHypernyms.size() > 0) {

                    System.out.println("FOUND SENTENCE BETWEEN " + sentenceStartOffset + "," + sentenceEndOffset
                            + " when testing for the term " + sentenceTerms);
                    System.out.println(">> " + sentence.getContent());

                    List<RelationalSentenceCandidate> candidates = _createRelationalSentence(sentence,
                            termsAnnotationsTable, term, termHypernyms);

                    relationalSentencesCandidates.addAll(candidates);

                }
            }
        }
        return relationalSentencesCandidates;
    }

    // ----------------------------------------------------------------------------------------------------------------------

    private Map<String, List<String>> _retrieveHypernyms(Set<String> terms) {
/*
        ClientConfig config = new DefaultClientConfig();

        Client client = Client.create(config);
        String knowledgeBasePath = "/uia/knowledgebase";

        URI testServiceURI = UriBuilder.fromUri("http://localhost:8080/epnoi/rest").build();
        WebResource service = client.resource(testServiceURI);


        MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
        for (String term : terms) {
            queryParams.add("source", term);
        }

        Map<String, List<String>> hypernyms = service.path(knowledgeBasePath + "/relations/hypernymy/targets")
                .queryParams(queryParams).type(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(Map.class);

return  hypernyms;



        Integer thriftPort = (Integer)parameters.getParameterValue(RelationalSentencesCorpusCreationParameters.THRIFT_PORT);
        KnowledgeBaseServiceClient uiaService = new KnowledgeBaseServiceClient();
*/
        Map<String, List<String>> stemmedForms=new HashMap<>();

        try {
            //uiaService.init("localhost", thriftPort);
            //System.out.println("It has been properly initialized!");
            stemmedForms =  this.knowledgeBaseServiceClient.getRelated(new ArrayList<>(terms), RelationHelper.HYPERNYMY);
            return stemmedForms;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
/*
        }finally {
            uiaService.close();
        }
*/

     //   return stemmedForms;

    }

    // ----------------------------------------------------------------------------------------------------------------------

    private Map<String, List<String>> _retrieveStems(Set<String> terms) {
        /*
        ClientConfig config = new DefaultClientConfig();

        Client client = Client.create(config);
        String knowledgeBasePath = "/uia/knowledgebase";


        URI testServiceURI = UriBuilder.fromUri("http://localhost:8080/epnoi/rest").build();
        WebResource service = client.resource("http://localhost:8080/epnoi/rest");

        MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
        for (String term : terms) {
            queryParams.add("term", term);
        }


        Map<String, List<String>> stemmedForms = service.path(knowledgeBasePath + "/stem").queryParams(queryParams)
                .type(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(Map.class);
        Map<String, Set<String>> stemsMap = new HashMap<>();
*/

        /*
        Integer thriftPort = (Integer)parameters.getParameterValue(RelationalSentencesCorpusCreationParameters.THRIFT_PORT);
        KnowledgeBaseServiceClient uiaService = new KnowledgeBaseServiceClient();
      */
       Map<String, List<String>> stemmedForms=new HashMap<>();

        try {
        //    this.knowledgeBaseServiceClient.init("localhost", thriftPort);
            //System.out.println("It has been properly initialized!");
            stemmedForms =  knowledgeBaseServiceClient.stem(new ArrayList<>(terms));
        } catch (Exception e) {
            e.printStackTrace();


        }
        /*
        finally {
            knowledgeBaseServiceClient.close();
        }
*/
        return stemmedForms;
    }

    // ----------------------------------------------------------------------------------------------------------------------

    private List<String> _stem(String term) {
        //System.out.println("----> "+this.stemmingTable.get(term).getClass());
        List<String> stems = this.stemmingTable.get(term);
        if (stems != null)
            return stems;
        else
            return new ArrayList<>();
    }

    private List<String> _hypernyms(String term) {
        List<String> hypernyms = this.hypernymsTable.get(term);
        if (hypernyms != null)
            return hypernyms;
        else
            return new ArrayList<>();
    }

    // ----------------------------------------------------------------------------------------------------------------------

    private Map<String, Annotation> _initTermsAnnotationsTable(Sentence sentence) {
        HashMap<String, Annotation> termsAnnotationsTable = new HashMap<String, Annotation>();

        AnnotationSet termCandidatesAnnotationSet = sentence.getContainedAnnotations()
                .get(NLPAnnotationsConstants.TERM_CANDIDATE);


        for (Annotation termAnnotation : sentence.getContainedAnnotations()
                .get(NLPAnnotationsConstants.TERM_CANDIDATE)) {
            Long startOffset = termAnnotation.getStartNode().getOffset()
                    - sentence.getAnnotation().getStartNode().getOffset();
            Long endOffset = termAnnotation.getEndNode().getOffset()
                    - sentence.getAnnotation().getStartNode().getOffset();

            String term = "";
            try {
                // First of all we retrieve the surface form of the term

                term = sentence.getContent().getContent(startOffset, endOffset).toString();

            } catch (Exception e) {
                e.printStackTrace();
                term = "";

            }

            // We stem the surface form (we left open the possibility of
            // different stemming results so we consider a set of stemmed
            // forms)
            _addTermToTermsTable(term, termAnnotation, termsAnnotationsTable);
        }
        return termsAnnotationsTable;
    }

    /**
     * @param sentence
     * @return The set of term candidates surface and stemmed forms that appear in the sentence
     */
    private Set<String> _initSentenceTerms(Sentence sentence) {
        Set<String> termCandatesSurfaceForms = _getSentenceTermsSurfaceForms(sentence);
        _initStemmingTable(termCandatesSurfaceForms);
        _initHypernymsTable(termCandatesSurfaceForms);

        Set<String> termCandidatesSurfaceAndStemmedForms = _addStemmsToSentenceTerms(termCandatesSurfaceForms);


        return termCandatesSurfaceForms;
    }

    private Set<String> _addStemmsToSentenceTerms(Set<String> termCandatesSurfaceForms) {
        Set<String> sentenceTerms = new HashSet<>();
        for (String term : termCandatesSurfaceForms) {
            sentenceTerms.add(term);
            sentenceTerms.retainAll(_stem(term));
        }
        return sentenceTerms;

    }


    private void _initHypernymsTable(Set<String> termCandatesSurfaceForms) {
        this.hypernymsTable = _retrieveHypernyms(termCandatesSurfaceForms);
    }

    private void _initStemmingTable(Set<String> termCandatesSurfaceForms) {
        this.stemmingTable = _retrieveStems(termCandatesSurfaceForms);
    }


    private Set<String> _getSentenceTermsSurfaceForms(Sentence sentence) {
        AnnotationSet termCandidatesAnnotationSet = sentence.getContainedAnnotations().get(NLPAnnotationsConstants.TERM_CANDIDATE);

        Set<String> termCandatesSurfaceForms = new HashSet<>();

        for (Annotation termAnnotation : termCandidatesAnnotationSet) {
            Long startOffset = termAnnotation.getStartNode().getOffset()
                    - sentence.getAnnotation().getStartNode().getOffset();
            Long endOffset = termAnnotation.getEndNode().getOffset()
                    - sentence.getAnnotation().getStartNode().getOffset();

            String termSurfaceForm = null;
            try {
                // First of all we retrieve the surface form of the term

                termSurfaceForm = sentence.getContent().getContent(startOffset, endOffset).toString();

            } catch (Exception e) {
                e.printStackTrace();


            }
            if (termCandatesSurfaceForms != null) {
                termCandatesSurfaceForms.add(termSurfaceForm);
            }
        }
        return termCandatesSurfaceForms;
    }


    // ----------------------------------------------------------------------------------------------------------------------

    private void _addTermToTermsTable(String term, Annotation termAnnotation,
                                      HashMap<String, Annotation> termsAnnotationsTable) {

        if (term.length() > MIN_TERM_LENGTH) {
            for (String stemmedTerm : _stem(term)) {
                termsAnnotationsTable.put(stemmedTerm, termAnnotation);
            }
            termsAnnotationsTable.put(term, termAnnotation);
        }

    }
    // ----------------------------------------------------------------------------------------------------------------------

    private List<RelationalSentenceCandidate> _createRelationalSentence(Sentence sentence,
                                                                        Map<String, Annotation> termsAnnotationsTable, String term, List<String> termHypernyms) {
        List<RelationalSentenceCandidate> relationalSentenceCandidates = new ArrayList<>();
        Annotation sourceTermAnnotation = termsAnnotationsTable.get(term);

        for (String destinationTerm : termHypernyms) {

            Annotation targetTermAnnotation = termsAnnotationsTable.get(destinationTerm);

            RelationalSentenceCandidate relationalSentenceCandidate = new RelationalSentenceCandidate(sentence,
                    sourceTermAnnotation, targetTermAnnotation);
            relationalSentenceCandidates.add(relationalSentenceCandidate);
        }
        return relationalSentenceCandidates;
    }
    // ----------------------------------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        Integer algo = 0;
        String name = null;
        try{
            algo++;
            name.charAt(0);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            algo=0;
        }
        System.out.printf("algo> "+algo);
    }


}
