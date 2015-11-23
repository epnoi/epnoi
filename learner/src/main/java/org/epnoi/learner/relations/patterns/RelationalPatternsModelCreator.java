package org.epnoi.learner.relations.patterns;

import org.epnoi.EpnoiConfig;
import org.epnoi.learner.relations.corpus.MockUpRelationalSentencesCorpusCreator;
import org.epnoi.learner.relations.patterns.syntactic.SyntacticRelationalModelCreationParameters;
import org.epnoi.model.RelationalSentencesCorpus;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class RelationalPatternsModelCreator {
	private static final Logger logger = Logger
			.getLogger(RelationalPatternsModelCreator.class.getName());
	private RelationalPatternsModelCreationParameters parameters;

	private Core core;

	String relationalSentencesCorpusURI;
	private RelationalSentencesCorpus relationalSentencesCorpus;
	private RelationalPatternsCorpusCreator patternsCorpusCreator;
	private RelationalPatternsCorpus patternsCorpus;

	private RelationalPatternsModelBuilder modelBuilder;
	private RelationalPatternsModel model;
	MockUpRelationalSentencesCorpusCreator relationSentencesCorpusCreator;
	private boolean store;
	private boolean verbose;
	private boolean test;
	private String path;

	// ----------------------------------------------------------------------------------------------------------------

	public void init(Core core,
			RelationalPatternsModelCreationParameters parameters)
			throws EpnoiInitializationException {
		logger.info("Initializing the RelationalModelCreator with the following parameters");
		logger.info(parameters.toString());
		this.core = core;
		this.parameters = parameters;
		String relationalSentencesCorpusURI = (String) this.parameters
				.getParameterValue(SyntacticRelationalModelCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER);


		this.patternsCorpusCreator = new RelationalPatternsCorpusCreator();
		RelationalPatternGenerator relationalPatternsGenerator = null;
		try {
			relationalPatternsGenerator = RelationalPatternsGeneratorFactory
					.build(parameters);
		} catch (EpnoiResourceAccessException exception) {

			throw new EpnoiInitializationException(exception.getMessage());
		}
		this.patternsCorpusCreator.init(core, relationalPatternsGenerator);

		this.relationSentencesCorpusCreator = new MockUpRelationalSentencesCorpusCreator();

		try {
			this.relationSentencesCorpusCreator.init(core);
		} catch (EpnoiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}

		try {
			this.modelBuilder = RelationalPatternsModelBuilderFactory
					.build(parameters);
		} catch (EpnoiResourceAccessException e) {

			throw new EpnoiInitializationException(e.getMessage());
		}

		this.path = (String) parameters
				.getParameterValue(RelationalPatternsModelCreationParameters.MODEL_PATH);

		this.store = (boolean) parameters
				.getParameterValue(RelationalPatternsModelCreationParameters.STORE);

		this.verbose = (boolean) parameters
				.getParameterValue(RelationalPatternsModelCreationParameters.VERBOSE);

		if (parameters
				.getParameterValue(RelationalPatternsModelCreationParameters.TEST) != null) {

			this.test = ((boolean) parameters
					.getParameterValue(RelationalPatternsModelCreationParameters.TEST));
		} else {
			this.test = false;
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------

	public void create() {
		_obtainCorpora();
		this.model = _createModel();
		if (this.verbose) {
			this.model.show();
		}
		if (this.store) {
			_storeModel();
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------

	private void _storeModel() {
		logger.info("Storing the model at " + path);

		try {
			RelationalPatternsModelSerializer.serialize(path, model);

		} catch (EpnoiResourceAccessException e) {
			logger.severe("There was a problem trying to serialize the BigramSoftPatternModel at "
					+ path);
			logger.severe(e.getMessage());
		}

	}

	// ------------------------------------------------------------------------------------------------------------------------

	private void _obtainCorpora() {
		logger.info("Obtaining the RelationalPatternsCorspus");

		if (this.test) {
			this.relationalSentencesCorpus = relationSentencesCorpusCreator
					.createTestCorpus();
		} else {
			this.relationalSentencesCorpus = _retrieveRelationalSentencesCorpus();
		}
		if (relationalSentencesCorpus == null) {
			logger.severe("The RelationalSentecesCorpus was null, the model cannot be created!");
		} else {

			logger.info("The RelationalSencentcesCorpus has "
					+ relationalSentencesCorpus.getSentences().size()
					+ " sentences");
			patternsCorpus = patternsCorpusCreator
					.buildCorpus(relationalSentencesCorpus);

			logger.info("The RelationalPatternsCorpus has "
					+ patternsCorpus.getPatterns().size() + " patterns");
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------

	private RelationalSentencesCorpus _retrieveRelationalSentencesCorpus() {

		RelationalSentencesCorpus relationalSentencesCorpus = (RelationalSentencesCorpus) this.core
				.getInformationHandler().get(relationalSentencesCorpusURI,
						RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS);

		if (relationalSentencesCorpus == null) {
			logger.info("The Relational Sentences Corpus "
					+ relationalSentencesCorpusURI + "could not be found");

		} else {

			logger.info("The RelationalSencentcesCorpus has "
					+ relationalSentencesCorpus.getSentences().size()
					+ " sentences");
			patternsCorpus = patternsCorpusCreator
					.buildCorpus(relationalSentencesCorpus);

			logger.info("The RelationalPatternsCorpus has "
					+ patternsCorpus.getPatterns().size() + " patterns");
		}
		return relationalSentencesCorpus;
	}

	// ----------------------------------------------------------------------------------------------------------------

	private RelationalPatternsModel _createModel() {
		long startingTime = System.currentTimeMillis();
		logger.info("Adding all the patterns to the model");
		for (RelationalPattern pattern : patternsCorpus.getPatterns()) {
			this.modelBuilder.addPattern(pattern);
		}
		logger.info("Building the model");
		RelationalPatternsModel model = this.modelBuilder.build();
		long totalTime = startingTime - System.currentTimeMillis();
		logger.info("It took " + Math.abs(totalTime) + " ms to build the model");
		return model;
	}

	// ----------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		System.out.println("Starting the Syntactic Relational Model creation");
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

	//	Core core = CoreUtility.getUIACore();

		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();



		applicationContext.getEnvironment().setActiveProfiles(EpnoiConfig.DEVELOP_PROFILE);
/*
		MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();

		Map epnoiProperties = new HashMap();

		epnoiProperties.put(EpnoiConfig.EPNOI_PROPERTIES_PATH, configFilePath);
		propertySources.addFirst(new MapPropertySource(EpnoiConfig.EPNOI_PROPERTIES, epnoiProperties));


*/
		applicationContext.register(org.epnoi.learner.LearnerConfig.class);
		applicationContext.refresh();

		List<String> beans = new ArrayList<>();
		for (String bean : applicationContext.getBeanDefinitionNames()) {
			beans.add("   Bean: " + bean);
		}
		logger.info("Initializing the Spring context with the following beans: \n"+String.join("\n",beans));




/*
		RelationalPatternsModelCreator modelCreator = new RelationalPatternsModelCreator();
		try {
			modelCreator.init(core, parameters);
		} catch (EpnoiInitializationException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		modelCreator.create();

		System.out.println("Ending the Relational Model creation");
*/
	}

	// ----------------------------------------------------------------------------------------------------------------

}
