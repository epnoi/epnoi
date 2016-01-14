package org.epnoi.modeler.services;

import org.epnoi.modeler.executor.ModelingPoolExecutor;
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
public class ModelingService {

    private static final Logger LOG = LoggerFactory.getLogger(ModelingService.class);

    private ConcurrentHashMap<String,ModelingPoolExecutor> executors;

    @Value("${epnoi.modeler.delay}")
    protected Long delay;

    @Autowired
    ModelingHelper helper;

    @PostConstruct
    public void setup(){
        this.executors = new ConcurrentHashMap<>();
    }


    public void buildModels(Domain domain){
        LOG.info("Plan a new build task to create topic models for domain: " + domain);
        this.executors.merge(domain.getUri(), new ModelingPoolExecutor(domain,helper,delay).buildModel(), (modelingExecutor, modelingExecutor2) -> modelingExecutor.buildModel());

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
