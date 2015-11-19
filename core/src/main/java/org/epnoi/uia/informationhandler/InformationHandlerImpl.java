package org.epnoi.uia.informationhandler;

import org.epnoi.informationhandler.wrappers.Wrapper;
import org.epnoi.informationhandler.wrappers.WrapperFactory;
import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.model.Selector;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.*;
import org.epnoi.model.parameterization.*;
import org.epnoi.uia.informationstore.InformationStoreFactory;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.VirtuosoInformationStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

@Component
public class InformationHandlerImpl implements InformationHandler {
    private static final Logger logger = Logger.getLogger(InformationHandlerImpl.class.getName());
    @Autowired
    private Core core;

    @Autowired
    private ParametersModel parameters;
    private WrapperFactory wrapperFactory;

    private List<InformationAccessListener> listeners;

    private HashMap<String, InformationStore> informationStores;
    private HashMap<String, List<InformationStore>> informationStoresByType;


    // ---------------------------------------------------------------------------

    public InformationHandlerImpl() {

    }

    @Override
    public boolean checkStatus(String informationStoreURI) {
        InformationStore informationStore = this.informationStores.get(informationStoreURI);
        return informationStore.test();
    }


    @PostConstruct
    public void init() throws EpnoiInitializationException {
        logger.info("Initializing the Information Handler");
        this.wrapperFactory = new WrapperFactory(core);
        this.listeners = new ArrayList<InformationAccessListener>();
        this.informationStores = new HashMap<>();
        this.informationStoresByType = new HashMap<>();
        this._informationStoresInitialization();
    }

    // ---------------------------------------------------------------------------



    public void close() {
        for (InformationStore dataSource : this.informationStores.values()) {
            dataSource.close();
        }

    }

    public void update(Resource resource) {
        Wrapper wrapper = this.wrapperFactory.build(resource);
        wrapper.update(resource);

    }

    // ---------------------------------------------------------------------------

    public void put(Resource resource, Context context) {
        Wrapper wrapper = this.wrapperFactory.build(resource);
        wrapper.put(resource, context);
        resource = null;
        context.clear();

    }

    // ---------------------------------------------------------------------------

    public Resource get(String URI) {
        // TODO: As it is now it just delivers items/feeds
        Resource resource = null;

        String resourceType = this.getType(URI);
        if (resourceType != null) {

            Wrapper wrapper = this.wrapperFactory.build(resourceType);
            resource = wrapper.get(URI);

        }
        return resource;
    }

    // ---------------------------------------------------------------------------

    public Resource get(String URI, String resourceType) {
        Wrapper wrapper = this.wrapperFactory.build(resourceType);
        return wrapper.get(URI);
    }

    // ---------------------------------------------------------------------------

    public void remove(String URI, String resourceType) {
        Wrapper wrapper = this.wrapperFactory.build(resourceType);
        wrapper.remove(URI);
    }

    // ---------------------------------------------------------------------------

    public void remove(Resource resource) {
        Wrapper wrapper = this.wrapperFactory.build(resource);
        wrapper.remove(resource.getUri());

    }

    // ---------------------------------------------------------------------------

    public void init(ParametersModel parameters) {
        // TODO Auto-generated method stub

    }

    // ---------------------------------------------------------------------------

    public void addInformationStore(InformationStore informationStore) {
        // TODO Auto-generated method stub

    }

    // ---------------------------------------------------------------------------

    public void removeInformationStore(String URI) {
        // TODO Auto-generated method stub

    }

    // ---------------------------------------------------------------------------

    public synchronized void publish(String eventType, Resource source) {
        for (InformationAccessListener listener : this.listeners) {
            listener.notify(eventType, source);
        }
    }

    // ---------------------------------------------------------------------------

    public synchronized void subscribe(InformationAccessListener listener,
                                       String subscriptionExpression) {
        this.listeners.add(listener);
    }

    // ---------------------------------------------------------------------------

    @Override
    public Content<String> getContent(Selector selector) {
        Wrapper wrapper = this.wrapperFactory.build(selector
                .getProperty(SelectorHelper.TYPE));
        Content<String> content = wrapper.getContent(selector);

        return content;
    }

