package org.epnoi.uia.learner.relations.lexical;

import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.exceptions.EpnoiInitializationException;
import org.epnoi.uia.learner.relations.RelationalPattern;
import org.epnoi.uia.learner.relations.RelationalPatternsCorpus;
import org.epnoi.uia.learner.relations.RelationalSentencesCorpus;

public class LexicalRelationalModelCreator {
	private LexicalRelationalModelCreationParameters parameters;
	private Core core;
	private LexicalRelationalPatternsCorpusCreator patternsCorpusCreator;
	private RelationalPatternsCorpus patternsCorpus;
	private BigramSoftPatternModelBuilder model;

	// ----------------------------------------------------------------------------------------------------------------

	public void init(Core core,
			LexicalRelationalModelCreationParameters parameters)
			throws EpnoiInitializationException {
		this.core = core;
		this.parameters = parameters;
		String relationalSentencesCorpusURI = (String) this.parameters
				.getParameterValue(LexicalRelationalModelCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER);
		RelationalSentencesCorpus relationalSentencesCorpus = (RelationalSentencesCorpus) this.core
				.getInformationHandler().get(relationalSentencesCorpusURI);
		if (relationalSentencesCorpus == null) {
			throw new EpnoiInitializationException(
					"The Relational Sentences Corpus "
							+ relationalSentencesCorpusURI
							+ "could not be found");

		} else {
			patternsCorpus = patternsCorpusCreator
					.buildCorpus(relationalSentencesCorpus);
		}
		model = new BigramSoftPatternModelBuilder(parameters);

	}

	// ----------------------------------------------------------------------------------------------------------------

	public BigramSoftPatternModelBuilder createModel() {

		for (RelationalPattern pattern : patternsCorpus.getPatterns()) {
			this.model.addPattern(((LexicalRelationalPattern) pattern));
		}
		return this.model;
	}

	// ----------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		System.out.println("Starting the Lexical Relational Model creation");
		LexicalRelationalModelCreationParameters parameters = new LexicalRelationalModelCreationParameters();
		parameters
				.setParameter(
						LexicalRelationalModelCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER,
						"");
		parameters
				.setParameter(
						LexicalRelationalModelCreationParameters.MAX_PATTERN_LENGTH_PARAMETER,
						10L);
		
		parameters
		.setParameter(
				LexicalRelationalModelCreationParameters.MODEL_PATH_PARAMETERS,
				"/JUNK");
		
		Core core = CoreUtility.getUIACore();

		LexicalRelationalModelCreator modelCreator = new LexicalRelationalModelCreator();
		try {
			modelCreator.init(core, parameters);
		} catch (EpnoiInitializationException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		BigramSoftPatternModelBuilder model = modelCreator.createModel();
		String path = (String) parameters
				.getParameterValue(LexicalRelationalModelCreationParameters.MODEL_PATH_PARAMETERS);
		if (path == null) {
			System.out.println("--> " + model);
		} else {
			BigramSoftPatternModelSerializer.serialize(path, model);
		}
		System.out.println("Ending the Lexical Relational Model creation");
	}

	// ----------------------------------------------------------------------------------------------------------------

}
