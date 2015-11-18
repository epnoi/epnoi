package org.epnoi.learner;

import org.epnoi.learner.relations.corpus.RelationalSentencesCorpusCreationParameters;
import org.epnoi.learner.relations.patterns.PatternsConstants;
import org.epnoi.learner.relations.patterns.RelationalPatternsModelCreationParameters;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.modules.Core;
import org.epnoi.uia.core.CoreUtility;
import org.springframework.context.annotation.*;

import java.util.logging.Logger;

@Configuration
@Import(org.epnoi.EpnoiConfig.class)
//@ComponentScan(basePackageClasses = {RelationalPatternsModelCreationParameters.class, LearnerImpl.class})
@ComponentScan(basePackages = {"org.epnoi.learner"})
@PropertySource("classpath:/epnoi.properties")

public class LearnerConfig {
    private static final Logger logger = Logger.getLogger(LearnerConfig.class
            .getName());
    public static final String DEPLOY_PROFILE = "deploy";
    public static final String DEVELOP_PROFILE = "develop";
    public static final String EPNOI_PROPERTIES = "epnoi.configurable.properties";
    public static final String EPNOI_PROPERTIES_PATH = "epnoi.configurable.properties.configurationFilePath";


    @Bean()
    @Profile(DEVELOP_PROFILE)
    public RelationalPatternsModelCreationParameters syntacticPatternsModelCreationParametersModel() {
        RelationalPatternsModelCreationParameters parameters = new RelationalPatternsModelCreationParameters();
        parameters
                .setParameter(
                        RelationalPatternsModelCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER,
                        "http://drInventor.eu/reviews/second/relationalSentencesCorpus");
        parameters
                .setParameter(
                        RelationalPatternsModelCreationParameters.MAX_PATTERN_LENGTH_PARAMETER,
                        20);

        parameters.setParameter(
                RelationalPatternsModelCreationParameters.MODEL_PATH,
                "/opt/epnoi/epnoideployment/secondReviewResources/syntacticModel/model.bin");
        parameters.setParameter(RelationalPatternsModelCreationParameters.TYPE,
                PatternsConstants.SYNTACTIC);

        parameters.setParameter(
                RelationalPatternsModelCreationParameters.STORE, false);

        parameters.setParameter(
                RelationalPatternsModelCreationParameters.VERBOSE, true);

        parameters.setParameter(RelationalPatternsModelCreationParameters.TEST,
                true);
        System.out.println("parameters> " + parameters);
        return parameters;
    }

    @Bean()
    @Profile(DEVELOP_PROFILE)
    public RelationalPatternsModelCreationParameters lexicalRelationalPatternsModelCreationParametersModel() {
        RelationalPatternsModelCreationParameters parameters = new RelationalPatternsModelCreationParameters();
        parameters
                .setParameter(
                        RelationalPatternsModelCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER,
                        "http://drInventor.eu/reviews/second/relationalSentencesCorpus");
        parameters
                .setParameter(
                        RelationalPatternsModelCreationParameters.MAX_PATTERN_LENGTH_PARAMETER,
                        20);

        parameters.setParameter(
                RelationalPatternsModelCreationParameters.MODEL_PATH,
                "/opt/epnoi/epnoideployment/secondReviewResources/lexicalModel/model.bin");
        parameters.setParameter(RelationalPatternsModelCreationParameters.TYPE,
                PatternsConstants.LEXICAL);

        parameters.setParameter(
                RelationalPatternsModelCreationParameters.STORE, false);

        parameters.setParameter(
                RelationalPatternsModelCreationParameters.VERBOSE, true);

        parameters.setParameter(RelationalPatternsModelCreationParameters.TEST,
                true);
        return parameters;
    }


    @Bean()
    @Profile(DEVELOP_PROFILE)
    public RelationalSentencesCorpusCreationParameters getRelationalSentencesCorpusParameters() {
        logger.info("Starting the Relation Sentences Corpus Creator");


        RelationalSentencesCorpusCreationParameters parameters = new RelationalSentencesCorpusCreationParameters();

        String relationalCorpusURI = "http://drInventor.eu/reviews/second/relationalSentencesCorpus";

        parameters.setParameter(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER,
                relationalCorpusURI);

        parameters.setParameter(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_TYPE_PARAMETER,
                RelationHelper.HYPERNYM);

        parameters.setParameter(
                RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_DESCRIPTION_PARAMETER,
                "DrInventor second relational sentences corpus");

        parameters.setParameter(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER,
                relationalCorpusURI);

        parameters.setParameter(RelationalSentencesCorpusCreationParameters.MAX_SENTENCE_LENGTH_PARAMETER, 80);

        parameters.setParameter(RelationalSentencesCorpusCreationParameters.STORE, true);

        parameters.setParameter(RelationalSentencesCorpusCreationParameters.VERBOSE, true);

        return parameters;
    }
}
