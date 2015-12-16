package org.epnoi.knowledgebase;

import org.epnoi.knowledgebase.wikidata.WikidataHandler;
import org.epnoi.knowledgebase.wikidata.WikidataHandlerBuilder;
import org.epnoi.knowledgebase.wikidata.WikidataHandlerParameters;
import org.epnoi.knowledgebase.wordnet.WordNetHandler;
import org.epnoi.knowledgebase.wordnet.WordNetHandlerParameters;
import org.epnoi.model.KnowledgeBase;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.modules.KnowledgeBaseParameters;


public class KnowledgeBaseFactory {
	private Core core; // The UIA core
	private WikidataHandlerBuilder wikidataHandlerBuilder;
	private WordNetHandler wordnetHandler;
	private WikidataHandler wikidataHandler;
	private KnowledgeBaseParameters parameters;
	private boolean retrieveWikidataView = true;
	private boolean considerWikidata;
	private boolean considerWordNet;
	

	// ------------------------------------------------------------------------------------

	public void init(Core core, KnowledgeBaseParameters parameters)
			throws EpnoiInitializationException {
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
		
		this.considerWikidata =(boolean) this.parameters
				.getParameterValue(KnowledgeBaseParameters.CONSIDER_WIKIDATA);
	
		this.considerWikidata =(boolean) this.parameters
				.getParameterValue(KnowledgeBaseParameters.CONSIDER_WIKIDATA);
	
	}

	// ------------------------------------------------------------------------------------

	public KnowledgeBase build() throws EpnoiInitializationException {
		
		if (this.considerWikidata){
		this.wikidataHandler = wikidataHandlerBuilder.build();
		}
		KnowledgeBase knowledgeBase = new KnowledgeBaseImpl(this.wordnetHandler,
				this.wikidataHandler);
		knowledgeBase.init(this.parameters);

		return knowledgeBase;
	}

	// ------------------------------------------------------------------------------------
/*FOR_TEST
	public static void main(String[] args) throws EpnoiInitializationException{
		System.out.println("Starting the Knowledge Base test!!");

		String filepath = "opt/epnoi/epnoi/epnoideployment/wordnet/dictWN3.1/";

		Core core = CoreUtility.getUIACore();

		KnowledgeBaseParameters knowledgeBaseParameters = new KnowledgeBaseParameters();
		WikidataHandlerParameters wikidataParameters = new WikidataHandlerParameters();

		WordNetHandlerParameters wordnetParameters = new WordNetHandlerParameters();
		wordnetParameters.setParameter(
				WordNetHandlerParameters.DICTIONARY_LOCATION, filepath);

		wikidataParameters.setParameter(
				WikidataHandlerParameters.WIKIDATA_VIEW_URI,
				WikidataHandlerParameters.DEFAULT_URI);
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
		
		knowledgeBaseParameters.setParameter(
				KnowledgeBaseParameters.CONSIDER_WIKIDATA, false);
		knowledgeBaseParameters.setParameter(
				KnowledgeBaseParameters.CONSIDER_WORDNET, true);

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
		System.out.println(curatedRelationsTable.areRelated("dog", "canrine", RelationHelper.HYPERNYMY));

		System.out
				.println("Testing for dogs-canine-------------------------------------------------------");
		System.out.println(curatedRelationsTable.areRelated("dogs", "canine",RelationHelper.HYPERNYMY));

		System.out
				.println("Testing for dog-canines-------------------------------------------------------");
		System.out.println(curatedRelationsTable.areRelated("dog", "canines ",RelationHelper.HYPERNYMY));

		System.out.println("Starting the CuratedRelationsTableCreator test!!");
	}
	*/
}
