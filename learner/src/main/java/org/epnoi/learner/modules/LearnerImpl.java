package org.epnoi.learner.modules;

import org.epnoi.learner.LearnerConfig;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.Core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

/**
 * Created by rgonza on 13/11/15.
 */
@Component
public class LearnerImpl implements Learner {

    private static final Logger logger = Logger.getLogger(LearnerConfig.class
            .getName());
    @Autowired
    private Core core;

    @Autowired
    Trainer trainer;

    @PostConstruct
    public void init() throws EpnoiInitializationException {
        logger.info("Initializing the Learner");
    }

}
