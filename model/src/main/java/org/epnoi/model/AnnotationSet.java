package org.epnoi.model;

import java.util.List;

public class AnnotationSet implements Resource {
	private String uri;
	private PAVProperties pavProperties;
	private List<String> items;

	// ---------------------------------------------------------------------------------



	// ---------------------------------------------------------------------------------

	public PAVProperties getPavProperties() {
		return pavProperties;
	}

	// ---------------------------------------------------------------------------------

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setPavProperties(PAVProperties pavProperties) {
		this.pavProperties = pavProperties;
	}

	// ---------------------------------------------------------------------------------

	public List<String> getItems() {
		return items;
	}

	// ---------------------------------------------------------------------------------

	public void setItems(List<String> items) {
		this.items = items;
	}

}
