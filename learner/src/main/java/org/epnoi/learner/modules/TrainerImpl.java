package org.epnoi.learner.modules;

import org.epnoi.learner.LearnerConfig;
import org.epnoi.learner.relations.corpus.RelationalSentencesCorpusCreationParameters;
import org.epnoi.learner.relations.corpus.parallel.RelationalSentencesCorpusCreator;
import org.epnoi.learner.relations.patterns.RelationalPatternsModelCreationParameters;
import org.epnoi.learner.relations.patterns.RelationalPatternsModelCreator;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.Core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Autowired
    RelationalSentencesCorpusCreationParameters relationalSentencesCorpusCreatorParameters;

    @Autowired
    @Qualifier("lexicalPatternsModelCreationParameters")
    RelationalPatternsModelCreationParameters lexicalPatternsModelParameters;

    @Autowired
    RelationalSentencesCorpusCreator relationalSentencesCorpusCreator;


    @PostConstruct
    public void init() throws EpnoiInitializationException {
        logger.info("Initializing the Trainer");

    }

    @Override
    public void createRelationalSentencesCorpus() {
        logger.info("Creating the relational sentences corpus");
        try {
            this.relationalSentencesCorpusCreator.createCorpus();
        } catch (Exception e) {
            logger.severe("There was a problem creating the relational sentences corpus");
            e.printStackTrace();
        }
    }

    @Override
    public void createRelationalPatternsModel() {

        RelationalPatternsModelCreator relationalPatternsModelCreator = new RelationalPatternsModelCreator();
        try {
            relationalPatternsModelCreator.init(core, lexicalPatternsModelParameters);
        } catch (Exception e) {
            logger.severe("There was a problem in the creation of the relational pattern model");
            e.printStackTrace();

        }
    }
@Override
    public RelationalSentencesCorpusCreationParameters getRelationalSentencesCorpusCreationParameters() {
        return this.relationalSentencesCorpusCreatorParameters;
    }

    public RelationalPatternsModelCreationParameters getRelationalPatternsModelCreationParameters() {
        return this.lexicalPatternsModelParameters;
    }
}
