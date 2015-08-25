package org.epnoi.uia.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.uia.annotation.AnnotationHandler;
import org.epnoi.uia.annotation.AnnotationHandlerImpl;
import org.epnoi.uia.core.eventbus.EventBus;
import org.epnoi.uia.domains.DomainsHandler;
import org.epnoi.uia.harvester.HarvestersHandler;
import org.epnoi.uia.harvester.rss.RSSHarvester;
import org.epnoi.uia.hoarder.RSSHoarder;
import org.epnoi.uia.informationhandler.InformationHandler;
import org.epnoi.uia.informationhandler.InformationHandlerImp;
import org.epnoi.uia.informationsources.InformationSourcesHandler;
import org.epnoi.uia.informationsources.InformationSourcesHandlerImpl;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreFactory;
import org.epnoi.uia.informationstore.InformationStoreHelper;
import org.epnoi.uia.knowledgebase.KnowledgeBaseHandler;
import org.epnoi.uia.nlp.NLPHandler;
import org.epnoi.uia.parameterization.CassandraInformationStoreParameters;
import org.epnoi.uia.parameterization.MapInformationStoreParameters;
import org.epnoi.uia.parameterization.ParametersModel;
import org.epnoi.uia.parameterization.RSSHarvesterParameters;
import org.epnoi.uia.parameterization.RSSHoarderParameters;
import org.epnoi.uia.parameterization.SOLRInformationStoreParameters;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;
import org.epnoi.uia.search.SearchHandler;

public class Core {

	private static final Logger logger = Logger.getLogger(Core.class.getName());

	private HashMap<String, InformationStore> informationStores;
	private HashMap<String, List<InformationStore>> informationStoresByType;

	private RSSHoarder rssHoarder;
	private RSSHarvester rssHarvester;
	private InformationHandler informationHandler;
	private InformationSourcesHandler informationSourcesHandler = null;

	private ParametersModel parametersModel = null;

	private SearchHandler searchHandler = null;
	private AnnotationHandler annotationHandler = null;
	private DomainsHandler domainsHandler = null;
	private HarvestersHandler harvestersHandler = null;
	private EventBus eventBus = null;
	private KnowledgeBaseHandler knowledgeBaseHandler = null;
	private NLPHandler NLPHandler = null;

	/**
	 * The initialization method for the epnoiCore
	 * 
	 * @param initializationProperties
	 *            The properties that define the characteristics of the
	 *            epnoiCore.
	 */

	public synchronized void init(ParametersModel parametersModel)
			throws EpnoiInitializationException {
		logger.info("\n =================================================================================================== \n starting epnoi! \n ===================================================================================================");
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
		this._hoardersInitialization();
		this._harvestersInitialization();
		this._knowedlgeBaseHandlerInitialization();
		logger.info("");
		logger.info("");
		logger.info("===================================================================================================");
		logger.info("");
		logger.info("");
	}

	private void _initNLPHandler() {
	
		
	}

	// ----------------------------------------------------------------------------------------------------------

	private void _initDomainsHandler() {
		this.domainsHandler = new DomainsHandler();
		this.domainsHandler.init(this);

	}

