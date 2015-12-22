package org.epnoi.learner.modules;

import org.epnoi.learner.relations.corpus.RelationalSentencesCorpusCreationParameters;
import org.epnoi.learner.relations.patterns.RelationalPatternsModelCreationParameters;
import org.epnoi.model.commons.Parameters;

/**
 * Created by rgonza on 14/11/15.
 */
public interface Trainer {

    void createRelationalSentencesCorpus(Parameters<Object> runtimeParameters);

    void createRelationalPatternsModel(Parameters<Object> runtimeParameters);

    RelationalSentencesCorpusCreationParameters getRelationalSentencesCorpusCreationParameters();

    RelationalPatternsModelCreationParameters getRelationalPatternsModelCreationParameters();

    Parameters<Object> getRuntimeParameters();
}
