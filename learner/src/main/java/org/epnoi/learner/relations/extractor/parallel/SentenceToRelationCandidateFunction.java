package org.epnoi.learner.relations.extractor.parallel;

import gate.Annotation;
import gate.AnnotationSet;
import org.epnoi.learner.LearningParameters;
import org.epnoi.learner.relations.corpus.parallel.RelationalSentenceCandidate;
import org.epnoi.learner.relations.corpus.parallel.Sentence;
import org.epnoi.nlp.gate.NLPAnnotationsConstants;

import java.util.ArrayList;
import java.util.List;


public class SentenceToRelationCandidateFunction {
    private int MAX_DISTANCE;


    SentenceToRelationCandidateFunction(LearningParameters parameters) {
        this.MAX_DISTANCE = (Integer) parameters.getParameterValue(LearningParameters.MAX_SOURCE_TARGET_DISTANCE);
    }

    public Iterable<RelationalSentenceCandidate> call(Sentence sentence) throws Exception {
        List<RelationalSentenceCandidate> generatedRelationalSentenceCandidates = new ArrayList<>();

        AnnotationSet senteceAnnotationSet = sentence.getContainedAnnotations();
        List<Annotation> termAnnotations = new ArrayList<>();
        for (Annotation termAnnotation : senteceAnnotationSet
                .get(NLPAnnotationsConstants.TERM_CANDIDATE)) {
            termAnnotations.add(termAnnotation);
        }


        for (int i = 0; i < termAnnotations.size(); i++) {
            for (int j = i + 1; j < termAnnotations.size(); j++) {
                Annotation source = termAnnotations.get(i);
                Annotation target = termAnnotations.get(j);
                if (_areValid(source, target) && !_areFar(source, target))
                    _addRelationalSentenceCandidates(sentence, generatedRelationalSentenceCandidates, source, target);
            }
        }
        return generatedRelationalSentenceCandidates;

    }

    private boolean _areValid(Annotation source, Annotation target) {
        return ((source.getEndNode().getOffset() - source.getStartNode().getOffset() > 2) && (target.getEndNode().getOffset() - target.getStartNode().getOffset() > 2));
    }

    private void _addRelationalSentenceCandidates(Sentence sentence, List<RelationalSentenceCandidate> generatedRelationalSentenceCandidates, Annotation source, Annotation target) {
        //In case that the source and target are not too far away we generate the relational sentence candidates associated with this sentence
        // if (!_areFar(source, target)) {
        // For each pair of terms we check both as target and as
        // source
        generateRelationalSentenceCandidates(sentence, generatedRelationalSentenceCandidates, source, target);


    }

    private void generateRelationalSentenceCandidates(Sentence sentence, List<RelationalSentenceCandidate> generatedRelationalSentenceCandidates, Annotation source, Annotation target) {

        RelationalSentenceCandidate relationalSentenceCandidate = new RelationalSentenceCandidate(sentence, source, target);

        RelationalSentenceCandidate inverseRelationalSentenceCandidate = new RelationalSentenceCandidate(sentence, target, source);

        generatedRelationalSentenceCandidates.add(relationalSentenceCandidate);
        generatedRelationalSentenceCandidates.add(inverseRelationalSentenceCandidate);
    }

    private boolean _areFar(Annotation source, Annotation target) {
        return (Math.abs(target.getEndNode().getOffset()
                - source.getEndNode().getOffset()) > MAX_DISTANCE);

    }
}
