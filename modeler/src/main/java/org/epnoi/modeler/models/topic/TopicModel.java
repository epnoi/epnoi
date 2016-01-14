package org.epnoi.modeler.models.topic;

import es.upm.oeg.epnoi.matching.metrics.domain.entity.ConceptualResource;
import lombok.Getter;
import org.apache.spark.mllib.linalg.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.io.Serializable;
import java.util.*;

/**
 * Created by cbadenes on 11/01/16.
 */
public class TopicModel implements Serializable{

    private static final Logger logger = LoggerFactory.getLogger(TopicModel.class);

    @Getter
    private Map<String,List<TopicDistribution>> resources;

    @Getter
    private final TopicConfiguration configuration;

    @Getter
    private final String id;

    @Getter
    private List<TopicData> topics;

    public TopicModel(String id, TopicConfiguration configuration, Integer wordsByTopic, List<Tuple2<Object, Vector>> documents, Map<Object, String> words,Tuple2<int[], double[]>[] topics, List<Tuple2<Object, ConceptualResource>> resources ) {

        this.id = id;
        this.configuration = configuration;
        this.topics = new LinkedList<>();
        this.resources = new HashMap<>();

        // Loading topics
        for (int i = 0; i < topics.length; i++) {

            TopicData wd = new TopicData();
            wd.setId(String.valueOf(i));
            wd.setLabel(id);
            Tuple2<int[], double[]> distr = topics[i];

            int[] wordIds = distr._1();
            double[] weights = distr._2();

            int numWords = (wordIds.length > wordsByTopic) ? wordsByTopic : wordIds.length;
            for (int j = 0; j < numWords; j++) {
                int wordId = wordIds[j];
                String word = words.get(new Long(wordId));
                logger.trace("Word: '" + word + "' [" + wordId + "/" + words.size() + "]");
                Double weight = weights[j];
                wd.add(word, weight);
            }
            this.topics.add(wd);
        }


        // Loading Resources

        for (Tuple2<Object, ConceptualResource> docDist : resources) {

            Long resId = (Long) docDist._1();
            ConceptualResource cr = docDist._2();
            String uri = cr.resource().uri();

            List<TopicDistribution> topicDistributionList = new ArrayList<>();
            logger.debug("Trying to get topic distribution of item(uri): " + uri + " - " + resId + " in model of '" + id + "'");

            Optional<Tuple2<Object, Vector>> tupleOpt = documents.stream().filter(t -> t._1().equals(resId)).findFirst();
            Tuple2<Object, Vector> tuple = tupleOpt.get();
            logger.debug("Tuple: " + tuple);

            double[] dist = tuple._2().toArray();
            logger.debug("Topics Distribution of '" + uri + "' in '" + id + "': " + Arrays.toString(dist));

            for (int i = 0; i < dist.length; i++) {
                TopicDistribution topicDistribution = new TopicDistribution();
                topicDistribution.setTopic(String.valueOf(i));
                topicDistribution.setWeight(dist[i]);
                topicDistributionList.add(topicDistribution);
                logger.debug("Added topic distribution: " + topicDistribution);
            }

            // saving topic distribution
            this.resources.put(uri, topicDistributionList);
        }
    }

}
