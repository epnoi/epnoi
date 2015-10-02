package org.epnoi.model.search;

public class SelectExpression {
	String sparqlExpression;
	String solrExpression;
	String informationUnitClass;
	
	public String getSparqlExpression() {
		return sparqlExpression;
	}
	
	//-----------------------------------------------------------------------

	public void setSparqlExpression(String sparqlExpression) {
		this.sparqlExpression = sparqlExpression;
	}
	
	//-----------------------------------------------------------------------

	public String getSolrExpression() {
		return solrExpression;
	}

	//-----------------------------------------------------------------------
	
	public void setSolrExpression(String solrExpression) {
		this.solrExpression = solrExpression;
	}
	
	//-----------------------------------------------------------------------

	public String getInformationUnitClass() {
		return informationUnitClass;
	}
	
	//-----------------------------------------------------------------------

	public void setInformationUnitClass(String informationUnitClass) {
		this.informationUnitClass = informationUnitClass;
	}

	//-----------------------------------------------------------------------

	@Override
	public String toString() {
		return "SelectExpression [sparqlExpression=" + sparqlExpression
				+ ", solrExpression=" + solrExpression
				+ ", informationUnitClass=" + informationUnitClass + "]";
	}

}
