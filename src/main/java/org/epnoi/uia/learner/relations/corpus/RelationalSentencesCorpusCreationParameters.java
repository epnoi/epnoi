package org.epnoi.uia.learner.relations.corpus;

import org.epnoi.uia.commons.Parameters;

public class RelationalSentencesCorpusCreationParameters extends
		Parameters<Object> {

	public static final String KNOWLEDGE_BASE_PARAMETERS_PARAMETER = "KNOWLEDGE_BASE_PARAMETERS_PARAMETER";
	public static final String RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER = "RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER";
	public static final String IS_TEST_PARAMETER = "IS_TEST_PARAMETER";
	public static final String STORE_RESULT_PARAMETER = "IS_STORED_PARAMETER";
	public static final String RELATIONAL_SENTENCES_CORPUS_TYPE_PARAMETER = "RELATIONAL_SENTENCES_CORPUS_TYPE_PARAMETER";
	public static final String RELATIONAL_SENTENCES_CORPUS_DESCRIPTION_PARAMETER = "RELATIONAL_SENTENCES_CORPUS_DESCRIPTION_PARAMETER";
	public static final String VERBOSE_PARAMETER = "VERBOSE_PARAMETER";
	public static final String MAX_SENTENCE_LENGTH_PARAMETER = "MAX_SENTENCE_LENGTH_PARAMETER";

	//---------------------------------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "RelationalSentencesCorpusCreationParameters [parmaters:"
				+ super.parameters.entrySet() + "]";
	}

}
