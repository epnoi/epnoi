package org.epnoi.uia.search.select;

import java.util.List;

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
	
	public SearchSelectResult select(SelectExpression selectExpression) {
		
		InformationStore informationStore=core.getInformationStoresByType(InformationStoreHelper.RDF_INFORMATION_STORE).get(0);
		List<String> queryResult = informationStore.query(selectExpression.getSparqlExpression());
		
		return new SearchSelectResult(queryResult);
	
	}
}
