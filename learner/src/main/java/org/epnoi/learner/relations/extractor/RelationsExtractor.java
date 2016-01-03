package org.epnoi.learner.relations.extractor;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.DocumentContent;
import gate.util.InvalidOffsetException;
import org.epnoi.learner.DomainsTable;
import org.epnoi.learner.LearningParameters;
import org.epnoi.learner.relations.patterns.RelationalPattern;
import org.epnoi.learner.relations.patterns.RelationalPatternsModel;
import org.epnoi.learner.relations.patterns.lexical.LexicalRelationalPatternGenerator;
import org.epnoi.learner.terms.TermCandidateBuilder;
import org.epnoi.learner.terms.TermsTable;
import org.epnoi.model.*;
import org.epnoi.model.commons.Parameters;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.nlp.gate.NLPAnnotationsConstants;
import org.epnoi.uia.informationstore.SelectorHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class RelationsExtractor {
    private static final Logger logger = Logger
            .getLogger(RelationsExtractor.class.getName());
    private static final long MAX_DISTANCE = 20;
    private Core core;
    private RelationalPatternsModel softPatternModel;
    private Parameters parameters;
    private DomainsTable domainsTable;
    private TermsTable termsTable;
    private LexicalRelationalPatternGenerator patternsGenerator;
    private RelationsTable relationsTable;
    private double hypernymExtractionThreshold;
    private String targetDomain;
    private boolean considerKnowledgeBase = false;
    private KnowledgeBase knowledgeBase;


    // --------------------------------------------------------------------------------------

    public void init(Core core, DomainsTable domainsTable, Parameters parameters) {
        logger.info("Initializing the Relations Extractor with the following parameters");
        logger.info(parameters.toString());

        this.core = core;
        this.parameters = parameters;
        String hypernymModelPath = (String) parameters
                .getParameterValue(LearningParameters.HYPERNYM_MODEL_PATH);
        this.hypernymExtractionThreshold = (double) parameters
                .getParameterValue(LearningParameters.HYPERNYM_RELATION_EXTRACTION_THRESHOLD);
        this.targetDomain = (String) parameters
                .getParameterValue(LearningParameters.TARGET_DOMAIN_URI);

        this.considerKnowledgeBase = (boolean) parameters
                .getParameterValue(LearningParameters.CONSIDER_KNOWLEDGE_BASE);
        this.patternsGenerator = new LexicalRelationalPatternGenerator();
        this.domainsTable = domainsTable;
        this.relationsTable = new RelationsTable();
        // We retrieve the knowledge base just in case that it must be
        // considered when searching for relations
       /*
        if (considerKnowledgeBase) {
            try {
                this.knowledgeBase = core.getKnowledgeBaseHandler()
                        .getKnowledgeBase();
            } catch (EpnoiResourceAccessException e) {
                // TODO Auto-generated catch block
                throw new EpnoiInitializationException(e.getMessage());
            }
        }
*/
        this.softPatternModel = (RelationalPatternsModel)parameters.getParameterValue(LearningParameters.HYPERNYM_MODEL);


    }

    // ---------------------------------------------------------------------------------------

    public RelationsTable extract(TermsTable termsTable) {
        logger.info("Extracting the Relations Table");
        String relationsTableUri= this.domainsTable.getTargetDomain().getUri()+"/relations";
        this.termsTable = termsTable;
        this.relationsTable = new RelationsTable();
        this.relationsTable.setUri(relationsTableUri);
        // The relations finding task is only performed in the target domain,
        // these are the resources that we should consider

        for (String domainResourceURI : domainsTable.getDomainResources().get(
                domainsTable.getTargetDomain().getUri())) {
            logger.info("Indexing the resource " + domainResourceURI);
            _findRelationsInResource(domainResourceURI);
        }
        return relationsTable;
    }

    // -----------------------------------------------------------------------------------

    private void _findRelationsInResource(String domainResourceURI) {
        Content<Object> annotatedResource = retrieveAnnotatedDocument(domainResourceURI);
        Document annotatedResourceDocument = (Document) annotatedResource
                .getContent();

        AnnotationSet sentenceAnnotations = annotatedResourceDocument
                .getAnnotations().get(NLPAnnotationsConstants.SENTENCE);

        System.out.println("There are " + sentenceAnnotations.size());
        DocumentContent sentenceContent = null;
        AnnotationSet resourceAnnotations = annotatedResourceDocument
                .getAnnotations();

        Iterator<Annotation> sentencesIt = sentenceAnnotations.iterator();
        while (sentencesIt.hasNext()) {
            Annotation sentenceAnnotation = sentencesIt.next();

            Long sentenceStartOffset = sentenceAnnotation.getStartNode()
                    .getOffset();
            Long sentenceEndOffset = sentenceAnnotation.getEndNode()
                    .getOffset();
            TermCandidateBuilder termCandidateBuilder = new TermCandidateBuilder(
                    annotatedResourceDocument);
            _testSentence(sentenceStartOffset, sentenceEndOffset,
                    annotatedResourceDocument, termCandidateBuilder);
            /*
			 * _testSentence(sentenceStartOffset, sentenceContent,
			 * annotatedResourceAnnotations.getContained( sentenceStartOffset,
			 * sentenceEndOffset));
			 */

        }

    }

    // -------------------------------------------------------------------------------------------

    private void _testSentence(Long sentenceStartOffset,
                               Long sentenceEndOffset, Document annotatedResource,
                               TermCandidateBuilder termCandidateBuilder) {

        AnnotationSet senteceAnnotationSet = annotatedResource.getAnnotations()
                .get(sentenceStartOffset, sentenceEndOffset);
        List<Annotation> termAnnotations = new ArrayList<Annotation>();
        for (Annotation termAnnotation : senteceAnnotationSet
                .get(NLPAnnotationsConstants.TERM_CANDIDATE)) {
            termAnnotations.add(termAnnotation);
        }

        String sentenceContent = null;
        try {
            sentenceContent = annotatedResource.getContent()
                    .getContent(sentenceStartOffset, sentenceEndOffset)
                    .toString();
        } catch (InvalidOffsetException e) {

            e.printStackTrace();
        }
        int combinations = 0;
        long time = System.currentTimeMillis();
        for (int i = 0; i < termAnnotations.size(); i++)
            for (int j = i + 1; j < termAnnotations.size(); j++) {
                Annotation source = termAnnotations.get(i);
                Annotation target = termAnnotations.get(j);
                if (!_areFar(source, target)) {
                    // For each pair of terms we check both as target and as
                    // source

                    _extractProbableRelationsFromSentence(source, target,
                            annotatedResource, sentenceContent,
                            termCandidateBuilder);

                    _extractProbableRelationsFromSentence(target, source,
                            annotatedResource, sentenceContent,
                            termCandidateBuilder);
                    combinations++;

                } else {
                    // System.out.println("Are far:"+source+" > "+target);
                }
            }
        // System.out.println("Sentence took "+ Math.abs(time -
        // System.currentTimeMillis())+ " consisting of "+combinations);

    }

    // --------------------------------------------------------------------------------------------

    private boolean _areFar(Annotation source, Annotation target) {
        return (Math.abs(target.getEndNode().getOffset()
                - source.getEndNode().getOffset()) > MAX_DISTANCE);

    }

    // --------------------------------------------------------------------------------------------

    private void _extractProbableRelationsFromSentence(Annotation source,
                                                       Annotation target, Document annotatedResource,
                                                       String sentenceContent, TermCandidateBuilder termCandidateBuilder) {
        String sourceTermWord = termCandidateBuilder.buildTermCandidate(source)
                .getWord();
        String targetTermWord = termCandidateBuilder.buildTermCandidate(target)
                .getWord();


        if (this.considerKnowledgeBase && this.knowledgeBase.areRelated(sourceTermWord, targetTermWord,
                RelationHelper.HYPERNYMY)) {
            _createRelation(sentenceContent, sourceTermWord, targetTermWord,
                    1.0);
        } else {

            List<RelationalPattern> generatedPatterns = this.patternsGenerator
                    .generate(source, target, annotatedResource);
            for (RelationalPattern pattern : generatedPatterns) {
                double relationProbability = this.softPatternModel
                        .calculatePatternProbability(pattern);

                if (relationProbability > this.hypernymExtractionThreshold) {

                    _createRelation(sentenceContent, sourceTermWord,
                            targetTermWord, relationProbability);

                }
            }
        }
    }

    // --------------------------------------------------------------------------------------

    private void _createRelation(String sentenceContent, String sourceTermWord,
                                 String targetTermWord, double relationProbability) {
        Term sourceTerm = this.termsTable.getTerm(Term.buildURI(sourceTermWord,
                this.targetDomain));
		/*
		 * String targetToken = (String) target.getFeatures() .get("string");
		 */

        Term targetTerm = this.termsTable.getTerm(Term.buildURI(targetTermWord,
                this.targetDomain));

        if (sourceTerm != null && targetTerm != null) {

            this.relationsTable.introduceRelation(this.targetDomain,
                    sourceTerm, targetTerm, RelationHelper.HYPERNYMY,
                    sentenceContent, relationProbability);
        } else {
            // System.out.println("S_word " + sourceTermWord +
            // " S_term "
            // + sourceTerm);
            // System.out.println("T_word " + targetTermWord +
            // " T_term "
            // + targetTerm);

        }
    }

    // ---------------------------------------------------------------------------------------

    private Content<Object> retrieveAnnotatedDocument(String URI) {

        Selector selector = new Selector();
        selector.setProperty(SelectorHelper.URI, URI);
        selector.setProperty(SelectorHelper.TYPE, RDFHelper.PAPER_CLASS);
        selector.setProperty(SelectorHelper.ANNOTATED_CONTENT_URI, URI + "/"
                + AnnotatedContentHelper.CONTENT_TYPE_OBJECT_XML_GATE);

        Content<Object> annotatedContent = core.getInformationHandler()
                .getAnnotatedContent(selector);
		/*
		 * Document document = null; try { document = (Document) Factory
		 * .createResource( "gate.corpora.DocumentImpl", Utils.featureMap(
		 * gate.Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME, (String)
		 * annotatedContent.getContent(),
		 * gate.Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME, "text/xml"));
		 * 
		 * } catch (ResourceInstantiationException e) { // TODO Auto-generated
		 * System.out .println(
		 * "Couldn't retrieve the GATE document that represents the annotated content of "
		 * + URI); e.printStackTrace(); }
		 */
        return annotatedContent;
    }


}
