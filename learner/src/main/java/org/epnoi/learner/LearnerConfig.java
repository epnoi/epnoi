package org.epnoi.learner;

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


    @Bean
    @Profile(DEVELOP_PROFILE)
    public RelationalPatternsModelCreationParameters developRelationalPatternsModelCreationParametersModel() {
        return new RelationalPatternsModelCreationParameters();
    }

}
