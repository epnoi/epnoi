package org.epnoi.learner.relations.parallel;

import gate.Document;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.epnoi.learner.relations.patterns.RelationalPattern;
import org.epnoi.learner.relations.patterns.RelationalPatternsModel;
import org.epnoi.learner.relations.patterns.lexical.BigramSoftPatternModel;
import org.epnoi.learner.relations.patterns.lexical.LexicalRelationalPattern;
import org.epnoi.learner.relations.patterns.lexical.LexicalRelationalPatternGenerator;

import org.epnoi.learner.relations.patterns.lexical.LexicalRelationalPatternNode;
import org.epnoi.learner.terms.TermCandidateBuilder;
import org.epnoi.model.*;
import org.epnoi.uia.commons.GateUtils;
import org.epnoi.uia.informationstore.dao.cassandra.RelationsTableCassandraHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RelationalSentenceToRelationMapper implements FlatMapFunction<RelationalSentence, Relation> {
    private final double THRESHOLD = 0.4;
    private String domain = "";
    private RelationalPatternsModel softPatternModel = new BigramSoftPatternModel();

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public Iterable<Relation> call(RelationalSentence relationalSentence) throws Exception {
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
            Relation relation = _createRelation(relationalSentence, relationhood);
            foundRelations.add(relation);
        }

        return foundRelations;
    }

    //------------------------------------------------------------------------------------------------------------------

    private Relation _createRelation(RelationalSentence relationalSentence,
                                     double relationhood) {

        Document relationalSentenceDocument = GateUtils.deserializeGATEDocument(relationalSentence.getAnnotatedSentence());
        TermCandidateBuilder termCandidateBulder = new TermCandidateBuilder(relationalSentenceDocument);

        Relation relation = new Relation();
        AnnotatedWord<TermMetadata> sourceTerm = termCandidateBulder.buildTermCandidate(relationalSentence.getSource());
        AnnotatedWord<TermMetadata> targetTerm = termCandidateBulder.buildTermCandidate(relationalSentence.getTarget());

        String relationURI = Relation.buildURI(sourceTerm
                .getWord(), targetTerm.getWord(), RelationHelper.HYPERNYM, domain);

        // If the relation is not already stored, we simply add it

        relation.setUri(relationURI);
        relation.setSource(Term.buildURI(sourceTerm.getWord(), domain));
        relation.setTarget(Term.buildURI(targetTerm.getWord(), domain));
        relation.setType(RelationHelper.HYPERNYM);
        relation.addProvenanceSentence(relationalSentence.getSentence(), relationhood);

        return relation;
    }

    //------------------------------------------------------------------------------------------------------------------

}
