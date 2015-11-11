package org.epnoi.learner;

import org.epnoi.model.commons.Parameters;
import org.springframework.stereotype.Component;

import java.io.Serializable;

public class OntologyLearningWorkflowParameters extends Parameters<Serializable> implements Serializable {

	// Domain Definition
	// Parameters--------------------------------------------------------------------------------

	public static final String CONSIDERED_DOMAINS = "CONSIDERED_DOMAINS"; // List<Domain>
	// The domains considered in the ontology learning process. It includes the
	// target domain plus the ones used as a reference.

	public static final String TARGET_DOMAIN = "TARGET_DOMAIN";//String (URI of the target domain that must be among the CONSIDERED_DOMAINS
	// Domain that is the target of the ontology learning process, the learned
	// ontology represents this domain.

	// Term Extraction phase
	// ----------------------------------------------------------------------------------------

	// Expansion phase
	// parameters-----------------------------------------------------------------------------------

	public static final String MAX_RELATIONAL_SENTENCE_LENGTH = "MAX_RELATIONAL_SENTENCE_LENGTH";

	public static final String NUMBER_INITIAL_TERMS = "NUMBER_INITIAL_TERMS";
	// number of initial terms in the ontology

	public static final String HYPERNYM_RELATION_EXPANSION_THRESHOLD = "HYPERNYM_RELATION_EXPANSION_THRESHOLD";
	// Minimum probability for a detected hypernym relation to be consider for
	// being expanded in the ontology learning process

	public static final String EXTRACT_TERMS = "EXTRACT_TERMS";

	public static final String HYPERNYM_MODEL_PATH = "HYPERNYM_MODEL_PATH";

	public static final String HYPERNYM_MODEL = "HYPERNYM_MODEL";

	public static final String HYPERNYM_RELATION_EXTRACTION_THRESHOLD = "HYPERNYM_RELATION_EXTRACTION_THRESHOLD";

	// Minimum probability for a detected hypernym relation to be consider for
	// being extracted in the relation extractor phase in the ontology learning
	// process

	public static final String RELATIONS_HANDLER_PARAMETERS = "RELATIONS_HANDLER_PARAMETER";
	
	public static final String CONSIDER_KNOWLEDGE_BASE= "CONSIDER_KNOWLDEDGE_BASE";

	public static final String UIA_PATH = "UIA_PATH";

}
