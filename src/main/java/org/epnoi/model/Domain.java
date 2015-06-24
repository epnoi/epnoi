package org.epnoi.model;

public class Domain implements Resource {

	private String URI;
	private String label;
	// Intensional definition
	private String consideredResource; // Type of the considered resources in
										// the domain
	private String expression;// Expression that defines such resources

	// Extensional definition
	private String resources; // Each domain can define an aggregation
										// of the resources that belong to it in
										// an explicit manner

	
	// -------------------------------------------------------------------------------------------
	
	@Override
	public String getURI() {
		return this.URI;
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
	public void setURI(String URI) {
		this.URI = URI;
	}

	// -------------------------------------------------------------------------------------------

	public String getConsideredResource() {
		return consideredResource;
	}

	// -------------------------------------------------------------------------------------------

	public void setConsideredResource(String consideredResource) {
		this.consideredResource = consideredResource;
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

	@Override
	public String toString() {
		return "Domain [URI=" + URI + ", label=" + label
				+ ", consideredResource=" + consideredResource
				+ ", expression=" + expression + ", resources=" + resources
				+ "]";
	}

	// -------------------------------------------------------------------------------------------


}
