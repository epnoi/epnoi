package org.epnoi.learner.relations.patterns.lexical;

import org.epnoi.learner.relations.corpus.RelationalSentencesCorpusCreationParameters;
import org.epnoi.learner.relations.patterns.*;
import org.epnoi.model.RelationalSentencesCorpus;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.core.CoreUtility;

import java.util.logging.Logger;

public class LexicalRelationalModelTester {
	private static final Logger logger = Logger
			.getLogger(LexicalRelationalModelTester.class.getName());
	private RelationalPatternsModelCreationParameters parameters;
	private Core core;
	private RelationalPatternsCorpusCreator patternsCorpusCreator;
	private RelationalPatternsCorpus patternsCorpus;
	private RelationalPatternsModelSerializer modelSerializer;
	private String relationalSentencesCorpusURI;
	private BigramSoftPatternModel model;
	private boolean store;
	private boolean verbose;
	private boolean test;
	private String path;

	// ----------------------------------------------------------------------------------------------------------------

	public void init(Core core,
			RelationalPatternsModelCreationParameters parameters)
			throws EpnoiInitializationException {
		logger.info("Initializing the LexicalRelationalModelTester with the following parameters");
		logger.info(parameters.toString());
		this.core = core;
		this.parameters = parameters;

		relationalSentencesCorpusURI = (String) this.parameters
				.getParameterValue(RelationalPatternsModelCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI);

		this.path = (String)this.parameters
				.getParameterValue(RelationalPatternsModelCreationParameters.MODEL_PATH);

		this.patternsCorpusCreator = new RelationalPatternsCorpusCreator();
		this.patternsCorpusCreator.init(core,
				new LexicalRelationalPatternGenerator());

		try {
			this.model = (BigramSoftPatternModel) RelationalPatternsModelSerializer
					.deserialize(this.path);
		} catch (EpnoiResourceAccessException e) {

			throw new EpnoiInitializationException(
					"The model couldn't not be retrieved at " + this.path);
		}

		this.path = (String) parameters
				.getParameterValue(RelationalPatternsModelCreationParameters.MODEL_PATH);

		this.verbose = (boolean) parameters
				.getParameterValue(RelationalSentencesCorpusCreationParameters.VERBOSE);

	}

	// ----------------------------------------------------------------------------------------------------------------

	public void test() {

		createPatternsModel();

		testPatternsModel();
	}

	private void testPatternsModel() {
		double averageProbability = 0;
		long startingTime = System.currentTimeMillis();
		logger.info("Testing all the patterns against the model");
		for (RelationalPattern pattern : patternsCorpus.getPatterns()) {
			double patternProbability = this.model
					.calculatePatternProbability(pattern);
			System.out.println("> " + patternProbability);
			averageProbability += patternProbability;
		}
		long totalTime = startingTime - System.currentTimeMillis();
		logger.info("It took " + Math.abs(totalTime) + " ms to test the model");

		logger.info("The average probability is " + averageProbability
				/ patternsCorpus.getPatterns().size());

	}

	// ----------------------------------------------------------------------------------------------------------------

	public void createPatternsModel() {
		RelationalSentencesCorpus relationalSentencesCorpus = (RelationalSentencesCorpus) this.core
				.getInformationHandler().get(relationalSentencesCorpusURI,
						RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS);

		if (relationalSentencesCorpus != null) {
			logger.info("The relational sentences  has "
					+ relationalSentencesCorpus.getSentences().size()
					+ " sentences");
			patternsCorpus = patternsCorpusCreator
					.buildCorpus(relationalSentencesCorpus);

			logger.info("The RelationalPatternsCorpus has "
					+ patternsCorpus.getPatterns().size() + " patterns");
		} else {
			logger.severe("The relational sentences corpus with URI "
					+ this.relationalSentencesCorpusURI
					+ " couldn't not be retrieved");
		}
	}

	// ----------------------------------------------------------------------------------------------------------------

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

		parameters
				.setParameter(
						RelationalPatternsModelCreationParameters.MODEL_PATH,
						"/opt/epnoi/epnoideployment/firstReviewResources/lexicalModel/model.bin");

		parameters.setParameter(
				RelationalSentencesCorpusCreationParameters.STORE, true);

		parameters.setParameter(
				RelationalSentencesCorpusCreationParameters.VERBOSE, false);

		Core core = CoreUtility.getUIACore();

		LexicalRelationalModelTester modelTester = new LexicalRelationalModelTester();
		try {
			modelTester.init(core, parameters);
		} catch (EpnoiInitializationException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		modelTester.test();

		logger.info("Ending the Lexical Relational Model creation");
	}

	// ----------------------------------------------------------------------------------------------------------------

}
