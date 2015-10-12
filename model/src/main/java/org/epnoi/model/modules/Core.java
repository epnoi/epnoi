package org.epnoi.model.modules;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.parameterization.ParametersModel;

public interface Core {


	void init(ParametersModel parametersModel) throws EpnoiInitializationException;

	NLPHandler getNLPHandler();

	void setNLPHandler(NLPHandler nlpHandler);

	Collection<InformationStore> getInformationStores();

	List<InformationStore> getInformationStoresByType(String type);

	InformationHandler getInformationHandler();

	InformationSourcesHandler getInformationSourcesHandler();

	void setInformationSourcesHandler(InformationSourcesHandler informationSourcesHandler);

	SearchHandler getSearchHandler();
	@Deprecated
	void setSearchHandler(SearchHandler searchHandler);

	boolean checkStatus(String informationStoreURI);

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

}