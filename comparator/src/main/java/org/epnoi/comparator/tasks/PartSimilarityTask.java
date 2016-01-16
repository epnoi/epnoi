package org.epnoi.comparator.tasks;

import org.epnoi.comparator.helper.ComparatorHelper;
import org.epnoi.comparator.similarity.RelationalSimilarity;
import org.epnoi.storage.model.Analysis;
import org.epnoi.storage.model.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cbadenes on 13/01/16.
 */
// TODO Extends from an abstract class
public class PartSimilarityTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(PartSimilarityTask.class);

    private final Analysis analysis;

    private final ComparatorHelper helper;

    private Map<String,List<Relationship>> distributions;

    public PartSimilarityTask(Analysis analysis, ComparatorHelper helper){
        this.analysis = analysis;
        this.helper = helper;
        this.distributions = new HashMap<>();
    }

    @Override
    public void run() {
        List<String> parts = helper.getUdm().findPartsByDomain(analysis.getDomain());
        recursiveSimilarityCalculus(parts.get(0),parts.subList(1,parts.size()));
    }


    private void recursiveSimilarityCalculus(String uri, List<String> uris){
        if (uris == null || uris.isEmpty()) return;

        for (String uri2: uris){
            similarityCalculus(uri,uri2);
        }
        recursiveSimilarityCalculus(uris.get(0),uris.subList(1,uris.size()));
    }

    private void similarityCalculus(String uri1,String uri2){
        LOG.info("Calculating similarity between: " + uri1 + " and " + uri2);
        Double similarity = RelationalSimilarity.between(getDistributionOf(uri1), getDistributionOf(uri2));
        if (similarity > helper.getThreshold()){
            // Save relation in ddbb
            helper.getUdm().relatePartToPart(uri1,uri2,similarity,analysis.getDomain());
            helper.getUdm().relatePartToPart(uri2,uri1,similarity,analysis.getDomain());
        }


    }

    private List<Relationship> getDistributionOf(String uri){
        List<Relationship> distribution = distributions.getOrDefault(uri, helper.getUdm().findDealsByPartAndAnalysis(uri, analysis.getUri()));
        distributions.put(uri,distribution);
        return distribution;
    }

}
