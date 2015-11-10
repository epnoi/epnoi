package org.epnoi.model;

import java.util.*;

public class DublinCoreMetadataElementsSet {

	//@JsonProperty("dublinCoreProperties")
	private Map<String, List<String>> dublinCoreProperties = new HashMap<>();

	// --------------------------------------------------------------------------

	public Map<String, List<String>> getDublinCoreProperties() {
		return dublinCoreProperties;
	}

	// --------------------------------------------------------------------------

	public void setDublinCoreProperties(
			Map<String, List<String>> dublinCoreProperties) {
		this.dublinCoreProperties = dublinCoreProperties;
	}

	// --------------------------------------------------------------------------

	public void addPropertyValue(String propertyURI, String value) {

		List<String> values = this.dublinCoreProperties.get(propertyURI);
		if (values == null) {
			values = new ArrayList<String>();
			this.dublinCoreProperties.put(propertyURI, values);
		}
		values.add(value);

	}

	// --------------------------------------------------------------------------

	//@JsonIgnore
	public List<String> getPropertyValues(String propertyURI) {
		return this.dublinCoreProperties.get(propertyURI);
	}

	// --------------------------------------------------------------------------

	//@JsonIgnore
	public String getPropertyFirstValue(String propertyURI) {

		return this.dublinCoreProperties.get(propertyURI).get(0);
	}

	// --------------------------------------------------------------------------
	//@JsonIgnore
	public Collection<String> getProperties() {
		return this.dublinCoreProperties.keySet();
	}

	// --------------------------------------------------------------------------

	@Override
	public String toString() {
		return "DublinCoreMetadataElementsSet [dublinCoreProperties="
				+ dublinCoreProperties + "]";
	}

	// --------------------------------------------------------------------------

}
