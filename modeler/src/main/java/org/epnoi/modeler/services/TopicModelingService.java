package org.epnoi.modeler.services;

import org.epnoi.modeler.executor.ModelingExecutor;
import org.epnoi.storage.model.Analysis;
import org.epnoi.storage.model.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

/**
 * Created by cbadenes on 11/01/16.
 */
@Component
public class TopicModelingService {

    private static final Logger LOG = LoggerFactory.getLogger(TopicModelingService.class);

    private ConcurrentHashMap<String,ModelingExecutor> executors;

    @Value("${epnoi.modeler.delay}")
    protected Long delay;

    @PostConstruct
    public void setup(){
        this.executors = new ConcurrentHashMap<>();
    }


    public void scheduleModeling(Domain domain){
        LOG.info("Scheduling a new topic model for domain: " + domain);
        this.executors.merge(domain.getUri(), new ModelingExecutor(domain, delay), (modelingExecutor, modelingExecutor2) -> modelingExecutor.buildModel());

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
