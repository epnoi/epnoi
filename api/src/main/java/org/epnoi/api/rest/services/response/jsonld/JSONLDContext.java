package org.epnoi.api.rest.services.response.jsonld;

import java.util.HashMap;
import java.util.Map;


public class JSONLDContext {

	public JSONLDContext() {
		contextTerms.put("ore:whatever", new HashMap<String, String>());
	}
	
	
	private Map<String, Map<String,String>> contextTerms = new HashMap<>();

	public Map<String, Map<String, String>> getContextTerms() {
		return contextTerms;
	}

	public void setContextTerms(Map<String, Map<String, String>> contextTerms) {
		this.contextTerms = contextTerms;
	}
	
	
}
