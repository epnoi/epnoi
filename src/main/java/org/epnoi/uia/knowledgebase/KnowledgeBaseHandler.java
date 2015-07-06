package org.epnoi.uia.knowledgebase;

import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.knowledgebase.wikidata.WikidataHandlerParameters;
import org.epnoi.uia.knowledgebase.wikidata.WikidataHandlerParameters.DumpProcessingMode;
import org.epnoi.uia.knowledgebase.wordnet.WordNetHandlerParameters;

public class KnowledgeBaseHandler {
	Core core = null;
	private KnowledgeBase knowledgeBase;
	private volatile boolean initialized = false;

	// ---------------------------------------------------------------------------------------------

	public void init(Core core) {
		this.core = core;
	}

	// ---------------------------------------------------------------------------------------------

	private void _initializeKnowledgeBase(Core core) {
		String filepath = "/opt/epnoi/epnoideployment/wordnet/dictWN3.1";
		KnowledgeBaseParameters knowledgeBaseParameters = new KnowledgeBaseParameters();
		WikidataHandlerParameters wikidataParameters = new WikidataHandlerParameters();

		WordNetHandlerParameters wordnetParameters = new WordNetHandlerParameters();
		wordnetParameters.setParameter(
				WordNetHandlerParameters.DICTIONARY_LOCATION, filepath);

		wikidataParameters.setParameter(
				WikidataHandlerParameters.WIKIDATA_VIEW_URI,
				WikidataHandlerParameters.DEFAULT_URI);
		wikidataParameters.setParameter(
				WikidataHandlerParameters.STORE_WIKIDATA_VIEW, false);
		wikidataParameters.setParameter(
				WikidataHandlerParameters.RETRIEVE_WIKIDATA_VIEW, true);
		wikidataParameters.setParameter(
				WikidataHandlerParameters.CREATE_WIKIDATA_VIEW, false);
		wikidataParameters.setParameter(WikidataHandlerParameters.OFFLINE_MODE,
				true);
		wikidataParameters.setParameter(
				WikidataHandlerParameters.DUMP_FILE_MODE,
				DumpProcessingMode.JSON);
		wikidataParameters.setParameter(WikidataHandlerParameters.TIMEOUT, 10);
		wikidataParameters.setParameter(WikidataHandlerParameters.DUMP_PATH,
				"/opt/epnoi/epnoideployment/wikidata");

		knowledgeBaseParameters.setParameter(
				KnowledgeBaseParameters.WORDNET_PARAMETERS, wordnetParameters);

		knowledgeBaseParameters
				.setParameter(KnowledgeBaseParameters.WIKIDATA_PARAMETERS,
						wikidataParameters);

		knowledgeBaseParameters.setParameter(
				KnowledgeBaseParameters.CONSIDER_WIKIDATA, true);
		knowledgeBaseParameters.setParameter(
				KnowledgeBaseParameters.CONSIDER_WORDNET, true);

		KnowledgeBaseFactory knowledgeBaseCreator = new KnowledgeBaseFactory();
		try {
			knowledgeBaseCreator.init(core, knowledgeBaseParameters);
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
}
