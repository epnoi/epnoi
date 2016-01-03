package org.epnoi.learner;

import org.epnoi.model.commons.Parameters;

import java.io.Serializable;

public class LearningParameters extends Parameters<Serializable> implements Serializable {

	// Domain Definition
	// Parameters--------------------------------------------------------------------------------

	public static final String CONSIDERED_DOMAINS = "CONSIDERED_DOMAINS"; // List<Domain>
	// The domains considered in the ontology learning process. It includes the
	// target domain plus the ones used as a reference.

	public static final String TARGET_DOMAIN_URI = "TARGET_DOMAIN_URI";//String (URI of the target domain that must be among the CONSIDERED_DOMAINS
	// Domain that is the target of the ontology learning process, the learned
	// ontology represents this domain.

	// Term Extraction phase
	// ----------------------------------------------------------------------------------------

	// Expansion phase
	// parameters-----------------------------------------------------------------------------------

	public static final String MAX_SOURCE_TARGET_DISTANCE = "MAX_SOURCE_TARGET_DISTANCE";

	public static final String NUMBER_INITIAL_TERMS = "NUMBER_INITIAL_TERMS";
	// number of initial terms in the ontology

	public static final String HYPERNYM_RELATION_EXPANSION_THRESHOLD = "HYPERNYM_RELATION_EXPANSION_THRESHOLD";
	// Minimum probability for a detected hypernym relation to be consider for
	// being expanded in the ontology learning process



	public static final String EXTRACT_TERMS = "EXTRACT_TERMS";

	public static final String OBTAIN_TERMS = "OBTAIN_TERMS";

	public static final String HYPERNYM_MODEL_PATH = "HYPERNYM_MODEL_PATH";

	public static final String HYPERNYM_MODEL = "HYPERNYM_MODEL";

	public static final String HYPERNYM_RELATION_EXTRACTION_THRESHOLD = "HYPERNYM_RELATION_EXTRACTION_THRESHOLD";

	// Minimum probability for a detected hypernym relation to be consider for
	// being extracted in the relation extractor phase in the ontology learning
	// process

	public static final String OBTAIN_RELATIONS = "OBTAIN_RELATIONS";

	public static final String EXTRACT_RELATIONS = "EXTRACT_RELATIONS";

	public static final String CONSIDER_KNOWLEDGE_BASE= "CONSIDER_KNOWLDEDGE_BASE";

	public static final String UIA_PATH = "UIA_PATH";


	public static final String THRIFT_PORT = "THRIFT_PORT";

	public static final String REST_PORT = "REST_PORT";

	public static final String  STORE_TERMS = "STORE_TERMS";

	public static final String STORE_RELATIONS = "STORE_RELATIONS";

	public static final String EXTRACT_RELATIONS_PARALLEL = "EXTRACT_RELATIONS_PARALLEL";
}