	private void _initEventBus() {

		logger.info("Initializing the Event Bus");
		this.eventBus = new EventBus();
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
					.buildInformationStore(virtuosoInformationStoreParameters,
							parametersModel);

			this.informationStores.put(
					virtuosoInformationStoreParameters.getURI(),
					newInformationStore);

			_addInformationStoreByType(newInformationStore,
					InformationStoreHelper.RDF_INFORMATION_STORE);
			logger.info("The status of the information source is "
					+ newInformationStore.test());

		}
		logger.info("Initializing SOLR information stores");
		for (SOLRInformationStoreParameters solrInformationStoreParameters : parametersModel
				.getSolrInformationStore()) {
			logger.info(solrInformationStoreParameters.toString());

			InformationStore newInformationStore = InformationStoreFactory
					.buildInformationStore(solrInformationStoreParameters,
							parametersModel);

			this.informationStores.put(solrInformationStoreParameters.getURI(),
					newInformationStore);

			_addInformationStoreByType(newInformationStore,
					InformationStoreHelper.SOLR_INFORMATION_STORE);
			logger.info("The status of the information source is "
					+ newInformationStore.test());

		}
		logger.info("Initializing Cassandra information stores");
		for (CassandraInformationStoreParameters cassandraInformationStoreParameters : parametersModel
				.getCassandraInformationStore()) {
			logger.info(cassandraInformationStoreParameters.toString());

			InformationStore newInformationStore = InformationStoreFactory
					.buildInformationStore(cassandraInformationStoreParameters,
							parametersModel);

			this.informationStores.put(
					cassandraInformationStoreParameters.getURI(),
					newInformationStore);

			_addInformationStoreByType(newInformationStore,
					InformationStoreHelper.CASSANDRA_INFORMATION_STORE);
			logger.info("The status of the information source is "
					+ newInformationStore.test());

		}
		logger.info("Initializing map information stores");
		for (MapInformationStoreParameters mapInformationStoreParameters : parametersModel
				.getMapInformationStore()) {
			logger.info(mapInformationStoreParameters.toString());

			InformationStore newInformationStore = InformationStoreFactory
					.buildInformationStore(mapInformationStoreParameters,
							parametersModel);

			this.informationStores.put(mapInformationStoreParameters.getURI(),
					newInformationStore);

			_addInformationStoreByType(newInformationStore,
					InformationStoreHelper.MAP_INFORMATION_STORE);
			logger.info("The status of the information source is "
					+ newInformationStore.test());

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

	private void _addInformationStoreByType(InformationStore informationStore,
			String type) {
		List<InformationStore> informationsStoresOfType = this.informationStoresByType
				.get(type);
		if (informationsStoresOfType == null) {
			informationsStoresOfType = new ArrayList<InformationStore>();
			this.informationStoresByType.put(type, informationsStoresOfType);
		}
		informationsStoresOfType.add(informationStore);
	}

	// ----------------------------------------------------------------------------------------------------------

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

	// ----------------------------------------------------------------------------------------------------------

	private void _harvestersInitialization()
			throws EpnoiInitializationException {
		logger.info("Initializing the HarvestersHandler");

		this.harvestersHandler = new HarvestersHandler();
		this.harvestersHandler.init(this);

		logger.info("Initializing the RSSHarvester");
		RSSHarvesterParameters parameters = this.parametersModel
				.getRssHarvester();

		if (parameters != null) {
			this.rssHarvester = new RSSHarvester(this, parameters);
			this.rssHarvester.start();
		} else {
			logger.info("There was no RSSHarvester defined in the configuration file");
		}
	}

	// ----------------------------------------------------------------------------------------------------------

	private void _initSearchHandler() {
		this.searchHandler = new SearchHandler(this);
	}

	// ----------------------------------------------------------------------------------------------------------

	public Collection<InformationStore> getInformationStores() {
		return this.informationStores.values();
	}

	// ----------------------------------------------------------------------------------------------------------

	public List<InformationStore> getInformationStoresByType(String type) {
		return this.informationStoresByType.get(type);
	}

	// ----------------------------------------------------------------------------------------------------------

	public InformationHandler getInformationHandler() {
		return this.informationHandler;
	}

	// ----------------------------------------------------------------------------------------------------------

	public InformationSourcesHandler getInformationSourcesHandler() {
		return informationSourcesHandler;
	}

	// ----------------------------------------------------------------------------------------------------------

	public void setInformationSourcesHandler(
			InformationSourcesHandler informationSourcesHandler) {
		this.informationSourcesHandler = informationSourcesHandler;
	}

	// ----------------------------------------------------------------------------------------------------------

	public SearchHandler getSearchHandler() {
		return searchHandler;
	}

	// ----------------------------------------------------------------------------------------------------------

	public void setSearchHandler(SearchHandler searchHandler) {
		this.searchHandler = searchHandler;
	}

	// ----------------------------------------------------------------------------------------------------------

	public boolean checkStatus(String informationStoreURI) {
		InformationStore informationStore = this.informationStores
				.get(informationStoreURI);
		return informationStore.test();
	}

	// ----------------------------------------------------------------------------------------------------------

	public void close() {
		for (InformationStore dataSource : this.informationStores.values()) {
			dataSource.close();
		}

	}

	// ----------------------------------------------------------------------------------------------------------

	public AnnotationHandler getAnnotationHandler() {
		return annotationHandler;
	}

	// ----------------------------------------------------------------------------------------------------------

	public void setAnnotationHandler(AnnotationHandler annotationHandler) {
		this.annotationHandler = annotationHandler;
	}

	// ----------------------------------------------------------------------------------------------------------

	public EventBus getEventBus() {
		return eventBus;
	}

	// ----------------------------------------------------------------------------------------------------------

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	

	// ----------------------------------------------------------------------------------------------------------

	private void _knowedlgeBaseHandlerInitialization() {
		this.knowledgeBaseHandler = new KnowledgeBaseHandler();
		this.knowledgeBaseHandler.init(this);

	}

	// ----------------------------------------------------------------------------------------------------------

	public DomainsHandler getDomainsHandler() {
		return domainsHandler;
	}

	// ----------------------------------------------------------------------------------------------------------

	public void setDomainsHandler(DomainsHandler domainsHandler) {
		this.domainsHandler = domainsHandler;
	}

	// ----------------------------------------------------------------------------------------------------------

	public ParametersModel getParameters() {
		return this.parametersModel;
	}

	// ----------------------------------------------------------------------------------------------------------

	public HarvestersHandler getHarvestersHandler() {
		return harvestersHandler;
	}

	// ----------------------------------------------------------------------------------------------------------

	public void setHarvestersHandler(HarvestersHandler harvestersHandler) {
		this.harvestersHandler = harvestersHandler;
	}

	// ----------------------------------------------------------------------------------------------------------

	public KnowledgeBaseHandler getKnowledgeBaseHandler() {
		return knowledgeBaseHandler;
	}

	// ----------------------------------------------------------------------------------------------------------

}
