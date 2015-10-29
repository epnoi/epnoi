package org.epnoi.learner.relations.parallel;

import org.apache.spark.api.java.function.Function2;
import org.epnoi.model.Relation;

/**
 * Created by rgonzalez on 29/10/15.
 */
public class RelationsReduceByKeyFunction implements Function2<Relation,Relation,Relation> {

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
