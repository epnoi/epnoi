package org.epnoi.uia.learner;

import java.util.HashMap;

public class OntologyLearningParameters {
/**
 *  NUMBER_INITIAL_TERMS: number of initial terms in the ontology
	
	double HYPERNYM_RELATION_THRESHOLD: Probabiliy Threshold
	List<String> CONSIDERED_DOMAINS = "CONSIDERED_DOMAINS";
	String TARGET_DOMAIN = "TARGET_DOMAIN";
	boolean EXTRACT_TERMS = "EXTRACT_TERMS";	
 */
	
	public static final String NUMBER_INITIAL_TERMS = "NUMBER_INITIAL_TERMS"; //number of initial terms in the ontology
	
	public static final String HYPERNYM_RELATION_THRESHOLD = "HYPERNYM_RELATION_THRESHOLD"; //Probabiliy Threshold
	public static final String CONSIDERED_DOMAINS = "CONSIDERED_DOMAINS";
	public static final String CONSIDERED_RESOURCES = "CONSIDERED_RESOURCES";
	
	public static final String TARGET_DOMAIN = "TARGET_DOMAIN";
	public static final String EXTRACT_TERMS = "EXTRACT_TERMS";
	

	private HashMap<String, Object> parameters = new HashMap<String, Object>();

	// ---------------------------------------------------------------------------------------------

	public void setParameter(String parameter, Object value) {
		this.parameters.put(parameter, value);

	}

	// ---------------------------------------------------------------------------------------------

	public Object getParameterValue(String parameter) {
		return this.parameters.get(parameter);
	}
}
