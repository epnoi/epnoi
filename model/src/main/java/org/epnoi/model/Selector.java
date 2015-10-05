package org.epnoi.model;

import java.util.HashMap;
import java.util.Map;

public class Selector {
	Map<String, String> properties;
	
	// -------------------------------------------------------------------------------
	
	public Selector(){
		this.properties = new HashMap<String, String>();
	}

	// -------------------------------------------------------------------------------

	public Map<String, String> getProperties() {
		return properties;
	}

	// -------------------------------------------------------------------------------

	public void setProperty(String property, String value) {
		this.properties.put(property, value);
	}

	// -------------------------------------------------------------------------------

	public String getProperty(String propertyName) {
		return this.properties.get(propertyName);
	}
	
	// -------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "Selector [properties=" + properties + "]";
	}
	
	// -------------------------------------------------------------------------------
	
}
