package org.epnoi.modeler.executor;

import org.epnoi.storage.model.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cbadenes on 11/01/16.
 */
public class ModelingTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ModelingTask.class);

    private final Domain domain;

    public ModelingTask(Domain domain){
        this.domain = domain;
    }


    @Override
    public void run() {
        LOG.info("Building a topic model for domain: " + domain);
    }
}
