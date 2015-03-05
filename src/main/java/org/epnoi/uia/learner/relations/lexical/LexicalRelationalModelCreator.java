package org.epnoi.uia.learner.relations.lexical;

import java.util.logging.Logger;

import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.exceptions.EpnoiInitializationException;
import org.epnoi.uia.exceptions.EpnoiResourceAccessException;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.learner.relations.RelationalPattern;
import org.epnoi.uia.learner.relations.RelationalPatternsCorpus;
import org.epnoi.uia.learner.relations.RelationalSentencesCorpus;
import org.epnoi.uia.learner.relations.RelationalSentencesCorpusCreationParameters;
import org.epnoi.uia.learner.relations.RelationalSentencesCorpusCreator;

public class LexicalRelationalModelCreator {
	private static final Logger logger = Logger
			.getLogger(LexicalRelationalModelCreator.class.getName());
	private LexicalRelationalModelCreationParameters parameters;
	private Core core;
	private LexicalRelationalPatternsCorpusCreator patternsCorpusCreator;
	private RelationalPatternsCorpus patternsCorpus;
	private BigramSoftPatternModelBuilder modelBuilder;
	private BigramSoftPatternModel model;
	private boolean store;
	private boolean verbose;
	private String path;

	// ----------------------------------------------------------------------------------------------------------------

	public void init(Core core,
			LexicalRelationalModelCreationParameters parameters)
			throws EpnoiInitializationException {
		logger.info("Initializing the LexicalRealationalModelCreator with the following parameters");
		logger.info(parameters.toString());
		this.core = core;
		this.parameters = parameters;
		String relationalSentencesCorpusURI = (String) this.parameters
				.getParameterValue(LexicalRelationalModelCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER);
		this.patternsCorpusCreator = new LexicalRelationalPatternsCorpusCreator();
		this.patternsCorpusCreator.init(core);

		RelationalSentencesCorpus relationalSentencesCorpus = (RelationalSentencesCorpus) this.core
				.getInformationHandler().get(relationalSentencesCorpusURI,
						RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS);

		if (relationalSentencesCorpus == null) {
			throw new EpnoiInitializationException(
					"The Relational Sentences Corpus "
							+ relationalSentencesCorpusURI
							+ "could not be found");

		} else {

			logger.info("The RelationalSencentcesCorpus has "
					+ relationalSentencesCorpus.getSentences().size()
					+ " sentences");
			patternsCorpus = patternsCorpusCreator
					.buildCorpus(relationalSentencesCorpus);

			logger.info("The RelationalPatternsCorpus has "
					+ patternsCorpus.getPatterns().size() + " patterns");
		}
		modelBuilder = new BigramSoftPatternModelBuilder(parameters);

		this.path = (String) parameters
				.getParameterValue(LexicalRelationalModelCreationParameters.MODEL_PATH_PARAMETERS);

		this.store = (boolean) parameters
				.getParameterValue(RelationalSentencesCorpusCreationParameters.STORE_RESULT_PARAMETER);

		this.verbose = (boolean) parameters
				.getParameterValue(RelationalSentencesCorpusCreationParameters.VERBOSE_PARAMETER);
	}

	// ----------------------------------------------------------------------------------------------------------------

	public BigramSoftPatternModel buildModel() {
		logger.info("Adding all the patterns to the model");
		for (RelationalPattern pattern : patternsCorpus.getPatterns()) {
			this.modelBuilder.addPattern(((LexicalRelationalPattern) pattern));
		}

		return this.modelBuilder.build();
	}

	// ----------------------------------------------------------------------------------------------------------------

	public void create() {
		this.model = buildModel();

		if (this.verbose) {
			this.model.show();
		}
		if (this.store) {

			try {
				BigramSoftPatternModelSerializer.serialize(path, model);

			} catch (EpnoiResourceAccessException e) {
				e.printStackTrace();
			}

		}
	}

	// ----------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		logger.info("Starting the Lexical Relational Model creation");
		LexicalRelationalModelCreationParameters parameters = new LexicalRelationalModelCreationParameters();
		parameters
				.setParameter(
						LexicalRelationalModelCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER,
						"http://drInventorFirstReview/relationalSentencesCorpus");
		parameters
				.setParameter(
						LexicalRelationalModelCreationParameters.MAX_PATTERN_LENGTH_PARAMETER,
						20);

		parameters.setParameter(
				LexicalRelationalModelCreationParameters.MODEL_PATH_PARAMETERS,
				"/JUNK/model.bin");

		parameters
				.setParameter(
						RelationalSentencesCorpusCreationParameters.STORE_RESULT_PARAMETER,
						true);

		parameters.setParameter(
				RelationalSentencesCorpusCreationParameters.VERBOSE_PARAMETER,
				true);

		Core core = CoreUtility.getUIACore();

		LexicalRelationalModelCreator modelCreator = new LexicalRelationalModelCreator();
		try {
			modelCreator.init(core, parameters);
		} catch (EpnoiInitializationException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		modelCreator.create();

		System.out.println("Ending the Lexical Relational Model creation");
	}

	// ----------------------------------------------------------------------------------------------------------------

}
