package org.epnoi.learner;

import org.epnoi.learner.relations.patterns.PatternsConstants;
import org.epnoi.learner.relations.patterns.RelationalPatternsModelCreationParameters;
import org.springframework.context.annotation.*;

import java.util.logging.Logger;

@Configuration
@Import(org.epnoi.EpnoiConfig.class)
@ComponentScan(basePackageClasses = {RelationalPatternsModelCreationParameters.class})
@PropertySource("classpath:/epnoi.properties")

public class LearnerConfig {
    private static final Logger logger = Logger.getLogger(LearnerConfig.class
            .getName());
    public static final String DEPLOY_PROFILE = "deploy";
    public static final String DEVELOP_PROFILE = "develop";
    public static final String EPNOI_PROPERTIES = "epnoi.configurable.properties";
    public static final String EPNOI_PROPERTIES_PATH = "epnoi.configurable.properties.configurationFilePath";


    @Bean(name = "relationalPatternsModelCreationParameters")
    @Profile(DEVELOP_PROFILE)
    public RelationalPatternsModelCreationParameters developRelationalPatternsModelCreationParametersModel() {
        RelationalPatternsModelCreationParameters parameters = new RelationalPatternsModelCreationParameters();
        parameters
                .setParameter(
                        RelationalPatternsModelCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER,
                        "http://drInventorFirstReview/relationalSentencesCorpus");
        parameters
                .setParameter(
                        RelationalPatternsModelCreationParameters.MAX_PATTERN_LENGTH_PARAMETER,
                        20);

        parameters.setParameter(
                RelationalPatternsModelCreationParameters.MODEL_PATH,
                "/JUNK/syntacticModel.bin");
        parameters.setParameter(RelationalPatternsModelCreationParameters.TYPE,
                PatternsConstants.SYNTACTIC);

        parameters.setParameter(
                RelationalPatternsModelCreationParameters.STORE, false);

        parameters.setParameter(
                RelationalPatternsModelCreationParameters.VERBOSE, true);

        parameters.setParameter(RelationalPatternsModelCreationParameters.TEST,
                true);
        return parameters;
    }

}
