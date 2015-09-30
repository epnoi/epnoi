package org.epnoi.model;

import java.util.List;

public class AnnotationSet implements Resource {
	private String URI;
	private PAVProperties pavProperties;
	private List<String> items;

	// ---------------------------------------------------------------------------------

	public String getURI() {
		return URI;
	}

	// ---------------------------------------------------------------------------------

	public void setURI(String URI) {
		URI = URI;
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

	public List<String> getItems() {
		return items;
	}

	// ---------------------------------------------------------------------------------

	public void setItems(List<String> items) {
		this.items = items;
	}

}
