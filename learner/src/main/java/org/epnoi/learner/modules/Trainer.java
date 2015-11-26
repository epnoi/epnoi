package org.epnoi.learner.modules;

import org.epnoi.learner.relations.corpus.RelationalSentencesCorpusCreationParameters;
import org.epnoi.learner.relations.corpus.parallel.RelationalSentencesCorpusCreator;
import org.epnoi.learner.relations.patterns.RelationalPatternsModelCreationParameters;

/**
 * Created by rgonza on 14/11/15.
 */
public interface Trainer {

    void createRelationalSentencesCorpus();

    void createRelationalPatternsModel();

   RelationalSentencesCorpusCreationParameters getRelationalSentencesCorpusCreationParameters() ;

   RelationalPatternsModelCreationParameters getRelationalPatternsModelCreationParameters();
}
