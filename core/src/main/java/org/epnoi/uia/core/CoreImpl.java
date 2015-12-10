package org.epnoi.uia.core;

import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.*;
import org.epnoi.model.parameterization.ParametersModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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

    @Autowired(required = false)
    @Deprecated
    private InformationSourcesHandler informationSourcesHandler;

    @Autowired (required = false)
    @Deprecated
    private HarvestersHandler harvestersHandler;


    public CoreImpl() {
    }

    @PostConstruct
    @Override
    public synchronized void init() throws EpnoiInitializationException {
        logger.info(
                "\n =================================================================================================== \n starting epnoi! \n ===================================================================================================");
        logger.info("Initializing the epnoi uia core with the following parameters ");
        logger.info(parametersModel.toString());


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


    // ----------------------------------------------------------------------------------------------------------

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
    public void close() {


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
