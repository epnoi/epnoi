package org.epnoi.uia.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.epnoi.knowledgebase.KnowledgeBaseHandlerImpl;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.AnnotationHandler;
import org.epnoi.model.modules.Core;
import org.epnoi.model.modules.DomainsHandler;
import org.epnoi.model.modules.EventBus;
import org.epnoi.model.modules.HarvestersHandler;
import org.epnoi.model.modules.InformationHandler;
import org.epnoi.model.modules.InformationSourcesHandler;
import org.epnoi.model.modules.InformationStore;
import org.epnoi.model.modules.InformationStoreHelper;
import org.epnoi.model.modules.KnowldedgeBaseHandler;
import org.epnoi.model.modules.NLPHandler;
import org.epnoi.model.modules.SearchHandler;
import org.epnoi.model.parameterization.CassandraInformationStoreParameters;
import org.epnoi.model.parameterization.MapInformationStoreParameters;
import org.epnoi.model.parameterization.ParametersModel;
import org.epnoi.model.parameterization.SOLRInformationStoreParameters;
import org.epnoi.model.parameterization.VirtuosoInformationStoreParameters;
import org.epnoi.sources.InformationSourcesHandlerImpl;
import org.epnoi.uia.annotation.AnnotationHandlerImpl;
import org.epnoi.uia.core.eventbus.EventBusFactory;
import org.epnoi.uia.core.eventbus.InternalEventBusImpl;
import org.epnoi.uia.domains.DomainsHandlerImpl;
import org.epnoi.uia.informationhandler.InformationHandlerImp;
import org.epnoi.uia.informationstore.InformationStoreFactory;
import org.epnoi.uia.nlp.NLPHandlerImpl;
import org.epnoi.uia.search.SearchHandlerImpl;

public class CoreImpl implements Core {

	private static final Logger logger = Logger.getLogger(CoreImpl.class.getName());

	private HashMap<String, InformationStore> informationStores;
	private HashMap<String, List<InformationStore>> informationStoresByType;

//	private RSSHoarder rssHoarder;
//	private RSSHarvester rssHarvester;
	private InformationHandler informationHandler;
	private InformationSourcesHandler informationSourcesHandler = null;

	private ParametersModel parametersModel = null;

	private SearchHandler searchHandler = null;
	private AnnotationHandler annotationHandler = null;
	private DomainsHandler domainsHandler = null;
	private HarvestersHandler harvestersHandler = null;
	private EventBus eventBus = null;
	private KnowldedgeBaseHandler knowledgeBaseHandler = null;
	private NLPHandler nlpHandler = null;

	/* (non-Javadoc)
	 * @see org.epnoi.uia.core.CoreInterface#init(org.epnoi.model.parameterization.ParametersModel)
	 */

	/* (non-Javadoc)
	 * @see org.epnoi.uia.core.Core#init(org.epnoi.model.parameterization.ParametersModel)
	 */
	@Override
	public synchronized void init(ParametersModel parametersModel) throws EpnoiInitializationException {
		logger.info(
				"\n =================================================================================================== \n starting epnoi! \n ===================================================================================================");
		logger.info("Initializing the epnoi uia core with the following parameters ");
		logger.info(parametersModel.toString());
		this.informationStores = new HashMap<String, InformationStore>();
		this.informationStoresByType = new HashMap<String, List<InformationStore>>();
		this.parametersModel = parametersModel;
		this._initEventBus();
		this._initNLPHandler();
		this._informationStoresInitialization();
		this._initInformationHandler();
		this._initInformationSourcesHandler();
		this._initSearchHandler();
		this._initAnnotationsHandler();
		this._initDomainsHandler();
		/*
		 * this._hoardersInitialization(); this._harvestersInitialization();
		 */
		this._knowedlgeBaseHandlerInitialization();
		logger.info("");
		logger.info("");
		logger.info(
				"===================================================================================================");
		logger.info("");
		logger.info("");
	}

	/* (non-Javadoc)
	 * @see org.epnoi.uia.core.CoreInterface#getNLPHandler()
	 */
	/* (non-Javadoc)
	 * @see org.epnoi.uia.core.Core#getNLPHandler()
	 */

	@Override
	public NLPHandler getNLPHandler() {
		return nlpHandler;
	}

	// ----------------------------------------------------------------------------------------------------------

	@Deprecated
	@Override
	public void setNLPHandler(NLPHandler nlpHandler) {
		this.nlpHandler = (NLPHandler) nlpHandler;
	}

	// ----------------------------------------------------------------------------------------------------------

