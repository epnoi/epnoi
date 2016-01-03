package org.epnoi.learner.relations.extractor.parallel;

import gate.Document;
import org.epnoi.learner.LearningParameters;
import org.epnoi.learner.relations.patterns.RelationalPattern;
import org.epnoi.learner.relations.patterns.lexical.LexicalRelationalPatternGenerator;
import org.epnoi.learner.relations.patterns.lexical.RelaxedBigramSoftPatternModel;
import org.epnoi.learner.terms.TermCandidateBuilder;
import org.epnoi.model.*;
import org.epnoi.uia.commons.GateUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RelationalSentenceToRelationMapper {
    private RelaxedBigramSoftPatternModel softPatternModel;
    private double THRESHOLD;
    private String domain;


    //------------------------------------------------------------------------------------------------------------------


    public RelationalSentenceToRelationMapper(LearningParameters parameters) {
        this.softPatternModel = (RelaxedBigramSoftPatternModel) parameters.getParameterValue(LearningParameters.HYPERNYM_MODEL);
        this.THRESHOLD = (double) parameters.getParameterValue(LearningParameters.HYPERNYM_RELATION_EXTRACTION_THRESHOLD);
        this.domain = (String) parameters.getParameterValue(LearningParameters.TARGET_DOMAIN_URI);
    }
    //------------------------------------------------------------------------------------------------------------------


    public Iterable<Relation> call(DeserializedRelationalSentence relationalSentence) throws Exception {
        List<Relation> foundRelations = new ArrayList<>();
        LexicalRelationalPatternGenerator patternsGenerator = new LexicalRelationalPatternGenerator();

        List<RelationalPattern> generatedPatterns = patternsGenerator
                .generate(relationalSentence);

        Iterator<RelationalPattern> generatedPatternsIt = generatedPatterns.iterator();
        double relationhood = 0;
        while (generatedPatternsIt.hasNext()) {
            relationhood = Math.max(relationhood, this.softPatternModel
                    .calculatePatternProbability(generatedPatternsIt.next()));
        }
        if (relationhood > THRESHOLD) {
            System.out.println("AQUI HAY UNA!!!!!!!!!!!!!!!!!!!!!!("+relationhood+")  "+relationalSentence.getSentence());
            Relation relation = _createRelation(relationalSentence, relationhood);
            foundRelations.add(relation);
        }

        return foundRelations;
    }

    //------------------------------------------------------------------------------------------------------------------

    private Relation _createRelation(DeserializedRelationalSentence relationalSentence,
                                     double relationhood) {

        Document relationalSentenceDocument = relationalSentence.getAnnotatedSentence();
        TermCandidateBuilder termCandidateBuilder = new TermCandidateBuilder(relationalSentenceDocument);

        Relation relation = new Relation();
        AnnotatedWord<TermMetadata> sourceTerm = termCandidateBuilder.buildTermCandidate(relationalSentence.getSource());
        AnnotatedWord<TermMetadata> targetTerm = termCandidateBuilder.buildTermCandidate(relationalSentence.getTarget());

        String relationURI = Relation.buildURI(sourceTerm
                .getWord(), targetTerm.getWord(), RelationHelper.HYPERNYMY, domain);


        relation.setUri(relationURI);
        relation.setSource(Term.buildURI(sourceTerm.getWord(), domain));
        relation.setTarget(Term.buildURI(targetTerm.getWord(), domain));
        relation.setType(RelationHelper.HYPERNYMY);
        relation.addProvenanceSentence(relationalSentence.getSentence(), relationhood);

        return relation;
    }

    //------------------------------------------------------------------------------------------------------------------

}
