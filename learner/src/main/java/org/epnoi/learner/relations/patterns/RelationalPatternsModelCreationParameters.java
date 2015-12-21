package org.epnoi.learner.relations.patterns;

import org.epnoi.model.commons.Parameters;

public class RelationalPatternsModelCreationParameters extends
		Parameters<Object> {
	public static final String RELATIONAL_SENTENCES_CORPUS_URI = "RELATIONAL_SENTENCES_CORPUS_URI";
	public static final String MAX_PATTERN_LENGTH = "MAX_PATTERN_LENGTH";
	public static final String INTERPOLATION_CONSTANT = "INTERPOLATION_CONSTANT";
	public static final String MODEL_PATH = "MODEL_PATH";
	public static final String VERBOSE = "VERBOSE_PARAMETER";
	public static final String STORE = "STORE_PARAMETER";
	public static final String TYPE = "TYPE_PARAMETER";
	public static final String MODEL = "MODEL";
	public static final String TEST = "TEST_PARAMETER";


	// ------------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "RelationalPatternsModelCreationParameters ["
				+ parameters.toString() + "]";
	}

}
