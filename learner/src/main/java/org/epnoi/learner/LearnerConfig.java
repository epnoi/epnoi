package org.epnoi.learner;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.epnoi.learner.filesystem.FilesystemHarvesterParameters;
import org.epnoi.learner.relations.corpus.RelationalSentencesCorpusCreationParameters;
import org.epnoi.learner.relations.patterns.*;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
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
@ComponentScan(basePackages = {"org.epnoi.learner",})
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
            @Value("${learner.corpus.patterns.lexical.interpolation}") Double interpolationConstant,
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


        parameters.setParameter(RelationalPatternsModelCreationParameters.INTERPOLATION_CONSTANT, interpolationConstant);

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
            relationalPatternsModelCreator.init(core, lexicalPatternsModelCreationParameters);
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
                description);


        parameters.setParameter(RelationalSentencesCorpusCreationParameters.MAX_SENTENCE_LENGTH, maxLength);

        parameters.setParameter(RelationalSentencesCorpusCreationParameters.STORE, store);

        parameters.setParameter(RelationalSentencesCorpusCreationParameters.VERBOSE, verbose);

        parameters.setParameter(RelationalSentencesCorpusCreationParameters.THRIFT_PORT, thriftPort);
        //parameters.setParameter(RelationalSentencesCorpusCreationParameters.REST_PORT, 8082);
        return parameters;
    }

    @Bean
    @Profile(Profiles.DEVELOP)
    public LearningParameters learningParameters(
            @Value("${learner.task.terms}") Boolean obtainTerms,
            @Value("${learner.task.terms.extract}") Boolean extractTerms,
            @Value("${learner.task.terms.store}") Boolean storeTerms,
            @Value("${learner.task.terms.initialterms}") Integer numberInitialTerms,
            @Value("${learner.task.relations}") Boolean obtainRelations,
            @Value("${learner.task.relations.extract}") Boolean extractRelations,
            @Value("${learner.task.relations.store}") Boolean storeRelations,
            @Value("${learner.task.relations.parallel}") Boolean parallelRelations,
            @Value("${learner.task.relations.maxdistance}") Integer maxSourceTargetDistance,
            @Value("${learner.task.relations.hypernyms.lexical.path}") String hypernymsLexicalModelPath,
            @Value("${learner.task.relations.hypernyms.threshold.expansion}") Double hyperymExpansionMinimumThreshold,
            @Value("${learner.task.relations.hypernyms.threshold.extraction}") Double hypernymExtractionMinimumThresohold,
            @Value("${learner.task.relations.thrift.port}") Integer thriftPort
            ) {
        LearningParameters learningParameters = new LearningParameters();
        //    System.out.println("=======================================================================================> bean");
/*
    learningParameters.setParameter(
            LearningParameters.CONSIDERED_DOMAINS,
            consideredDomains);

    learningParameters.setParameter(
            LearningParameters.TARGET_DOMAIN_URI, targetDomain);
            */

//Term related parameters
        learningParameters.setParameter(LearningParameters.OBTAIN_TERMS, obtainTerms);
        learningParameters.setParameter(
                LearningParameters.EXTRACT_TERMS, extractTerms);
        learningParameters.setParameter(
                LearningParameters.STORE_TERMS, storeTerms);
        learningParameters.setParameter(
                LearningParameters.NUMBER_INITIAL_TERMS,
                numberInitialTerms);

        //Relation related parameters
        learningParameters.setParameter(LearningParameters.OBTAIN_RELATIONS, obtainRelations);
        learningParameters.setParameter(LearningParameters.EXTRACT_RELATIONS, extractRelations);
        learningParameters.setParameter(LearningParameters.EXTRACT_RELATIONS_PARALLEL, parallelRelations);
        learningParameters.setParameter(LearningParameters.STORE_RELATIONS, storeRelations);
        learningParameters
                .setParameter(
                        LearningParameters.HYPERNYM_RELATION_EXPANSION_THRESHOLD,
                        hyperymExpansionMinimumThreshold);

        learningParameters
                .setParameter(
                        LearningParameters.HYPERNYM_RELATION_EXTRACTION_THRESHOLD,
                        hypernymExtractionMinimumThresohold);


        learningParameters.setParameter(
                LearningParameters.HYPERNYM_MODEL_PATH,
                hypernymsLexicalModelPath);


        learningParameters.setParameter(LearningParameters.MAX_SOURCE_TARGET_DISTANCE, maxSourceTargetDistance);

        try {


            RelationalPatternsModel softPatternModel = RelationalPatternsModelSerializer
                    .deserialize(hypernymsLexicalModelPath);
            learningParameters.setParameter(LearningParameters.HYPERNYM_MODEL, softPatternModel);

        } catch (EpnoiResourceAccessException e) {
            logger.severe(e.getMessage());
        }

        learningParameters.setParameter(LearningParameters.CONSIDER_KNOWLEDGE_BASE, false);
        learningParameters.setParameter(RelationalSentencesCorpusCreationParameters.THRIFT_PORT, thriftPort);
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


    @Bean
    public FilesystemHarvesterParameters filesystemHarvesterParameters(
            @Value("${learner.demo.harvester.uri}") String uri,
            @Value("${learner.demo.harvester.label}") String label,
            @Value("${learner.demo.harvester.overwrite}") Boolean overwrite,
            @Value("${learner.demo.harvester.verbose}") Boolean verbose,
            @Value("${learner.demo.harvester.path}") String path
    ) {
        FilesystemHarvesterParameters parameters = new FilesystemHarvesterParameters();

        parameters.setParameter(FilesystemHarvesterParameters.CORPUS_LABEL, label);

        parameters.setParameter(FilesystemHarvesterParameters.CORPUS_URI, uri);
        parameters.setParameter(FilesystemHarvesterParameters.VERBOSE, verbose);

        parameters.setParameter(FilesystemHarvesterParameters.OVERWRITE, overwrite);

        parameters.setParameter(FilesystemHarvesterParameters.FILEPATH, path);
        return parameters;

    }

}