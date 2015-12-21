package org.epnoi.learner.relations.patterns.lexical;

import org.epnoi.learner.relations.corpus.RelationalSentencesCorpusCreationParameters;
import org.epnoi.learner.relations.patterns.*;
import org.epnoi.model.RelationalSentencesCorpus;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

public class LexicalRelationalModelCreator {
    private static final Logger logger = Logger
            .getLogger(LexicalRelationalModelCreator.class.getName());

    private RelationalPatternsModelCreationParameters parameters;

    private Core core;

    private RelationalPatternsCorpusCreator patternsCorpusCreator;
    private RelationalPatternsCorpus patternsCorpus;
    private RelaxedBigramSoftPatternModelBuilder modelBuilder;
    private RelaxedBigramSoftPatternModel model;
    private boolean store;
    private boolean verbose;
    private boolean test;
    private double interpolationConstant;
    private String path;

    // ----------------------------------------------------------------------------------------------------------------
    @PostConstruct
    public void init() throws EpnoiInitializationException {
        logger.info("Initializing the LexicalRealationalModelCreator with the following parameters");
        logger.info(parameters.toString());
        this.core = core;
        this.parameters = parameters;
        String relationalSentencesCorpusURI = (String) this.parameters
                .getParameterValue(RelationalPatternsModelCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI);
        this.patternsCorpusCreator = new RelationalPatternsCorpusCreator();
        this.patternsCorpusCreator.init(core,
                new LexicalRelationalPatternGenerator());

        RelationalSentencesCorpus relationalSentencesCorpus = (RelationalSentencesCorpus) this.core
                .getInformationHandler().get(relationalSentencesCorpusURI,
                        RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS);

        if (relationalSentencesCorpus == null) {
            throw new EpnoiInitializationException(
                    "The Relational Sentences Corpus "
                            + relationalSentencesCorpusURI
                            + "could not be found");

        } else {

            _buildPatternsCorpus(relationalSentencesCorpus);
        }
        modelBuilder = new RelaxedBigramSoftPatternModelBuilder(parameters);

        _readParameters();

    }

    private void _buildPatternsCorpus(RelationalSentencesCorpus relationalSentencesCorpus) {
        logger.info("The RelationalSencentcesCorpus has "
                + relationalSentencesCorpus.getSentences().size()
                + " sentences");
        patternsCorpus = patternsCorpusCreator
                .buildCorpus(relationalSentencesCorpus);

        logger.info("The RelationalPatternsCorpus has "
                + patternsCorpus.getPatterns().size() + " patterns");
    }

    private void _readParameters() {
        this.path = (String) parameters
                .getParameterValue(RelationalPatternsModelCreationParameters.MODEL_PATH);

        this.store = (boolean) parameters
                .getParameterValue(RelationalSentencesCorpusCreationParameters.STORE);

        this.verbose = (boolean) parameters
                .getParameterValue(RelationalSentencesCorpusCreationParameters.VERBOSE);

        if (parameters
                .getParameterValue(RelationalSentencesCorpusCreationParameters.TEST) != null) {

            this.test = ((boolean) parameters
                    .getParameterValue(RelationalSentencesCorpusCreationParameters.TEST));
        } else {
            this.test = false;
        }

        this.interpolationConstant = (double) parameters
                .getParameterValue(RelationalPatternsModelCreationParameters.INTERPOLATION_CONSTANT);
    }

    // ----------------------------------------------------------------------------------------------------------------

    public RelaxedBigramSoftPatternModel buildModel() {
        long startingTime = System.currentTimeMillis();
        logger.info("Adding all the patterns to the model");
        for (RelationalPattern pattern : patternsCorpus.getPatterns()) {
            this.modelBuilder.addPattern(((LexicalRelationalPattern) pattern));
        }

        logger.info("Building the model " + this.modelBuilder);
        RelaxedBigramSoftPatternModel model = this.modelBuilder.build();
        long totalTime = startingTime - System.currentTimeMillis();
        logger.info("It took " + Math.abs(totalTime) + " ms to build the model");
        return model;
    }

    // ----------------------------------------------------------------------------------------------------------------

    public void create() {
        logger.info("Starting the creation of a lexical BigramSoftPatternModel with the following parameters: "
                + this.parameters);
        this.model = buildModel();

        if (this.verbose) {
            this.model.show();
        }
        if (this.store) {
            logger.info("Storing the model at " + path);
            try {
                RelationalPatternsModelSerializer.serialize(path, model);

            } catch (EpnoiResourceAccessException e) {
                logger.severe("There was a problem trying to serialize the BigramSoftPatternModel at "
                        + path);
                logger.severe(e.getMessage());
            }

        }
    }

    // ----------------------------------------------------------------------------------------------------------------
/*
    public static void main(String[] args) {
        logger.info("Starting the Lexical Relational Model creation");
        RelationalPatternsModelCreationParameters parameters = new RelationalPatternsModelCreationParameters();
        parameters
                .setParameter(
                        RelationalPatternsModelCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI,
                        "http://drInventorFirstReview/relationalSentencesCorpus");
        parameters
                .setParameter(
                        RelationalPatternsModelCreationParameters.MAX_PATTERN_LENGTH,
                        20);

        parameters.setParameter(
                RelationalPatternsModelCreationParameters.MODEL_PATH,
                "/home/rgonza/Escritorio/model.bin");

        parameters.setParameter(
                RelationalSentencesCorpusCreationParameters.STORE, true);

        parameters.setParameter(
                RelationalSentencesCorpusCreationParameters.VERBOSE, true);

        parameters
                .setParameter(
                        RelationalPatternsModelCreationParameters.INTERPOLATION_CONSTANT,
                        0.0);

        Core core = CoreUtility.getUIACore();

        LexicalRelationalModelCreator modelCreator = new LexicalRelationalModelCreator();
        try {
            modelCreator.init(core, parameters);
        } catch (EpnoiInitializationException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        modelCreator.create();

        logger.info("Ending the Lexical Relational Model creation");
    }
*/
    // ----------------------------------------------------------------------------------------------------------------

}
