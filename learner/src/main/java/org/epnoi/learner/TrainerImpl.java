package org.epnoi.learner;

import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.Core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

/**
 * Created by rgonza on 14/11/15.
 */
@Component
public class TrainerImpl implements Trainer {
    private static final Logger logger = Logger.getLogger(LearnerConfig.class
            .getName());
    @Autowired
    private Core core;

    @PostConstruct
    public void init() throws EpnoiInitializationException {
        logger.info("Initializing the Trainer");
    }
}
