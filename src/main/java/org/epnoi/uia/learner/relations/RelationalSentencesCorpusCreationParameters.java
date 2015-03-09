package org.epnoi.uia.learner.relations;

import org.epnoi.uia.commons.Parameters;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.exceptions.EpnoiInitializationException;

public class RelationalSentencesCorpusCreationParameters extends
		Parameters<Object> {

	public static final String WORDNET_PARAMETERS = "WORDNET_PARAMETERS";
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
