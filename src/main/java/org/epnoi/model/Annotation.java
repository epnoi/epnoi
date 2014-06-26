package org.epnoi.model;

import javax.xml.bind.annotation.XmlElement;

public class Annotation implements Resource {
	private String URI;
	private PAVProperties pavProperties;
	private String hasTopic;
	private String annotatesResource;
	private String onSourceResource;

	// ---------------------------------------------------------------------------------

	@XmlElement(name = "URI")
	public String getURI() {
		return URI;
	}

	// ---------------------------------------------------------------------------------

	public void setURI(String URI) {
		this.URI = URI;
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

	@Override
	public String toString() {
		return "Annotation [URI=" + URI + ", pavProperties=" + pavProperties
				+ ", hasTopic=" + hasTopic + ", annotatesResource="
				+ annotatesResource + ", onSourceResource=" + onSourceResource
				+ "]";
	}

}
