package org.epnoi.learner.relations.extractor.parallel;

import org.apache.spark.api.java.function.PairFunction;
import org.epnoi.model.Relation;
import scala.Tuple2;

/**
 * Created by rgonzalez on 29/10/15.
 */
public class ResourceKeyValueMapper implements PairFunction<Relation, String, Relation>{

    @Override
    public Tuple2<String, Relation> call(Relation resource) throws Exception {
        return new Tuple2<>(resource.getUri(), resource);
    }
}
