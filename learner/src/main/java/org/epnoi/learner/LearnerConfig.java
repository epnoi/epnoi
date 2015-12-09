package org.epnoi.learner;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.epnoi.learner.relations.corpus.RelationalSentencesCorpusCreationParameters;
import org.epnoi.learner.relations.patterns.PatternsConstants;
import org.epnoi.learner.relations.patterns.RelationalPatternsModelCreationParameters;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.modules.*;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.Profile;

import java.util.logging.Logger;

@Configuration
@Import(org.epnoi.EpnoiConfig.class)
//@ComponentScan(basePackageClasses = {RelationalPatternsModelCreationParameters.class, LearnerImpl.class})
@ComponentScan(basePackages = {"org.epnoi.learner"})
@PropertySource("classpath:/epnoi.properties")

public class LearnerConfig {
    private static final Logger logger = Logger.getLogger(LearnerConfig.class
            .getName());

    public static final String EPNOI_PROPERTIES = "epnoi.configurable.properties";
    public static final String EPNOI_PROPERTIES_PATH = "epnoi.configurable.properties.configurationFilePath";


    @Bean
    @Profile(Profiles.DEVELOP)
    public RelationalPatternsModelCreationParameters syntacticPatternsModelCreationParameters() {
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

    @Bean
    @Profile(Profiles.DEVELOP)
    public RelationalPatternsModelCreationParameters lexicalPatternsModelCreationParameters() {
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


    @Bean
    @Profile(Profiles.DEVELOP)
    public RelationalSentencesCorpusCreationParameters relationalSentencesCorpusParameters() {
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

        parameters.setParameter(RelationalSentencesCorpusCreationParameters.THRIFT_PORT, 8585);
        parameters.setParameter(RelationalSentencesCorpusCreationParameters.REST_PORT, 8082);
        return parameters;
    }

    @Bean
    @Profile(Profiles.DEVELOP)
    public LearningParameters learningParameters() {
        LearningParameters learningParameters = new LearningParameters();
    //    System.out.println("=======================================================================================> bean");
/*
    learningParameters.setParameter(
            LearningParameters.CONSIDERED_DOMAINS,
            consideredDomains);

    learningParameters.setParameter(
            LearningParameters.TARGET_DOMAIN, targetDomain);
    learningParameters
            .setParameter(
                    LearningParameters.HYPERNYM_RELATION_EXPANSION_THRESHOLD,
                    hyperymExpansionMinimumThreshold);

    learningParameters
            .setParameter(
                    LearningParameters.HYPERNYM_RELATION_EXTRACTION_THRESHOLD,
                    hypernymExtractionMinimumThresohold);
    learningParameters.setParameter(
            LearningParameters.EXTRACT_TERMS, extractTerms);
    learningParameters.setParameter(
            LearningParameters.NUMBER_INITIAL_TERMS,
            numberInitialTerms);

    learningParameters.setParameter(
            LearningParameters.HYPERNYM_MODEL_PATH,
            hypernymsModelPath);
            */
        learningParameters.setParameter(LearningParameters.CONSIDER_KNOWLEDGE_BASE, false);

        return learningParameters;
    }

    @Profile({Profiles.DEVELOP, Profiles.DEPLOYMENT})
    @Bean
    @Scope(BeanDefinition.SCOPE_SINGLETON)
    public ApiListingResource apiListingResource() {
        return new ApiListingResource();
    }

    @Profile({Profiles.DEVELOP, Profiles.DEPLOYMENT})
    @Bean
    @Scope(BeanDefinition.SCOPE_SINGLETON)
    public SwaggerSerializers swaggerSerializer() {
        return new SwaggerSerializers();
    }

    @Profile({Profiles.DEVELOP, Profiles.DEPLOYMENT})
    @Bean()
    @Scope(BeanDefinition.SCOPE_SINGLETON)
    public BeanConfig beanConfig() {


        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.2");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8082/learner/rest");
        beanConfig.setBasePath("/");
        beanConfig.setResourcePackage("org.epnoi.learner.service.rest");
        beanConfig.setScan(true);
        return beanConfig;
    }


}
