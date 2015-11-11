package org.epnoi.knowledgebase;

import org.epnoi.knowledgebase.wikidata.WikidataHandlerParameters;
import org.epnoi.knowledgebase.wikidata.WikidataHandlerParameters.DumpProcessingMode;
import org.epnoi.knowledgebase.wordnet.WordNetHandlerParameters;
import org.epnoi.model.KnowledgeBase;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.modules.KnowldedgeBaseHandler;
import org.epnoi.model.modules.KnowledgeBaseParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@Component
public class KnowledgeBaseHandlerImpl implements KnowldedgeBaseHandler {
    private static final Logger logger = Logger.getLogger(KnowledgeBaseHandlerImpl.class.getName());
    @Autowired
    Core core;
    private KnowledgeBase knowledgeBase;
    private KnowledgeBaseParameters knowledgeBaseParameters;

    private volatile boolean initialized = false;

    // ---------------------------------------------------------------------------------------------

    @PostConstruct
    @Override
    public void init() throws EpnoiInitializationException {

        if (core.getParameters().getKnowledgeBase() != null) {


            this.knowledgeBaseParameters = new KnowledgeBaseParameters();
            String wordnetDictionaryfilepath = this.core.getParameters().getKnowledgeBase().getWordnet()
                    .getDictionaryPath();
            WikidataHandlerParameters wikidataParameters = new WikidataHandlerParameters();

            WordNetHandlerParameters wordnetParameters = new WordNetHandlerParameters();
            wordnetParameters.setParameter(WordNetHandlerParameters.DICTIONARY_LOCATION, wordnetDictionaryfilepath);

            knowledgeBaseParameters.setParameter(KnowledgeBaseParameters.CONSIDER_WIKIDATA,
                    this.core.getParameters().getKnowledgeBase().getWikidata().isConsidered());
            knowledgeBaseParameters.setParameter(KnowledgeBaseParameters.CONSIDER_WORDNET,
                    this.core.getParameters().getKnowledgeBase().getWordnet().isConsidered());

            knowledgeBaseParameters.setParameter(KnowledgeBaseParameters.LAZY,
                    this.core.getParameters().getKnowledgeBase().isLazy());

            String mode = core.getParameters().getKnowledgeBase().getWikidata().getMode();

            if (org.epnoi.model.parameterization.ParametersModel.KNOWLEDGEBASE_WIKIDATA_MODE_CREATE.equals(mode)) {

                wikidataParameters.setParameter(WikidataHandlerParameters.CREATE_WIKIDATA_VIEW, true);
                wikidataParameters.setParameter(WikidataHandlerParameters.RETRIEVE_WIKIDATA_VIEW, false);

            }
            if (org.epnoi.model.parameterization.ParametersModel.KNOWLEDGEBASE_WIKIDATA_MODE_LOAD.equals(mode)) {

                wikidataParameters.setParameter(WikidataHandlerParameters.CREATE_WIKIDATA_VIEW, false);
                wikidataParameters.setParameter(WikidataHandlerParameters.RETRIEVE_WIKIDATA_VIEW, true);

            }
            wikidataParameters.setParameter(WikidataHandlerParameters.DUMP_PATH,
                    this.core.getParameters().getKnowledgeBase().getWikidata().getDumpPath());
            wikidataParameters.setParameter(WikidataHandlerParameters.WIKIDATA_VIEW_URI,
                    this.core.getParameters().getKnowledgeBase().getWikidata().getUri());
            wikidataParameters.setParameter(WikidataHandlerParameters.IN_MEMORY,
                    this.core.getParameters().getKnowledgeBase().getWikidata().isInMemory());
            wikidataParameters.setParameter(WikidataHandlerParameters.STORE_WIKIDATA_VIEW, true);
            wikidataParameters.setParameter(WikidataHandlerParameters.OFFLINE_MODE, true);
            wikidataParameters.setParameter(WikidataHandlerParameters.DUMP_FILE_MODE, DumpProcessingMode.JSON);
            wikidataParameters.setParameter(WikidataHandlerParameters.TIMEOUT, 0);

            knowledgeBaseParameters.setParameter(KnowledgeBaseParameters.WORDNET_PARAMETERS, wordnetParameters);

            knowledgeBaseParameters.setParameter(KnowledgeBaseParameters.WIKIDATA_PARAMETERS, wikidataParameters);
            // In case that the knowledge base is not initialized in a lazy
            // fashion
            // we initialize it
            if (!this.core.getParameters().getKnowledgeBase().isLazy()) {
                _initializeKnowledgeBase(core);
            }
        } else {
            logger.severe(
                    "The Knowledge Base was not initialized since there is no knowledge base element defined in the uia configuration file");
        }
    }

    // ---------------------------------------------------------------------------------------------

    private void _initializeKnowledgeBase(Core core) throws EpnoiInitializationException {

        KnowledgeBaseFactory knowledgeBaseCreator = new KnowledgeBaseFactory();
        try {
            knowledgeBaseCreator.init(core, this.knowledgeBaseParameters);
            this.knowledgeBase = knowledgeBaseCreator.build();
            initialized = true;
        } catch (EpnoiInitializationException e) {
            logger.severe("The KnowledgeBase couldn't be initialized");
            throw new EpnoiInitializationException(e.getMessage());

        }

    }

    // ---------------------------------------------------------------------------------------------


    @Override
    public synchronized KnowledgeBase getKnowledgeBase()
            throws EpnoiInitializationException, EpnoiResourceAccessException {
        if (core.getParameters().getKnowledgeBase() == null) {
            throw new EpnoiInitializationException(
                    "Error accessing the the Knowledge Base, since it was not initialized as there is no knowledge base element defined in the uia configuration file");
        }
        if (!initialized) {

            _initializeKnowledgeBase(this.core);

        }
        return this.knowledgeBase;
    }

    // ---------------------------------------------------------------------------------------------

    /* (non-Javadoc)
     * @see org.epnoi.uia.knowledgebase.KnowldedgeBaseHandlerInterface#isKnowledgeBaseInitialized()
     */
    @Override
    public synchronized boolean isKnowledgeBaseInitialized() {
        return this.initialized;
    }

    // ---------------------------------------------------------------------------------------------

	/* (non-Javadoc)
     * @see org.epnoi.uia.knowledgebase.KnowldedgeBaseHandlerInterface#getKnowledgeBaseParameters()
	 */
    /*
    @Override
	public KnowledgeBaseParameters getKnowledgeBaseParameters() {
		return this.knowledgeBaseParameters;
	}
	*/
}
