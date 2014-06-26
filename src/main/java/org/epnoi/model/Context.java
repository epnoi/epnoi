package org.epnoi.model;

import java.util.HashMap;
import java.util.Map;

public class Context {
	public static final String INFORMATION_SOURCE_URI= "INFORMATION_SOURCE_URI";
	public static final String INFORMATION_SOURCE_NAME= "INFORMATION_SOURCE_NAME";
	Map<String, String> parameters = new HashMap<String, String>();
	Map<String, Object> elements = new HashMap<String, Object>();
	

	// ---------------------------------------------------------------

	public Map<String, Object> getElements() {
		return elements;
	}

	// ---------------------------------------------------------------

	public void setElements(Map<String, Object> elements) {
		this.elements = elements;
	}

	// ---------------------------------------------------------------

	public Map<String, String> getParameters() {
		return parameters;
	}
	
	// ---------------------------------------------------------------

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	
	// ---------------------------------------------------------------

	@Override
	public String toString() {
		return "Context [parameters=" + parameters + ", elements=" + elements
				+ "]";
	}

}
