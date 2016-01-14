package org.epnoi.comparator.eventbus;

import org.epnoi.comparator.services.TopicSimilarityService;
import org.epnoi.comparator.services.WordSimilarityService;
import org.epnoi.model.Event;
import org.epnoi.model.Resource;
import org.epnoi.model.modules.RoutingKey;
import org.epnoi.storage.model.Analysis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 13/01/16.
 */
@Component
public class AnalysisCreatedEventHandler extends AbstractEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AnalysisCreatedEventHandler.class);

    @Autowired
    TopicSimilarityService topicSimilarityService;

    @Autowired
    WordSimilarityService wordSimilarityService;


    public AnalysisCreatedEventHandler() {
        super(RoutingKey.of(Resource.Type.ANALYSIS, Resource.State.CREATED));
    }

    @Override
    public void handle(Event event) {
        LOG.info("Analysis created event received: " + event);
        try{
            Analysis analysis = event.to(Analysis.class);

            switch(analysis.getType().toLowerCase()){
                //TODO this value should be an enumerate
                case "topic-model": topicSimilarityService.calculate(analysis);
                    break;
                case "word-embedding": wordSimilarityService.calculate(analysis);
                    break;
            }

        } catch (Exception e){
            LOG.error("Error calculating similarity for analysis: " + event, e);
        }
    }
}