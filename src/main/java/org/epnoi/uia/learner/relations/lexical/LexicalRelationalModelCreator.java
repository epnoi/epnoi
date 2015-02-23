package org.epnoi.uia.learner.relations.lexical;

import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.exceptions.EpnoiInitializationException;
import org.epnoi.uia.exceptions.EpnoiResourceAccessException;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.learner.relations.RelationalPattern;
import org.epnoi.uia.learner.relations.RelationalPatternsCorpus;
import org.epnoi.uia.learner.relations.RelationalSentencesCorpus;

public class LexicalRelationalModelCreator {
	private LexicalRelationalModelCreationParameters parameters;
	private Core core;
	private LexicalRelationalPatternsCorpusCreator patternsCorpusCreator;
	private RelationalPatternsCorpus patternsCorpus;
	private BigramSoftPatternModelBuilder modelBuilder;

	// ----------------------------------------------------------------------------------------------------------------

	public void init(Core core,
			LexicalRelationalModelCreationParameters parameters)
			throws EpnoiInitializationException {
		this.core = core;
		this.parameters = parameters;
		String relationalSentencesCorpusURI = (String) this.parameters
				.getParameterValue(LexicalRelationalModelCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER);
		this.patternsCorpusCreator = new LexicalRelationalPatternsCorpusCreator();
		this.patternsCorpusCreator.init(core);
		
		RelationalSentencesCorpus relationalSentencesCorpus = (RelationalSentencesCorpus) this.core
				.getInformationHandler().get(relationalSentencesCorpusURI,RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS);
		
		System.out.println("has--> "+relationalSentencesCorpus.getSentences().size());
		if (relationalSentencesCorpus == null) {
			throw new EpnoiInitializationException(
					"The Relational Sentences Corpus "
							+ relationalSentencesCorpusURI
							+ "could not be found");

		} else {
			patternsCorpus = patternsCorpusCreator
					.buildCorpus(relationalSentencesCorpus);
			
			System.out.println(patternsCorpus);
		}
		modelBuilder = new BigramSoftPatternModelBuilder(parameters);

	}

	// ----------------------------------------------------------------------------------------------------------------

	public BigramSoftPatternModel createModel() {

		for (RelationalPattern pattern : patternsCorpus.getPatterns()) {
			this.modelBuilder.addPattern(((LexicalRelationalPattern) pattern));
		}
		System.out.println("--"+this.modelBuilder);
		return this.modelBuilder.build();
	}

	// ----------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		System.out.println("Starting the Lexical Relational Model creation");
		LexicalRelationalModelCreationParameters parameters = new LexicalRelationalModelCreationParameters();
		parameters
				.setParameter(
						LexicalRelationalModelCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER,
						"http://thetestcorpus/drinventor");
		parameters
				.setParameter(
						LexicalRelationalModelCreationParameters.MAX_PATTERN_LENGTH_PARAMETER,
						10);
		
		parameters
		.setParameter(
				LexicalRelationalModelCreationParameters.MODEL_PATH_PARAMETERS,
				"/JUNK/model.bin");
		
		Core core = CoreUtility.getUIACore();

		LexicalRelationalModelCreator modelCreator = new LexicalRelationalModelCreator();
		try {
			modelCreator.init(core, parameters);
		} catch (EpnoiInitializationException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		BigramSoftPatternModel model = modelCreator.createModel();
		String path = (String) parameters
				.getParameterValue(LexicalRelationalModelCreationParameters.MODEL_PATH_PARAMETERS);
		if (path == null) {
			System.out.println("--> " + model);
		} else {
			System.out.println("--> " + model);
			try {
				BigramSoftPatternModelSerializer.serialize(path, model);
				BigramSoftPatternModel readedModel = BigramSoftPatternModelSerializer.deserialize(path);
			} catch (EpnoiResourceAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("r--> " + model);
		}
		System.out.println("Ending the Lexical Relational Model creation");
	}

	// ----------------------------------------------------------------------------------------------------------------

}
