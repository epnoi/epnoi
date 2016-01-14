package org.epnoi.modeler.models.topic;

import es.upm.oeg.epnoi.matching.metrics.domain.entity.ConceptualResource;
import org.apache.spark.mllib.linalg.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.io.Serializable;
import java.util.*;

/**
 * Created by cbadenes on 11/01/16.
 */
public class TopicModelCopy implements Serializable{

    private static final Logger logger = LoggerFactory.getLogger(TopicModelCopy.class);
    private String id;

    private Tuple2<int[], double[]>[] topics;
    private Map<String,Long> ids = new HashMap<>();
    private Map<Long, String> words;
    private List<Tuple2<Object, Vector>> documents;

    public static class Configuration implements Serializable{

        private final Double alpha;
        private final Double beta;
        private final Integer numIterations;
        private final Integer maxEvaluations;
        private final Integer numTopics;

        public Configuration(Integer numTopics, Double alpha, Double beta, Integer numIterations, Integer maxEvaluations){
            this.numTopics = numTopics;
            this.alpha  = alpha;
            this.beta   = beta;
            this.numIterations = numIterations;
            this.maxEvaluations = maxEvaluations;
        }

        public Double getAlpha(){return alpha;}
        public Double getBeta(){return beta;}
        public Integer getNumTopics(){return numTopics;}
        public Integer getNumIterations(){return numIterations;}
        public Integer getMaxEvaluations(){return maxEvaluations;}
    }

    public TopicModelCopy(String id, Configuration configuration, List<Tuple2<Object, Vector>> documents, Map<Object, String> words, Tuple2<int[], double[]>[] topics, List<Tuple2<Object, ConceptualResource>> resources ){

        this.id = id;
        this.words = new HashMap<>();

        for (Object key: words.keySet()){
            String word = words.get(key);
            Long wordId = (Long) key;
            this.words.put(wordId,word);
            logger.info("adding word: " + word + " with key: " + wordId);
        }

        this.topics     = topics;
        this.documents  = documents;
        this.ids        = new HashMap<>();

        for (Tuple2<Object,ConceptualResource> docDist : resources){

            Long resId                  = (Long) docDist._1();
            ConceptualResource cr       = docDist._2();
            String uri                  = cr.resource().uri();

            logger.info("Added conceptual resource: " + uri + " with Id: " + resId);
            ids.put(uri,resId);
        }
    }

    public TopicModelCopy(String id){
        this.id = id;
    }

    public boolean isEmpty(){
        return topics == null;
    }

    public List<TopicData> getTopics(Integer numWords){

        if (isEmpty()) return Arrays.asList(new TopicData[]{});

        List<TopicData> wds = new ArrayList<>();

        for (int i=0;i < topics.length; i++ ){

            TopicData wd = new TopicData();
            wd.setId("topic"+i);
            wd.setLabel(id);
            Tuple2<int[], double[]> distr = topics[i];

            int[] wordIds = distr._1();
            double[] weights = distr._2();

            for (int j = 0; j < numWords; j++){
                int wordId = wordIds[j];
                String word = words.get(new Long(wordId));

                logger.trace("Word: '" + word + "' [" + wordId + "/" + words.size() + "]");

                Double weight = weights[j];
                wd.add(word,weight);
            }

            wds.add(wd);
        }

        return wds;
    }




    public double[] distributionOf(String uri){


        Long resId    = ids.get(uri);

        if (isEmpty()){
            logger.warn("Topic Model for '" + id+ "' is empty. Then '" + uri + "' has no topic distribution");
            return new double[]{0.0};
        }

        logger.debug("Trying to get topic distribution of item(uri): " + uri+" - "+ resId + " in model of '" + id + "'");
        Optional<Tuple2<Object, Vector>> tupleOpt;
        try {
            tupleOpt = documents.stream().filter(t -> t._1().equals(resId)).findFirst();
        }catch (NullPointerException e){
            tupleOpt = Optional.empty();
        }

        if (!tupleOpt.isPresent()){
            logger.error("Topics distribution not found for item: " + uri  + " in documents: " + documents.size());
            return new double[]{0.0};
        }
        Tuple2<Object, Vector> tuple = tupleOpt.get();
        logger.debug("Tuple: " + tuple);

        double[] dist = tuple._2().toArray();
        logger.info("Topics Distribution of '"+uri+"' in '"+id+"': " + Arrays.toString(dist));
        return dist;
    }

}
