package org.epnoi.learner.relations.parallel;

import org.apache.spark.api.java.function.Function;
import org.epnoi.model.Relation;
import org.epnoi.model.RelationalSentence;


public class RelationalSentenceToRelationMapper implements Function<RelationalSentence,Relation> {
    @Override
    public Relation call(RelationalSentence relationalSentence) throws Exception {
        return null;
    }

}