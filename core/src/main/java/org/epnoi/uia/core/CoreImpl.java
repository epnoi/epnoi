package org.epnoi.uia.core;

import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.*;
import org.epnoi.model.parameterization.*;
import org.epnoi.uia.core.eventbus.EventBusFactory;
import org.epnoi.uia.informationstore.InformationStoreFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

@Component
public class CoreImpl implements Core {

    private static final Logger logger = Logger.getLogger(CoreImpl.class.getName());
    @Autowired
    private ParametersModel parametersModel;

    @Autowired
    private NLPHandler nlpHandler;

    @Autowired
    private SearchHandler searchHandler;

    @Autowired
    private AnnotationHandler annotationHandler;

    @Autowired
    private InformationHandler informationHandler;

    @Autowired
    private DomainsHandler domainsHandler = null;

    @Autowired
    private KnowldedgeBaseHandler knowledgeBaseHandler;

    @Autowired
    private InformationSourcesHandler informationSourcesHandler;

    //  @Autowired
    @Deprecated
    private HarvestersHandler harvestersHandler;

    private HashMap<String, InformationStore> informationStores;
    private HashMap<String, List<InformationStore>> informationStoresByType;


    private EventBus eventBus = null;


    public CoreImpl() {
    }

    @PostConstruct
    @Override
    public synchronized void init() throws EpnoiInitializationException {
        logger.info(
                "\n =================================================================================================== \n starting epnoi! \n ===================================================================================================");
        logger.info("Initializing the epnoi uia core with the following parameters ");
        logger.info(parametersModel.toString());
        this.informationStores = new HashMap<>();
        this.informationStoresByType = new HashMap<>();

        this._initEventBus();
        this._informationStoresInitialization();

        logger.info("");
        logger.info("");
        logger.info(
                "===================================================================================================");
        logger.info("");
        logger.info("");
    }


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

    // ---------------------------------------------------------------------------------------------------------


    private void _initEventBus() {

        logger.info("Initializing the Event Bus");
        this.eventBus = EventBusFactory.newInstance(parametersModel);
        this.eventBus.init();
    }

    // ----------------------------------------------------------------------------------------------------------

    /**
     * Information Stores initialization
     */

    private void _informationStoresInitialization() {

        logger.info("Initializing information stores");
        logger.info("Initializing Virtuoso information stores");
        for (VirtuosoInformationStoreParameters virtuosoInformationStoreParameters : parametersModel
                .getVirtuosoInformationStore()) {
            _initVirtuosoInformationStore(virtuosoInformationStoreParameters);

        }
        logger.info("Initializing SOLR information stores");
        for (SOLRInformationStoreParameters solrInformationStoreParameters : parametersModel
                .getSolrInformationStore()) {
            _initSOLRInformationStore(solrInformationStoreParameters);

        }
        logger.info("Initializing Cassandra information stores");
        for (CassandraInformationStoreParameters cassandraInformationStoreParameters : parametersModel
                .getCassandraInformationStore()) {
            _initCassandraInformationStore(cassandraInformationStoreParameters);

        }
        logger.info("Initializing map information stores");
        for (MapInformationStoreParameters mapInformationStoreParameters : parametersModel.getMapInformationStore()) {
            _initMapInformationStore(mapInformationStoreParameters);

        }

    }

    private void _initMapInformationStore(MapInformationStoreParameters mapInformationStoreParameters) {
        logger.info(mapInformationStoreParameters.toString());
        InformationStore newInformationStore = null;
        try {
            newInformationStore = InformationStoreFactory
                    .buildInformationStore(mapInformationStoreParameters, parametersModel);
            logger.info("The status of the information source is " + newInformationStore.test());
        } catch (Exception e) {
            logger.severe("Something went wrong in the MapInfomration store");
        }
        this.informationStores.put(mapInformationStoreParameters.getURI(), newInformationStore);

        _addInformationStoreByType(newInformationStore, InformationStoreHelper.MAP_INFORMATION_STORE);

    }

    private void _initCassandraInformationStore(CassandraInformationStoreParameters cassandraInformationStoreParameters) {
        logger.info(cassandraInformationStoreParameters.toString());

        InformationStore newInformationStore = null;
        try {
            newInformationStore = InformationStoreFactory
                    .buildInformationStore(cassandraInformationStoreParameters, parametersModel);
            logger.info("The status of the information source is " + newInformationStore.test());
        } catch (Exception e) {
            logger.severe("Something went wrong in the CassandraInformationStore initialization!");
           // e.printStackTrace();
        }
        this.informationStores.put(cassandraInformationStoreParameters.getURI(), newInformationStore);

        _addInformationStoreByType(newInformationStore, InformationStoreHelper.CASSANDRA_INFORMATION_STORE);

    }

    private void _initSOLRInformationStore(SOLRInformationStoreParameters solrInformationStoreParameters) {
        logger.info(solrInformationStoreParameters.toString());
        InformationStore newInformationStore = null;
        try {
            newInformationStore = InformationStoreFactory
                    .buildInformationStore(solrInformationStoreParameters, parametersModel);
            logger.info("The status of the information source is " + newInformationStore.test());
        } catch (Exception e) {
            logger.severe("Something went wrong in the SOLRInformationStore initialization!");
            //e.printStackTrace();
        }
        this.informationStores.put(solrInformationStoreParameters.getURI(), newInformationStore);

        _addInformationStoreByType(newInformationStore, InformationStoreHelper.SOLR_INFORMATION_STORE);

    }

    private void _initVirtuosoInformationStore(VirtuosoInformationStoreParameters virtuosoInformationStoreParameters) {
        logger.info(virtuosoInformationStoreParameters.toString());
        InformationStore newInformationStore = null;
        try {
            newInformationStore = InformationStoreFactory
                    .buildInformationStore(virtuosoInformationStoreParameters, parametersModel);
            logger.info("The status of the information source is " + newInformationStore.test());
        } catch (Exception e) {
            logger.severe("Something went wrong in the VirtuosoInfomrationStore initialization!");
          //  e.printStackTrace();
        }
        this.informationStores.put(virtuosoInformationStoreParameters.getURI(), newInformationStore);

        _addInformationStoreByType(newInformationStore, InformationStoreHelper.RDF_INFORMATION_STORE);


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
