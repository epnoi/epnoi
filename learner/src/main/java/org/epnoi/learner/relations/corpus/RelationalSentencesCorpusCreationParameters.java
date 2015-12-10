package org.epnoi.learner.relations.corpus;

import org.epnoi.model.commons.Parameters;

import java.io.Serializable;

public class RelationalSentencesCorpusCreationParameters extends Parameters<Serializable> implements Serializable {

	public static final String KNOWLEDGE_BASE_PARAMETERS = "KNOWLEDGE_BASE_PARAMETERS";
	public static final String RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER = "RELATIONAL_SENTENCES_CORPUS_URI";
	public static final String IS_TEST_PARAMETER = "IS_TEST_PARAMETER";
	public static final String STORE = "IS_STORED_PARAMETER";
	public static final String RELATIONAL_SENTENCES_CORPUS_TYPE_PARAMETER = "RELATIONAL_SENTENCES_CORPUS_TYPE";
	public static final String RELATIONAL_SENTENCES_CORPUS_DESCRIPTION_PARAMETER = "RELATIONAL_SENTENCES_CORPUS_DESCRIPTION";
	public static final String VERBOSE = "VERBOSE_PARAMETER";
	public static final String MAX_SENTENCE_LENGTH_PARAMETER = "MAX_SENTENCE_LENGTH";
	public static final String TEST = "TEST";
	public static final String UIA_PATH = "UIA_PATH";
	public static final String SPARK_PATH = "SPARK_PATH";

	public static final String THRIFT_PORT = "THRIFT_PORT";

	public static final String REST_PORT = "REST_PORT";

	public static final String MAX_TEXT_CORPUS_SIZE = "MAX_TEXT_CORPUS_SIZE";


}
