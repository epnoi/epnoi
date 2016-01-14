package org.epnoi.modeler.builder;

import es.upm.oeg.epnoi.matching.metrics.domain.entity.ConceptualResource;
import es.upm.oeg.epnoi.matching.metrics.domain.entity.RegularResource;
import es.upm.oeg.epnoi.matching.metrics.domain.space.ConceptsSpace;
import es.upm.oeg.epnoi.matching.metrics.domain.space.TopicsSpace;
import es.upm.oeg.epnoi.matching.metrics.topics.LDASettings;
import es.upm.oeg.epnoi.matching.metrics.topics.search.LDASolution;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.linalg.Vector;
import org.epnoi.modeler.helper.SparkHelper;
import org.epnoi.modeler.models.topic.TopicConfiguration;
import org.epnoi.modeler.models.topic.TopicModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import scala.Tuple2;
import scala.collection.JavaConverters;

import java.util.List;
import java.util.Map;

/**
 * Created by cbadenes on 11/01/16.
 */
@Component
public class TopicModelBuilder {

    private static final Logger logger = LoggerFactory.getLogger(TopicModelBuilder.class);

    @Autowired
    WorkspaceBuilder workspaceBuilder;

    @Autowired
    SparkHelper sparkHelper;

    @Value("${epnoi.modeler.maxiterations}")
    Integer maxIterations;

    @Value("${epnoi.modeler.similar.max}")
    Integer maxWords;

    public TopicModel build(String id, List<RegularResource> regularResources){

        JavaRDD<RegularResource> rrs = sparkHelper.getSc().parallelize(regularResources);

        ConceptsSpace workspace = workspaceBuilder.from(id, rrs);

        // TODO LDASettings can not be static class!!!
        LDASettings.setMaxIterations(maxIterations);

        logger.info("Learning Topic Model for '" + id + "' maxIt: " + maxIterations);
        LDASolution settings = LDASettings.learn(workspace.featureVectors(), maxIterations, maxIterations);

        logger.info("Topic Model for '" + id + "' learnt with: alpha=" + settings.getAlpha() + ", beta="+ settings.getBeta() + " and numTopics: " + settings.getTopics());

        return build(id, workspace, settings.getTopics(), settings.getAlpha(), settings.getBeta(), maxIterations);
    }

    public TopicModel build(String id, List<RegularResource> regularResources, Integer numTopics, Double alpha, Double beta, Integer maxIt){
        JavaRDD<RegularResource> rrs = sparkHelper.getSc().parallelize(regularResources);
        return build(id, workspaceBuilder.from(id, rrs), numTopics, alpha, beta, maxIt);
    }



    private TopicModel build(String id, ConceptsSpace conceptsSpace, Integer numTopics, Double alpha, Double beta, Integer maxIt){
        // TODO LDASettings can not be static class!!!
        // Manual Configuration
        LDASettings.setTopics(numTopics);
        LDASettings.setAlpha(alpha);
        LDASettings.setBeta(beta);
        LDASettings.setMaxIterations(maxIt);

        // Build model
        TopicsSpace topicsSpace = new TopicsSpace(conceptsSpace);

        Map<Object, String> words               = (Map<Object, String>) JavaConverters.mapAsJavaMapConverter(conceptsSpace.vocabulary().wordsByKeyMap()).asJava();
        Tuple2<int[], double[]>[] topics        = topicsSpace.model().ldaModel().describeTopics(200);
        List<Tuple2<Object, Vector>> documents  = topicsSpace.model().ldaModel().topicDistributions().toJavaRDD().collect();
        List<Tuple2<Object, ConceptualResource>> resources = conceptsSpace.conceptualResourcesMap().toJavaRDD().collect();

        logger.info("Created topic model with " + documents.size() + " documents");
        TopicConfiguration configuration = new TopicConfiguration(LDASettings.topics(),LDASettings.alpha(), LDASettings.beta(), LDASettings.maxEvals(), LDASettings.maxEvals());
        return new TopicModel(id,configuration,maxWords,documents,words,topics,resources);
    }



}