	private void _initNLPHandler() {

		this.nlpHandler = new NLPHandlerImpl();
		this.nlpHandler.init(this, parametersModel);

	}

	// ----------------------------------------------------------------------------------------------------------

	private void _initDomainsHandler() {
		this.domainsHandler = new DomainsHandlerImpl();
		this.domainsHandler.init(this);

	}

	// ----------------------------------------------------------------------------------------------------------

	private void _initEventBus() {

		logger.info("Initializing the Event Bus");
		this.eventBus = EventBusFactory.newInstance(parametersModel);
		this.eventBus.init();
	}

	// ----------------------------------------------------------------------------------------------------------

	private void _initAnnotationsHandler() {
		this.annotationHandler = new AnnotationHandlerImpl(this);

	}

	/**
	 * Information Stores initialization
	 */

	private void _informationStoresInitialization() {

		logger.info("Initializing information stores");
		logger.info("Initializing Virtuoso information stores");
		for (VirtuosoInformationStoreParameters virtuosoInformationStoreParameters : parametersModel
				.getVirtuosoInformationStore()) {
			logger.info(virtuosoInformationStoreParameters.toString());

			InformationStore newInformationStore = InformationStoreFactory
					.buildInformationStore(virtuosoInformationStoreParameters, parametersModel);

			this.informationStores.put(virtuosoInformationStoreParameters.getURI(), newInformationStore);

			_addInformationStoreByType(newInformationStore, InformationStoreHelper.RDF_INFORMATION_STORE);
			logger.info("The status of the information source is " + newInformationStore.test());

		}
		logger.info("Initializing SOLR information stores");
		for (SOLRInformationStoreParameters solrInformationStoreParameters : parametersModel
				.getSolrInformationStore()) {
			logger.info(solrInformationStoreParameters.toString());

			InformationStore newInformationStore = InformationStoreFactory
					.buildInformationStore(solrInformationStoreParameters, parametersModel);

			this.informationStores.put(solrInformationStoreParameters.getURI(), newInformationStore);

			_addInformationStoreByType(newInformationStore, InformationStoreHelper.SOLR_INFORMATION_STORE);
			logger.info("The status of the information source is " + newInformationStore.test());

		}
		logger.info("Initializing Cassandra information stores");
		for (CassandraInformationStoreParameters cassandraInformationStoreParameters : parametersModel
				.getCassandraInformationStore()) {
			logger.info(cassandraInformationStoreParameters.toString());

			InformationStore newInformationStore = InformationStoreFactory
					.buildInformationStore(cassandraInformationStoreParameters, parametersModel);

			this.informationStores.put(cassandraInformationStoreParameters.getURI(), newInformationStore);

			_addInformationStoreByType(newInformationStore, InformationStoreHelper.CASSANDRA_INFORMATION_STORE);
			logger.info("The status of the information source is " + newInformationStore.test());

		}
		logger.info("Initializing map information stores");
		for (MapInformationStoreParameters mapInformationStoreParameters : parametersModel.getMapInformationStore()) {
			logger.info(mapInformationStoreParameters.toString());

			InformationStore newInformationStore = InformationStoreFactory
					.buildInformationStore(mapInformationStoreParameters, parametersModel);

			this.informationStores.put(mapInformationStoreParameters.getURI(), newInformationStore);

			_addInformationStoreByType(newInformationStore, InformationStoreHelper.MAP_INFORMATION_STORE);
			logger.info("The status of the information source is " + newInformationStore.test());

		}

	}

	// ----------------------------------------------------------------------------------------------------------

	private void _initInformationHandler() {
		this.informationHandler = new InformationHandlerImp(this);
	}

	// ----------------------------------------------------------------------------------------------------------

	private void _initInformationSourcesHandler() {
		this.informationSourcesHandler = new InformationSourcesHandlerImpl(this);
	}

	// ----------------------------------------------------------------------------------------------------------

	private void _addInformationStoreByType(InformationStore informationStore, String type) {
		List<InformationStore> informationsStoresOfType = this.informationStoresByType.get(type);
		if (informationsStoresOfType == null) {
			informationsStoresOfType = new ArrayList<InformationStore>();
			this.informationStoresByType.put(type, informationsStoresOfType);
		}
		informationsStoresOfType.add(informationStore);
	}

