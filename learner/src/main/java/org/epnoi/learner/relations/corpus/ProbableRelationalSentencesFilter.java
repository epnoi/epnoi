package org.epnoi.learner.relations.corpus;

import org.apache.spark.api.java.function.Function;
import org.epnoi.model.RelationalSentence;

/**
 * Created by rgonza on 24/10/15.
 */
public class ProbableRelationalSentencesFilter implements Function<RelationalSentence,Boolean> {
    @Override
    public Boolean call(RelationalSentence relationalSentence) throws Exception {
        return false;
    }
}
