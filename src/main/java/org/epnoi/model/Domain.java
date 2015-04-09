package org.epnoi.model;

public class Domain implements Resource {

	String URI;
	String consideredResource;
	String expression;

	//-------------------------------------------------------------------------------------------
	
	@Override
	public String getURI() {
		return this.URI;
	}

	//-------------------------------------------------------------------------------------------
	
	@Override
	public void setURI(String URI) {
		this.URI = URI;
	}
	
	//-------------------------------------------------------------------------------------------

	public String getConsideredResource() {
		return consideredResource;
	}
	
	//-------------------------------------------------------------------------------------------

	public void setConsideredResource(String consideredResource) {
		this.consideredResource = consideredResource;
	}
	
	//-------------------------------------------------------------------------------------------

	public String getExpression() {
		return expression;
	}
	
	//-------------------------------------------------------------------------------------------

	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	//-------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "Domain [URI=" + URI + ", consideredResource="
				+ consideredResource + ", expression=" + expression + "]";
	}
	
	//-------------------------------------------------------------------------------------------
	
	

}
