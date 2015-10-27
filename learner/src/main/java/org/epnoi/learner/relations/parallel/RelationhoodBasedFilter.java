package org.epnoi.learner.relations.parallel;

import org.apache.spark.api.java.function.Function;
import org.epnoi.learner.relations.patterns.RelationalPattern;
import org.epnoi.learner.relations.patterns.RelationalPatternsModel;
import org.epnoi.learner.relations.patterns.lexical.BigramSoftPatternModel;
import org.epnoi.learner.relations.patterns.lexical.LexicalRelationalPattern;
import org.epnoi.learner.relations.patterns.lexical.LexicalRelationalPatternGenerator;

import org.epnoi.learner.relations.patterns.lexical.LexicalRelationalPatternNode;
import org.epnoi.model.RelationalSentence;

import java.util.Iterator;
import java.util.List;

public class RelationhoodBasedFilter implements Function<RelationalSentence, Boolean> {
    private final double THRESHOLD = 0.4;
    private RelationalPatternsModel softPatternModel = new BigramSoftPatternModel();

    @Override
    public Boolean call(RelationalSentence relationalSentence) throws Exception {
        LexicalRelationalPatternGenerator patternsGenerator = new LexicalRelationalPatternGenerator();

        List<RelationalPattern> generatedPatterns = patternsGenerator
                .generate(relationalSentence);
        boolean found = false;
        Iterator<RelationalPattern> generatedPatternsIt = generatedPatterns.iterator();
        while (generatedPatternsIt.hasNext() && !found) {
            found = (this.softPatternModel
                    .calculatePatternProbability(generatedPatternsIt.next()) > THRESHOLD);

        }
        return found;
    }
}
