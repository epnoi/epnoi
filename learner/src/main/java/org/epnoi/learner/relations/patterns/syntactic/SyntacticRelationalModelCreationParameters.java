package org.epnoi.learner.relations.patterns.syntactic;

import org.epnoi.model.commons.Parameters;

public class SyntacticRelationalModelCreationParameters extends Parameters<Object> {
	public static final String RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER = "RELATIONAL_SENTENCES_CORPUS_URI";
	public static final String MAX_PATTERN_LENGTH_PARAMETER = "MAX_PATTERN_LENGTH";
	public static final String MODEL_PATH_PARAMETERS = "MODEL_PATH_PARAMETER";
	public static final String VERBOSE_PARAMETERS = "VERBOSE_PARAMETER";
	public static final String STORE_PARAMETERS = "STORE_PARAMETER";

	
	
	// ------------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "SyntacticRelationalModelCreationParameters ["
				+ parameters.toString() + "]";
	}

}
