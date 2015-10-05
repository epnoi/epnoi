package org.epnoi.knowledgebase.wikidata;

import org.epnoi.model.commons.Parameters;

public class WikidataHandlerParameters extends Parameters<Object> {
	public static String WIKIDATA_VIEW_URI = "WIKIDATA_VIEW_URI";
	
	/**
	 * Path where the Wikidata dump can be found
	 */
	public static String DUMP_PATH = "DUMP_PATH";

	/**
	 * If set to true, all example programs will run in offline mode. Only data
	 * dumps that have been downloaded in previous runs will be used.
	 */
	// public static final boolean OFFLINE_MODE = true;

	public static final String OFFLINE_MODE = "OFFLINE_MODE";

	public static enum DumpProcessingMode {
		JSON, CURRENT_REVS, ALL_REVS, CURRENT_REVS_WITH_DAILIES, ALL_REVS_WITH_DAILIES, JUST_ONE_DAILY_FOR_TEST
	}

	/**
	 * Defines which dumps will be downloaded and processed in all examples.
	 */

	public static final String DUMP_FILE_MODE = "DUMP_FILE_MODE";
	// public static final DumpProcessingMode DUMP_FILE_MODE =
	// DumpProcessingMode.JSON;

	/**
	 * Timeout to abort processing after a short while or 0 to disable timeout.
	 * If set, then the processing will cleanly exit after about this many
	 * seconds, as if the dump file would have ended there. This is useful for
	 * testing (and in particular better than just aborting the program) since
	 * it allows for final processing and proper closing to happen without
	 * having to wait for a whole dump file to process.
	 */
	public static final String TIMEOUT = "TIMEOUT";

	public static final String RETRIEVE_WIKIDATA_VIEW = "RETRIEVE_WIKIDATA_VIEW";
	
	public static final String CREATE_WIKIDATA_VIEW = "CREATE_WIKIDATA_VIEW";
	
	public static final String STORE_WIKIDATA_VIEW = "STORE_WIKIDATA_VIEW";
	
	public static final String IN_MEMORY = "IN_MEMORY";


	// public static final int TIMEOUT_SEC = 10;

	
	public static final String DEFAULT_URI= "http://www.epnoi.org/wikidataView";
}
