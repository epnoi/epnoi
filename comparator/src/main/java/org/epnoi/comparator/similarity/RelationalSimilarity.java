package org.epnoi.comparator.similarity;

import es.upm.oeg.epnoi.matching.metrics.similarity.JensenShannonSimilarity;
import org.epnoi.storage.model.Relationship;

import java.util.Comparator;
import java.util.List;

/**
 * Created by cbadenes on 13/01/16.
 */
public class RelationalSimilarity {

    public static Double between(List<Relationship> relationships1,List<Relationship> relationships2){

        Comparator<Relationship> byUri = (e1, e2) ->e1.getUri().compareTo(e2.getUri());

        double[] weights1 = relationships1.stream().sorted(byUri).mapToDouble(x -> x.getWeight()).toArray();
        double[] weights2 = relationships2.stream().sorted(byUri).mapToDouble(x -> x.getWeight()).toArray();

        return JensenShannonSimilarity.apply(weights1, weights2);
    }
}
