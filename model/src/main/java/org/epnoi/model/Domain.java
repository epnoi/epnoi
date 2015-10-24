package org.epnoi.model;

public class Domain implements Resource {

	private String uri;

	//Extensional definition
	private String type; // Type of the considered resources in
										// the domain
	private String expression="";// Expression that defines such resources

	private String label="";
	//Intensional definition
	private String resources; // Each domain can define an aggregation
										// of the resources that belong to it in
										// an explicit manner

	// -------------------------------------------------------------------------------------------
	
	@Override
	public String getUri() {
		return this.uri;
	}

	// -------------------------------------------------------------------------------------------

	public String getResources() {
		return resources;
	}
	
	// -------------------------------------------------------------------------------------------

	public void setResources(String resources) {
		this.resources = resources;
	}

	// -------------------------------------------------------------------------------------------
	
	@Override
	public void setUri(String uri) {
		this.uri = uri;
	}

	// -------------------------------------------------------------------------------------------

	public String getType() {
		return type;
	}

	// -------------------------------------------------------------------------------------------

	public void setType(String type) {
		this.type = type;
	}

	// -------------------------------------------------------------------------------------------

	public String getExpression() {
		return expression;
	}

	// -------------------------------------------------------------------------------------------

	public void setExpression(String expression) {
		this.expression = expression;
	}

	// -------------------------------------------------------------------------------------------

	public String getLabel() {
		return label;
	}

	// -------------------------------------------------------------------------------------------

	public void setLabel(String label) {
		this.label = label;
	}
	
	// -------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "Domain [URI=" + uri + ", label=" + label + ", type=" + type
				+ ", expression=" + expression + ", resources=" + resources
				+ "]";
	}
	
	// -------------------------------------------------------------------------------------------

}
