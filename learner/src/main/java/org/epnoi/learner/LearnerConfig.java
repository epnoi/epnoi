package org.epnoi.learner;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.epnoi.learner.relations.corpus.RelationalSentencesCorpusCreationParameters;
import org.epnoi.learner.relations.patterns.PatternsConstants;
import org.epnoi.learner.relations.patterns.RelationalPatternsModelCreationParameters;
import org.epnoi.learner.relations.patterns.RelationalPatternsModelCreator;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.modules.Profiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.*;

import java.util.logging.Logger;

@Configuration
@Import(org.epnoi.EpnoiConfig.class)
//@ComponentScan(basePackageClasses = {RelationalPatternsModelCreationParameters.class, LearnerImpl.class})
@ComponentScan(basePackages = {"org.epnoi.learner"})
//@PropertySource("classpath:/epnoi.properties")
@PropertySources({
        @PropertySource("classpath:/epnoi.properties"),
        @PropertySource("classpath:/learner.properties")
})
public class LearnerConfig {
    private static final Logger logger = Logger.getLogger(LearnerConfig.class
            .getName());

    @Autowired
    private Core core;

    @Autowired
    @Qualifier("lexicalPatternsModelCreationParameters")
    private RelationalPatternsModelCreationParameters lexicalPatternsModelCreationParameters;

    @Autowired
    @Qualifier("syntacticPatternsModelCreationParameters")
    private RelationalPatternsModelCreationParameters syntacticPatternsModelCreationParameters;

    @Bean
    @Profile(Profiles.DEVELOP)
    public RelationalPatternsModelCreationParameters syntacticPatternsModelCreationParameters() {

        RelationalPatternsModelCreationParameters parameters = new RelationalPatternsModelCreationParameters();
        parameters
                .setParameter(
                        RelationalPatternsModelCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI,
                        "http://drInventor.eu/reviews/second/relationalSentencesCorpus");
        parameters
                .setParameter(
                        RelationalPatternsModelCreationParameters.MAX_PATTERN_LENGTH,
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
    public RelationalPatternsModelCreationParameters lexicalPatternsModelCreationParameters(
            @Value("${learner.corpus.patterns.lexical.path}") String path,
            @Value("${learner.corpus.patterns.lexical.uri}") String uri,
            @Value("${learner.corpus.patterns.lexical.maxlength}") Integer maxLength,
            @Value("${learner.corpus.patterns.lexical.store}") Boolean store,
            @Value("${learner.corpus.patterns.lexical.verbose}") Boolean verbose,
            @Value("${learner.corpus.patterns.lexical.test}") Boolean test

    ) {
        RelationalPatternsModelCreationParameters parameters = new RelationalPatternsModelCreationParameters();
        parameters
                .setParameter(
                        RelationalPatternsModelCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI,
                       uri);
        parameters
                .setParameter(
                        RelationalPatternsModelCreationParameters.MAX_PATTERN_LENGTH,
                        maxLength);

        parameters.setParameter(
                RelationalPatternsModelCreationParameters.MODEL_PATH, path);
        parameters.setParameter(RelationalPatternsModelCreationParameters.TYPE,
                PatternsConstants.LEXICAL);

        parameters.setParameter(
                RelationalPatternsModelCreationParameters.STORE, store);

        parameters.setParameter(
                RelationalPatternsModelCreationParameters.VERBOSE, verbose);

        parameters.setParameter(RelationalPatternsModelCreationParameters.TEST,
                test);
        return parameters;
    }

    @Bean
    @Profile(Profiles.DEVELOP)
    public RelationalPatternsModelCreator lexicalPatternsModelCreator() {


        RelationalPatternsModelCreator relationalPatternsModelCreator = new RelationalPatternsModelCreator();


        try {
            relationalPatternsModelCreator.init(core,lexicalPatternsModelCreationParameters);
        } catch (EpnoiInitializationException e) {
            e.printStackTrace();
        }

        return relationalPatternsModelCreator;
    }

    @Bean
    @Profile(Profiles.DEVELOP)
    public RelationalPatternsModelCreator syntacticPatternsModelCreator() {
        RelationalPatternsModelCreator relationalPatternsModelCreator = new RelationalPatternsModelCreator();

        try {
            relationalPatternsModelCreator.init(core, syntacticPatternsModelCreationParameters);
        } catch (EpnoiInitializationException e) {
            e.printStackTrace();
        }

        return relationalPatternsModelCreator;
    }


    @Bean
    @Profile(Profiles.DEVELOP)
    public RelationalSentencesCorpusCreationParameters relationalSentencesCorpusParameters(
            @Value("${learner.corpus.sentences.uri}") String uri,
            @Value("${learner.corpus.sentences.description}") String description,
            @Value("${learner.corpus.sentences.type}") String type,
            @Value("${learner.corpus.sentences.maxlength}") Integer maxLength,
            @Value("${learner.corpus.sentences.store}") Boolean store,
            @Value("${learner.corpus.sentences.verbose}") Boolean verbose,
            @Value("${learner.corpus.sentences.thrift.port}") Integer thriftPort) {


        //logger.info("Starting the Relation Sentences Corpus Creator");


        RelationalSentencesCorpusCreationParameters parameters = new RelationalSentencesCorpusCreationParameters();

        // String relationalCorpusURI = "http://drInventor.eu/reviews/second/relationalSentencesCorpus";

        parameters.setParameter(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI,
                uri);

        parameters.setParameter(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_TYPE,
                type);

        parameters.setParameter(
                RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_DESCRIPTION,
                "DrInventor second relational sentences corpus");


        parameters.setParameter(RelationalSentencesCorpusCreationParameters.MAX_SENTENCE_LENGTH, maxLength);

        parameters.setParameter(RelationalSentencesCorpusCreationParameters.STORE, store);

        parameters.setParameter(RelationalSentencesCorpusCreationParameters.VERBOSE, verbose);

        parameters.setParameter(RelationalSentencesCorpusCreationParameters.THRIFT_PORT, thriftPort);
        //parameters.setParameter(RelationalSentencesCorpusCreationParameters.REST_PORT, 8082);
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


    @Bean
    @Scope(BeanDefinition.SCOPE_SINGLETON)
    public ApiListingResource apiListingResource() {
        return new ApiListingResource();
    }


    @Bean
    @Scope(BeanDefinition.SCOPE_SINGLETON)
    public SwaggerSerializers swaggerSerializer() {
        return new SwaggerSerializers();
    }


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

    @Bean()
    public SparkConf sparkConfig(@Value("${epnoi.learner.spark.master}") String master,
                                 @Value("${epnoi.learner.spark.app}") String appName) {

        SparkConf sparkConf = new SparkConf().setMaster(master).setAppName(appName);
        logger.info("Creating the following spark configuration " + sparkConf.getAll());

        return sparkConf;
    }


    @Bean()
    public JavaSparkContext sparkContext(SparkConf sparkConf) {
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
        return sparkContext;
    }
}