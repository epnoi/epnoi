package org.epnoi.uia.learner;

import java.util.HashMap;

public class OntologyLearningParameters {
	public static final String NUMBER_INITIAL_TERMS = "NUMBER_INITIAL_TERMS";
	public static final String HYPERNYM_RELATION_THRESHOLD = "HYPERNYM_RELATION_THRESHOLD";
	private HashMap<String, String> parameters = new HashMap<String, String>();

	//---------------------------------------------------------------------------------------------
	
	public void setParameter(String parameter, String value) {
		this.parameters.put(parameter, value);

	}


	//---------------------------------------------------------------------------------------------
	
	public String getParameterValue(String parameter) {
		return this.parameters.get(parameter);
	}
}
