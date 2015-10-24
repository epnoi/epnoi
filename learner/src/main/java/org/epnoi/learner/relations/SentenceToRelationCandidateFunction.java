package org.epnoi.learner.relations;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.epnoi.learner.relations.corpus.parallel.RelationalSentenceCandidate;
import org.epnoi.learner.relations.corpus.parallel.Sentence;

/**
 * Created by rgonza on 24/10/15.
 */
public class SentenceToRelationCandidateFunction implements FlatMapFunction<Sentence, RelationalSentenceCandidate> {
    @Override
    public Iterable<RelationalSentenceCandidate> call(Sentence sentence) throws Exception {
        return null;
    }
}
