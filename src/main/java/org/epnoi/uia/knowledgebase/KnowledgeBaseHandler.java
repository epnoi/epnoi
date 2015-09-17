package org.epnoi.uia.knowledgebase;

import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.parameterization.ParametersModel;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.knowledgebase.wikidata.WikidataHandlerParameters;
import org.epnoi.uia.knowledgebase.wikidata.WikidataHandlerParameters.DumpProcessingMode;
import org.epnoi.uia.knowledgebase.wordnet.WordNetHandlerParameters;

public class KnowledgeBaseHandler {

	Core core = null;
	private KnowledgeBase knowledgeBase;
	private KnowledgeBaseParameters knowledgeBaseParameters;

	private volatile boolean initialized = false;

	// ---------------------------------------------------------------------------------------------

	public void init(Core core) {
		this.core = core;
		this.knowledgeBaseParameters = new KnowledgeBaseParameters();
		String wordnetDictionaryfilepath = this.core.getParameters().getKnowledgeBase().getWordnet().getDictionaryPath();
		WikidataHandlerParameters wikidataParameters = new WikidataHandlerParameters();

		WordNetHandlerParameters wordnetParameters = new WordNetHandlerParameters();
		wordnetParameters.setParameter(WordNetHandlerParameters.DICTIONARY_LOCATION, wordnetDictionaryfilepath);

		
		
		knowledgeBaseParameters.setParameter(KnowledgeBaseParameters.CONSIDER_WIKIDATA,
				this.core.getParameters().getKnowledgeBase().getWikidata().isConsidered());
		knowledgeBaseParameters.setParameter(KnowledgeBaseParameters.CONSIDER_WORDNET,
				this.core.getParameters().getKnowledgeBase().getWordnet().isConsidered());
		
		knowledgeBaseParameters.setParameter(KnowledgeBaseParameters.LAZY, this.core.getParameters().getKnowledgeBase().isLazy());
		wikidataParameters.setParameter(WikidataHandlerParameters.DUMP_PATH,
				this.core.getParameters().getKnowledgeBase().getWikidata().getDumpPath());
		
		String mode = core.getParameters().getKnowledgeBase().getWikidata().getMode();
		if (org.epnoi.uia.parameterization.ParametersModel.WIKIDATA_MODE_CREATE.equals(mode)) {

			wikidataParameters.setParameter(WikidataHandlerParameters.CREATE_WIKIDATA_VIEW, true);
			wikidataParameters.setParameter(WikidataHandlerParameters.RETRIEVE_WIKIDATA_VIEW, false);

		}
		if (org.epnoi.uia.parameterization.ParametersModel.WIKIDATA_MODE_LOAD.equals(mode)) {

			wikidataParameters.setParameter(WikidataHandlerParameters.CREATE_WIKIDATA_VIEW, false);
			wikidataParameters.setParameter(WikidataHandlerParameters.RETRIEVE_WIKIDATA_VIEW, true);

		}
		

		wikidataParameters.setParameter(WikidataHandlerParameters.STORE_WIKIDATA_VIEW, false);
		wikidataParameters.setParameter(WikidataHandlerParameters.OFFLINE_MODE, true);
		wikidataParameters.setParameter(WikidataHandlerParameters.DUMP_FILE_MODE, DumpProcessingMode.JSON);
		wikidataParameters.setParameter(WikidataHandlerParameters.TIMEOUT, 0);
		

		knowledgeBaseParameters.setParameter(KnowledgeBaseParameters.WORDNET_PARAMETERS, wordnetParameters);

		knowledgeBaseParameters.setParameter(KnowledgeBaseParameters.WIKIDATA_PARAMETERS, wikidataParameters);

	

	}

	// ---------------------------------------------------------------------------------------------

	private void _initializeKnowledgeBase(Core core) {

		KnowledgeBaseFactory knowledgeBaseCreator = new KnowledgeBaseFactory();
		try {
			knowledgeBaseCreator.init(core, this.knowledgeBaseParameters);
		} catch (EpnoiInitializationException e) {
			System.out.println("The KnowledgeBase couldn't be initialized");
			e.printStackTrace();

		}
		this.knowledgeBase = knowledgeBaseCreator.build();
		initialized = true;
	}

	// ---------------------------------------------------------------------------------------------

	public synchronized KnowledgeBase getKnowledgeBase() {

		if (!initialized) {
			_initializeKnowledgeBase(this.core);
		}
		return this.knowledgeBase;
	}

	// ---------------------------------------------------------------------------------------------

	public synchronized boolean isKnowledgeBaseInitialized() {
		return this.initialized;
	}

	// ---------------------------------------------------------------------------------------------

	public KnowledgeBaseParameters getKnowledgeBaseParameters() {
		return this.knowledgeBaseParameters;
	}
}
