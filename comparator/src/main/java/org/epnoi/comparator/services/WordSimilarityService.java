package org.epnoi.comparator.services;

import org.epnoi.storage.model.Analysis;
import org.epnoi.storage.model.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by cbadenes on 13/01/16.
 */
@Component
public class WordSimilarityService {

    private static final Logger LOG = LoggerFactory.getLogger(WordSimilarityService.class);


    public void calculate(Analysis analysis){
        LOG.info("Ready to calculate vector similarity based on W2V from Analysis: " + analysis);


    }

    public String create(Domain domain){
        throw new RuntimeException("Method not implemented yet");
    }

    public List<Analysis> list(){
        throw new RuntimeException("Method not implemented yet");
    }

    public Analysis get(String uri){
        throw new RuntimeException("Method not implemented yet");
    }

    public Analysis remove(String uri){
        throw new RuntimeException("Method not implemented yet");
    }

    public Analysis update(String uri, Analysis analysis){
        throw new RuntimeException("Method not implemented yet");
    }

}