    // ---------------------------------------------------------------------------

    @Override
    public Content<Object> getAnnotatedContent(Selector selector) {
        Wrapper wrapper = this.wrapperFactory.build(selector
                .getProperty(SelectorHelper.TYPE));

        Content<Object> content = wrapper.getAnnotatedContent(selector);
        return content;
    }

    // ---------------------------------------------------------------------------

    @Override
    public void setContent(Selector selector, Content<String> content) {
        Wrapper wrapper = this.wrapperFactory.build(selector
                .getProperty(SelectorHelper.TYPE));
        wrapper.setContent(selector, content);

    }

    // ---------------------------------------------------------------------------

    @Override
    public void setAnnotatedContent(Selector selector,
                                    Content<Object> annotatedContent) {
        Wrapper wrapper = this.wrapperFactory.build(selector
                .getProperty(SelectorHelper.TYPE));

        wrapper.setAnnotatedContent(selector, annotatedContent);

    }

    // ---------------------------------------------------------------------------

    @Override
    public boolean contains(String URI, String resourceType) {

        Selector selector = new Selector();
        selector.setProperty(SelectorHelper.TYPE, resourceType);
        selector.setProperty(SelectorHelper.URI, URI);
        Wrapper wrapper = this.wrapperFactory.build(resourceType);
        return wrapper.exists(URI);

    }

    // ---------------------------------------------------------------------------

    @Override
    public List<String> getAll(String resourceType) {
        // ------------------------------------------------------------------------------

        InformationStore informationStore = this.core.getInformationHandler()
                .getInformationStoresByType(
                        InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

        String queryExpression = "SELECT DISTINCT ?uri FROM <{GRAPH}>"
                + "{ ?uri a <" + resourceType + "> ." + "}";

        queryExpression = queryExpression
                .replace(
                        "{GRAPH}",
                        ((VirtuosoInformationStoreParameters) informationStore
                                .getParameters()).getGraph());

        System.out.println("QUERY EXPRESSION ----------> " + queryExpression);
        List<String> queryResults = informationStore.query(queryExpression);

        return queryResults;
    }

    // ---------------------------------------------------------------------------

    public String getType(String URI) {
        VirtuosoInformationStore informationStore = (VirtuosoInformationStore) this.core.getInformationHandler()
                .getInformationStoresByType(
                        InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

        return informationStore.getType(URI);
    }


    /**
     * Information Stores initialization
     */

    private void _informationStoresInitialization() {

        logger.info("Initializing information stores");
        logger.info("Initializing Virtuoso information stores");
        for (VirtuosoInformationStoreParameters virtuosoInformationStoreParameters : this.parameters
                .getVirtuosoInformationStore()) {
            _initVirtuosoInformationStore(virtuosoInformationStoreParameters);

        }
        logger.info("Initializing SOLR information stores");
        for (SOLRInformationStoreParameters solrInformationStoreParameters : this.parameters
                .getSolrInformationStore()) {
            _initSOLRInformationStore(solrInformationStoreParameters);

        }
        logger.info("Initializing Cassandra information stores");
        for (CassandraInformationStoreParameters cassandraInformationStoreParameters : this.parameters
                .getCassandraInformationStore()) {
            _initCassandraInformationStore(cassandraInformationStoreParameters);

        }
        logger.info("Initializing map information stores");
        for (MapInformationStoreParameters mapInformationStoreParameters : this.parameters.getMapInformationStore()) {
            _initMapInformationStore(mapInformationStoreParameters);

        }

    }

    private void _initMapInformationStore(MapInformationStoreParameters mapInformationStoreParameters) {
        logger.info(mapInformationStoreParameters.toString());
        InformationStore newInformationStore = null;
        try {
            newInformationStore = InformationStoreFactory
                    .buildInformationStore(mapInformationStoreParameters, this.parameters);
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
                    .buildInformationStore(cassandraInformationStoreParameters, this.parameters);
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
                    .buildInformationStore(solrInformationStoreParameters, this.parameters);
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
                    .buildInformationStore(virtuosoInformationStoreParameters, this.parameters);
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

}
