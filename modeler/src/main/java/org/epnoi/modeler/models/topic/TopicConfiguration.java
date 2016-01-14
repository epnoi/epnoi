package org.epnoi.modeler.models.topic;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by cbadenes on 12/01/16.
 */
@Data
public class TopicConfiguration implements Serializable {

    private final Double alpha;
    private final Double beta;
    private final Integer numIterations;
    private final Integer maxEvaluations;
    private final Integer numTopics;

    public TopicConfiguration(Integer numTopics, Double alpha, Double beta, Integer numIterations, Integer maxEvaluations){
        this.numTopics = numTopics;
        this.alpha  = alpha;
        this.beta   = beta;
        this.numIterations = numIterations;
        this.maxEvaluations = maxEvaluations;
    }
}
