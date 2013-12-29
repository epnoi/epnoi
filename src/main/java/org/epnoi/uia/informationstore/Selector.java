package org.epnoi.uia.informationstore;

import java.util.Map;

public class Selector {
	Map<String, String> properties;

	// -------------------------------------------------------------------------------

	public Map<String, String> getProperties() {
		return properties;
	}

	// -------------------------------------------------------------------------------

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	// -------------------------------------------------------------------------------

	public String getProperty(String propertyName) {
		return this.properties.get(propertyName);
	}

}
