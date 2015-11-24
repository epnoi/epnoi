package org.epnoi.learner.modules;

import org.epnoi.learner.LearningParameters;
import org.epnoi.learner.terms.TermsTable;
import org.epnoi.model.RelationsTable;

/**
 * Created by rgonza on 14/11/15.
 */
public interface Learner {
    Trainer getTrainer();

    LearningParameters getParameters();

    void learn(String domainUri);

    RelationsTable retrieveRelations(String domainUri);

    TermsTable retrieveTerminology(String domainUri);
}
