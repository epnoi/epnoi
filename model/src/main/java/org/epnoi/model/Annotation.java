package org.epnoi.model;

public class Annotation implements Resource {
	private String uri;
	private PAVProperties pavProperties;
	private String hasTopic;
	private String annotatesResource;
	private String onSourceResource;
	private String predicate;
	private String label;
	

	// ---------------------------------------------------------------------------------

	
	public String getUri() {
		return uri;
	}

	// ---------------------------------------------------------------------------------

	public void setUri(String URI) {
		this.uri = URI;
	}

	// ---------------------------------------------------------------------------------

	public PAVProperties getPavProperties() {
		return pavProperties;
	}

	// ---------------------------------------------------------------------------------

	public void setPavProperties(PAVProperties pavProperties) {
		this.pavProperties = pavProperties;
	}

	// ---------------------------------------------------------------------------------

	public String getHasTopic() {
		return hasTopic;
	}

	// ---------------------------------------------------------------------------------

	public void setHasTopic(String hasTopic) {
		this.hasTopic = hasTopic;
	}

	// ---------------------------------------------------------------------------------

	public String getAnnotatesResource() {
		return annotatesResource;
	}

	// ---------------------------------------------------------------------------------

	public void setAnnotatesResource(String annotatesResource) {
		this.annotatesResource = annotatesResource;
	}

	// ---------------------------------------------------------------------------------

	public String getOnSourceResource() {
		return onSourceResource;
	}

	// ---------------------------------------------------------------------------------

	public void setOnSourceResource(String onSourceResource) {
		this.onSourceResource = onSourceResource;
	}

	// ---------------------------------------------------------------------------------
	
	public String getPredicate() {
		return predicate;
	}

	// ---------------------------------------------------------------------------------
	
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	// ---------------------------------------------------------------------------------
	
	public String getLabel() {
		return label;
	}
	
	// ---------------------------------------------------------------------------------

	public void setLabel(String label) {
		this.label = label;
	}
	
	// ---------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "Annotation [URI=" + uri + ", pavProperties=" + pavProperties
				+ ", hasTopic=" + hasTopic + ", annotatesResource="
				+ annotatesResource + ", onSourceResource=" + onSourceResource
				+ ", predicate=" + predicate + ", label=" + label + "]";
	}

}
