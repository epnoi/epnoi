package org.epnoi.uia.learner.knowledgebase;

import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.learner.knowledgebase.wikidata.WikidataHandler;
import org.epnoi.uia.learner.knowledgebase.wikidata.WikidataHandlerBuilder;
import org.epnoi.uia.learner.knowledgebase.wikidata.WikidataHandlerParameters;
import org.epnoi.uia.learner.knowledgebase.wikidata.WikidataHandlerParameters.DumpProcessingMode;
import org.epnoi.uia.learner.knowledgebase.wordnet.WordNetHandler;
import org.epnoi.uia.learner.knowledgebase.wordnet.WordNetHandlerParameters;

public class KnowledgeBaseFactory {
	private Core core; // The UIA core
	private WikidataHandlerBuilder wikidataHandlerBuilder;
	private WordNetHandler wordnetHandler;
	private WikidataHandler wikidataHandler;
	private KnowledgeBaseParameters parameters;
	private boolean retrieveWikidataView = true;

	// ------------------------------------------------------------------------------------

	public void init(Core core, KnowledgeBaseParameters parameters)
			throws EpnoiInitializationException {
		System.out.println("ENTRA");
		this.core = core;
		this.parameters = parameters;
		this.wordnetHandler = new WordNetHandler();
		this.wordnetHandler.init((WordNetHandlerParameters) parameters
				.getParameterValue(KnowledgeBaseParameters.WORDNET_PARAMETERS));

		this.wikidataHandlerBuilder = new WikidataHandlerBuilder();
		this.wikidataHandlerBuilder
				.init(core,
						(WikidataHandlerParameters) parameters
								.getParameterValue(KnowledgeBaseParameters.WIKIDATA_PARAMETERS));

		if (this.parameters
				.getParameterValue(KnowledgeBaseParameters.RETRIEVE_WIKIDATA_VIEW) != null) {
			this.retrieveWikidataView = (boolean) this.parameters
					.getParameterValue(KnowledgeBaseParameters.RETRIEVE_WIKIDATA_VIEW);
		}

	}

	// ------------------------------------------------------------------------------------

	public KnowledgeBase build() {
		System.out.println("---> " + wikidataHandlerBuilder);
		this.wikidataHandler = wikidataHandlerBuilder.build();
		KnowledgeBase knowledgeBase = new KnowledgeBase(this.wordnetHandler,
				this.wikidataHandler);

		return knowledgeBase;
	}

	// ------------------------------------------------------------------------------------

	public static void main(String[] args) {
		System.out.println("Starting the Knowledge Base test!!");

		String filepath = "/epnoi/epnoideployment/wordnet/dictWN3.1/";

		Core core = CoreUtility.getUIACore();

		KnowledgeBaseParameters knowledgeBaseParameters = new KnowledgeBaseParameters();
		WikidataHandlerParameters wikidataParameters = new WikidataHandlerParameters();

		WordNetHandlerParameters wordnetParameters = new WordNetHandlerParameters();
		wordnetParameters.setParameter(
				WordNetHandlerParameters.DICTIONARY_LOCATION, filepath);

		wikidataParameters.setParameter(
				WikidataHandlerParameters.WIKIDATA_VIEW_URI,
				"http://wikidataView");
		wikidataParameters.setParameter(
				WikidataHandlerParameters.STORE_WIKIDATA_VIEW, true);
		wikidataParameters.setParameter(
				WikidataHandlerParameters.RETRIEVE_WIKIDATA_VIEW, false);
		wikidataParameters.setParameter(WikidataHandlerParameters.OFFLINE_MODE,
				true);
		wikidataParameters.setParameter(
				WikidataHandlerParameters.DUMP_FILE_MODE,
				DumpProcessingMode.JSON);
		wikidataParameters.setParameter(WikidataHandlerParameters.TIMEOUT, 10);
		wikidataParameters.setParameter(WikidataHandlerParameters.DUMP_PATH,
				"/Users/rafita/Documents/workspace/wikidataParsingTest");

		knowledgeBaseParameters.setParameter(
				KnowledgeBaseParameters.WORDNET_PARAMETERS, wordnetParameters);

		knowledgeBaseParameters
				.setParameter(KnowledgeBaseParameters.WIKIDATA_PARAMETERS,
						wikidataParameters);

		KnowledgeBaseFactory knowledgeBaseCreator = new KnowledgeBaseFactory();
		try {
			knowledgeBaseCreator.init(core, knowledgeBaseParameters);
		} catch (EpnoiInitializationException e) {
			System.out.println("The KnowledgeBase couldn't be initialized");
			e.printStackTrace();

		}
		KnowledgeBase curatedRelationsTable = knowledgeBaseCreator.build();
		System.out
				.println("Testing for dog-canine-------------------------------------------------------");
		System.out.println(curatedRelationsTable.areRelated("dog", "canrine"));

		System.out
				.println("Testing for dogs-canine-------------------------------------------------------");
		System.out.println(curatedRelationsTable.areRelated("dogs", "canine"));

		System.out
				.println("Testing for dog-canines-------------------------------------------------------");
		System.out.println(curatedRelationsTable.areRelated("dog", "canines "));

		System.out.println("Starting the CuratedRelationsTableCreator test!!");
	}
}
