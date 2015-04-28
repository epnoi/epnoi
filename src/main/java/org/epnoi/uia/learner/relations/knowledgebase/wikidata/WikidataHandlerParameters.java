package org.epnoi.uia.learner.relations.knowledgebase.wikidata;

import org.epnoi.uia.commons.Parameters;

public class WikidataHandlerParameters extends Parameters<Object> {
	public static String DUMP_PATH_PARAMETER = "DUMP_PATH_PARAMETER";

	/**
	 * If set to true, all example programs will run in offline mode. Only data
	 * dumps that have been downloaded in previous runs will be used.
	 */
	// public static final boolean OFFLINE_MODE = true;

	public static final String OFFLINE_MODE_PARAMETER = "OFFLINE_MODE_PARAMETER";

	public static enum DumpProcessingMode {
		JSON, CURRENT_REVS, ALL_REVS, CURRENT_REVS_WITH_DAILIES, ALL_REVS_WITH_DAILIES, JUST_ONE_DAILY_FOR_TEST
	}

	/**
	 * Defines which dumps will be downloaded and processed in all examples.
	 */

	public static final String DUMP_FILE_MODE_PARAMETER = "DUMP_FILE_MODE_PARAMETER";
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
	public static final String TIMEOUT_PARAMETER = "TIMEOUT_PARAMETER";

	// public static final int TIMEOUT_SEC = 10;

}
