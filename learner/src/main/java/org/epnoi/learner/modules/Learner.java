package org.epnoi.learner.modules;

import org.epnoi.learner.LearningParameters;

/**
 * Created by rgonza on 14/11/15.
 */
public interface Learner {
    Trainer getTrainer();
    LearningParameters getParameters();
    void learn(String domainUri);
}
