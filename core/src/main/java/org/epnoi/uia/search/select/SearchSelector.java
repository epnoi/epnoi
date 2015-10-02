package org.epnoi.uia.search.select;

import org.epnoi.model.search.SearchContext;
import org.epnoi.model.search.SearchSelectResult;
import org.epnoi.model.search.SelectExpression;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreHelper;

public class SearchSelector {
	
	private Core core;
	
	// -------------------------------------------------------------------------------------------------
	
	public SearchSelector(Core core) {
		this.core=core;
	}
	
	// -------------------------------------------------------------------------------------------------
	
	public SearchSelectResult select(SelectExpression selectExpression, SearchContext searchContext) {
		
		if (selectExpression.getSparqlExpression()!=null&&selectExpression.getSparqlExpression().length()>0){
			System.out.println("SPARQL handling should be here");
		}
		/*
		InformationStore informationStore=core.getInformationStoresByType(InformationStoreHelper.RDF_INFORMATION_STORE).get(0);
		List<String> queryResult = informationStore.query(selectExpression.getSparqlExpression());
		*/
		
		
		InformationStore informationStore=core.getInformationStoresByType(InformationStoreHelper.SOLR_INFORMATION_STORE).get(0);
		SearchSelectResult searchSelectResult =informationStore.query(selectExpression, searchContext);

		
		//System.out.println("SSR -> "+searchSelectResult);
		
		return searchSelectResult;
	
	}
}
