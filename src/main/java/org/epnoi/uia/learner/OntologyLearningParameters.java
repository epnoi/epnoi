package org.epnoi.uia.learner;

import org.epnoi.uia.commons.Parameters;

public class OntologyLearningParameters extends Parameters {
	/**
	 * NUMBER_INITIAL_TERMS: number of initial terms in the ontology
	 * 
	 * double HYPERNYM_RELATION_THRESHOLD: Probabiliy Threshold List<String>
	 * CONSIDERED_DOMAINS = "CONSIDERED_DOMAINS"; String TARGET_DOMAIN =
	 * "TARGET_DOMAIN"; boolean EXTRACT_TERMS = "EXTRACT_TERMS";
	 */

	public static final String NUMBER_INITIAL_TERMS = "NUMBER_INITIAL_TERMS"; // number
																				// of
																				// initial
																				// terms
																				// in
																				// the
																				// ontology

	public static final String HYPERNYM_RELATION_EXPANSION_THRESHOLD = "HYPERNYM_RELATION_EXPANSION_THRESHOLD";
	// Minimum probability for a detected hypernym relation to be consider for
	// being expanded in the ontology learning process

	public static final String CONSIDERED_DOMAINS = "CONSIDERED_DOMAINS";
	// The domains considered in the ontology learning process. It includes the
	// target domain plus the ones used as a reference.

	public static final String CONSIDERED_RESOURCES = "CONSIDERED_RESOURCES";
	// RDF class of the resources that we consider in the ontology learning
	// process

	public static final String TARGET_DOMAIN = "TARGET_DOMAIN";
	// Domain that is the target of the ontology learning process, the learned ontology represents this domain.
	public static final String EXTRACT_TERMS = "EXTRACT_TERMS";

	public static final String HYPERNYM_MODEL_PATH = "/JUNK/drinventorcorpus/hypernymsmodel";

	// ---------------------------------------------------------------------------------------------

	// ---------------------------------------------------------------------------------------------

}
