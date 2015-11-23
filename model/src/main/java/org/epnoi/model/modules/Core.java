package org.epnoi.model.modules;

import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.parameterization.ParametersModel;

public interface Core {


	void init() throws EpnoiInitializationException;

	NLPHandler getNLPHandler();

	void setNLPHandler(NLPHandler nlpHandler);



	InformationSourcesHandler getInformationSourcesHandler();

	void setInformationSourcesHandler(InformationSourcesHandler informationSourcesHandler);

	SearchHandler getSearchHandler();
	@Deprecated
	void setSearchHandler(SearchHandler searchHandler);

	//boolean checkStatus(String informationStoreURI);

	void close();

	AnnotationHandler getAnnotationHandler();
	@Deprecated
	void setAnnotationHandler(AnnotationHandler annotationHandler);
	
	@Deprecated
	EventBus getEventBus();
	@Deprecated
	void setEventBus(EventBus eventBus);

	DomainsHandler getDomainsHandler();
	@Deprecated
	void setDomainsHandler(DomainsHandler domainsHandler);

	
	ParametersModel getParameters();
	@Deprecated
	HarvestersHandler getHarvestersHandler();
	@Deprecated
	void setHarvestersHandler(HarvestersHandler harvestersHandler);

	KnowldedgeBaseHandler getKnowledgeBaseHandler();

	InformationHandler getInformationHandler();

	// ----------------------------------------------------------------------------------------------------------
}