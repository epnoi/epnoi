package org.epnoi.learner.relations.extractor.parallel;

import org.apache.spark.api.java.function.Function2;
import org.epnoi.model.Relation;

/**
 * Created by rgonzalez on 29/10/15.
 */

/**
 * This class is contains the functionality for grouping by key the relations that have been found in the relations extraction process.
 * The key that has been considered is the uri of the relations
 */

public class RelationsReduceByKeyFunction implements Function2<Relation,Relation,Relation> {
    /**
     * This functions  given two relations (that share the same uri), merge them.
     * @param relation
     * @param relation2
     * @return
     * @throws Exception
     */
    @Override
    public Relation call(Relation relation, Relation relation2) throws Exception {
        if(relation.getProvenanceRelationhoodTable().size()>relation2.getProvenanceRelationhoodTable().size()){
            relation.getProvenanceRelationhoodTable().putAll(relation2.getProvenanceRelationhoodTable());
            return relation;
        }else{
            relation2.getProvenanceRelationhoodTable().putAll(relation.getProvenanceRelationhoodTable());
            return relation2;
        }
    }
}