	// ----------------------------------------------------------------------------------------------------------
/*
	private void _hoardersInitialization() {
		logger.info("Initializing hoarders");
		RSSHoarderParameters parameters = this.parametersModel.getRssHoarder();
		if (parameters != null) {
			this.rssHoarder = new RSSHoarder(parameters);
			this.rssHoarder.start();
		} else {
			logger.info("There was no RSSHoarder defined in the configuration file");
		}
	}
*/
	// ----------------------------------------------------------------------------------------------------------
/*
	private void _harvestersInitialization() throws EpnoiInitializationException {
		logger.info("Initializing the HarvestersHandler");

		this.harvestersHandler = new HarvestersHandler();
		this.harvestersHandler.init(this);

		logger.info("Initializing the RSSHarvester");
		RSSHarvesterParameters parameters = this.parametersModel.getRssHarvester();

		if (parameters != null) {
			this.rssHarvester = new RSSHarvester(this, parameters);
			this.rssHarvester.start();
		} else {
			logger.info("There was no RSSHarvester defined in the configuration file");
		}
	}
*/
	// ----------------------------------------------------------------------------------------------------------

	private void _initSearchHandler() {
		this.searchHandler = new SearchHandlerImpl(this);
	}

	// ----------------------------------------------------------------------------------------------------------

	@Override
	public Collection<InformationStore> getInformationStores() {
		return this.informationStores.values();
	}

	// ----------------------------------------------------------------------------------------------------------

	@Override
	public List<InformationStore> getInformationStoresByType(String type) {
		return this.informationStoresByType.get(type);
	}

	// ----------------------------------------------------------------------------------------------------------

	@Override
	public InformationHandler getInformationHandler() {
		return this.informationHandler;
	}

	// ----------------------------------------------------------------------------------------------------------

	@Override
	public InformationSourcesHandler getInformationSourcesHandler() {
		return informationSourcesHandler;
	}

	// ----------------------------------------------------------------------------------------------------------

	@Override
	public void setInformationSourcesHandler(InformationSourcesHandler informationSourcesHandler) {
		this.informationSourcesHandler = informationSourcesHandler;
	}

	// ----------------------------------------------------------------------------------------------------------

	@Override
	public SearchHandler getSearchHandler() {
		return searchHandler;
	}

	// ----------------------------------------------------------------------------------------------------------
	
	@Override
	public void setSearchHandler(SearchHandler searchHandler) {
		this.searchHandler = searchHandler;
	}

	// ----------------------------------------------------------------------------------------------------------

	@Override
	public boolean checkStatus(String informationStoreURI) {
		InformationStore informationStore = this.informationStores.get(informationStoreURI);
		return informationStore.test();
	}

	// ----------------------------------------------------------------------------------------------------------
	
	@Override
	public void close() {
		for (InformationStore dataSource : this.informationStores.values()) {
			dataSource.close();
		}

	}

	// ----------------------------------------------------------------------------------------------------------
	

	@Override
	public AnnotationHandler getAnnotationHandler() {
		return annotationHandler;
	}

	// ----------------------------------------------------------------------------------------------------------

	@Override
	public void setAnnotationHandler(AnnotationHandler annotationHandler) {
		this.annotationHandler = annotationHandler;
	}

	// ----------------------------------------------------------------------------------------------------------

	
	@Override
	public EventBus getEventBus() {
		return eventBus;
	}

	// ----------------------------------------------------------------------------------------------------------

	
	@Override
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	// ----------------------------------------------------------------------------------------------------------

	private void _knowedlgeBaseHandlerInitialization() throws EpnoiInitializationException {
		
			this.knowledgeBaseHandler = new KnowledgeBaseHandlerImpl();
			this.knowledgeBaseHandler.init(this);
		
	}

	// ----------------------------------------------------------------------------------------------------------

	

	@Override
	public DomainsHandler getDomainsHandler() {
		return domainsHandler;
	}

	// ----------------------------------------------------------------------------------------------------------


	@Override
	public void setDomainsHandler(DomainsHandler domainsHandler) {
		this.domainsHandler = domainsHandler;
	}

	// ----------------------------------------------------------------------------------------------------------

	
	@Override
	public ParametersModel getParameters() {
		return this.parametersModel;
	}

	// ----------------------------------------------------------------------------------------------------------

	@Override
	public HarvestersHandler getHarvestersHandler() {
		return harvestersHandler;
	}

	
	@Override
	public void setHarvestersHandler(HarvestersHandler harvestersHandler) {
		this.harvestersHandler = harvestersHandler;
	}

	// ----------------------------------------------------------------------------------------------------------


	@Override
	public KnowldedgeBaseHandler getKnowledgeBaseHandler() {
		return knowledgeBaseHandler;
	}

	// ----------------------------------------------------------------------------------------------------------

}
