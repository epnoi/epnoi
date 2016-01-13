package org.epnoi.modeler.services;

import org.epnoi.modeler.executor.TopicModelingExecutor;
import org.epnoi.modeler.executor.WordEmbeddingExecutor;
import org.epnoi.modeler.helper.ModelingHelper;
import org.epnoi.storage.model.Analysis;
import org.epnoi.storage.model.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cbadenes on 11/01/16.
 */
@Component
public class WordEmbeddingService {

    private static final Logger LOG = LoggerFactory.getLogger(WordEmbeddingService.class);

    private ConcurrentHashMap<String,WordEmbeddingExecutor> executors;

    @Value("${epnoi.modeler.delay}")
    protected Long delay;

    @Autowired
    ModelingHelper helper;

    @PostConstruct
    public void setup(){
        this.executors = new ConcurrentHashMap<>();
    }


    public void buildModel(Domain domain){
        LOG.info("Plan a new build task to create topic models for domain: " + domain);
        this.executors.merge(domain.getUri(), new WordEmbeddingExecutor(domain,helper,delay), (modelingExecutor, modelingExecutor2) -> modelingExecutor.buildModel());

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
