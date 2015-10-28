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
import org.epnoi.model.Relation;
import org.epnoi.model.RelationalSentence;
import org.epnoi.model.Term;
import org.epnoi.uia.commons.GateUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RelationalSentenceToRelationMapper implements FlatMapFunction<RelationalSentence, Relation> {
    private final double THRESHOLD = 0.4;
    private RelationalPatternsModel softPatternModel = new BigramSoftPatternModel();

    @Override
    public Iterable<Relation> call(RelationalSentence relationalSentence) throws Exception {
        List<Relation> foundRelations = new ArrayList<>();
        LexicalRelationalPatternGenerator patternsGenerator = new LexicalRelationalPatternGenerator();

        List<RelationalPattern> generatedPatterns = patternsGenerator
                .generate(relationalSentence);

        Iterator<RelationalPattern> generatedPatternsIt = generatedPatterns.iterator();
        double relationHood = 0;
        while (generatedPatternsIt.hasNext()) {
            relationHood = Math.max(relationHood, this.softPatternModel
                    .calculatePatternProbability(generatedPatternsIt.next()));

        }

        if (relationHood > THRESHOLD) {
            Relation relation = _createRelation(relationalSentence, relationHood);

        }


        return foundRelations;
    }

    private Relation _createRelation(RelationalSentence relationalSentence,
                                     double relationhood) {

        Document relationalSentenceDocument = GateUtils.deserializeGATEDocument(relationalSentence.getAnnotatedSentence());
        TermCandidateBuilder termCandidateBulder = new TermCandidateBuilder(relationalSentenceDocument);

        Relation relation = new Relation();

/*
        String relationURI = Relation.buildURI(sourceTerm.getAnnotatedTerm()
                        .getWord(), targetTerm.getAnnotatedTerm().getWord(), type,
                domain);

        // If the relation is not already stored, we simply add it

        relation.setUri(relationURI);
        relation.setSource(sourceTerm.getUri());
        relation.setTarget(targetTerm.getUri());
        relation.setType(RelationsHelper);
        relation.addProvenanceSentence(provenanceSentence, relationhood);
*/
        return relation;
    }
}
