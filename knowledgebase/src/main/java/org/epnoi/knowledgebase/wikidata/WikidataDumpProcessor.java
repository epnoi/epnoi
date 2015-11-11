package org.epnoi.knowledgebase.wikidata;

import org.epnoi.knowledgebase.wikidata.WikidataHandlerParameters.DumpProcessingMode;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocumentProcessor;
import org.wikidata.wdtk.dumpfiles.DumpContentType;
import org.wikidata.wdtk.dumpfiles.DumpProcessingController;
import org.wikidata.wdtk.dumpfiles.EntityTimerProcessor;
import org.wikidata.wdtk.dumpfiles.EntityTimerProcessor.TimeoutException;

import java.io.IOException;

public class WikidataDumpProcessor {
	private DumpProcessingController dumpProcessingController;
	private WikidataHandlerParameters parameters;
	private boolean offlineMode;
	private String dumpPath;
	private DumpProcessingMode dumpProcessingMode;
	private int timeout;
	private boolean onlyCurrentRevisions;

	// --------------------------------------------------------------------------------------------------

	public void init(WikidataHandlerParameters parameters) {
		this.parameters = parameters;
		this.dumpPath = (String) this.parameters
				.getParameterValue(WikidataHandlerParameters.DUMP_PATH);

		this.offlineMode = (boolean) this.parameters
				.getParameterValue(WikidataHandlerParameters.OFFLINE_MODE);

		this.dumpProcessingMode = (DumpProcessingMode) this.parameters
				.getParameterValue(WikidataHandlerParameters.DUMP_FILE_MODE);
		this.timeout = (int) this.parameters
				.getParameterValue(WikidataHandlerParameters.TIMEOUT);

		this.dumpProcessingController = new DumpProcessingController("wikidata");
		this.dumpProcessingController.setOfflineMode(this.offlineMode);
		try {
			this.dumpProcessingController.setDownloadDirectory(this.dumpPath);
		} catch (IOException ioException) {

			ioException.printStackTrace();
		}

		// Should we process historic revisions or only current ones?

		switch (this.dumpProcessingMode) {
		case ALL_REVS:
		case ALL_REVS_WITH_DAILIES:
			this.onlyCurrentRevisions = false;
			break;
		case CURRENT_REVS:
		case CURRENT_REVS_WITH_DAILIES:
		case JSON:
		case JUST_ONE_DAILY_FOR_TEST:
		default:
			this.onlyCurrentRevisions = true;
		}
	}

	// --------------------------------------------------------------------------------------------------

	public void registerEntityDocumentProcessor(
			EntityDocumentProcessor entityDocumentProcessor) {

		// Subscribe to the most recent entity documents of type wikibase item:
		dumpProcessingController.registerEntityDocumentProcessor(
				entityDocumentProcessor, null, this.onlyCurrentRevisions);

		// Also add a timer that reports some basic progress information:
		EntityTimerProcessor entityTimerProcessor = new EntityTimerProcessor(
				this.timeout);
		dumpProcessingController.registerEntityDocumentProcessor(
				entityTimerProcessor, null, this.onlyCurrentRevisions);

	}

	// --------------------------------------------------------------------------------------------------

	public void processEntitiesFromWikidataDump() {

		// Also add a timer that reports some basic progress information:
		EntityTimerProcessor entityTimerProcessor = new EntityTimerProcessor(
				this.timeout);
		try {
			// Start processing (may trigger downloads where needed):
			switch (this.dumpProcessingMode) {
			case ALL_REVS:
			case CURRENT_REVS:
				dumpProcessingController.processMostRecentMainDump();
				break;
			case ALL_REVS_WITH_DAILIES:
			case CURRENT_REVS_WITH_DAILIES:
				dumpProcessingController.processAllRecentRevisionDumps();
				break;
			case JSON:
				// MwDumpFile dumpFile
				// =dumpProcessingController.getMostRecentDump(DumpContentType.JSON);
				try {
					dumpProcessingController
							.setDownloadDirectory((String) this.parameters
									.getParameterValue(WikidataHandlerParameters.DUMP_PATH));
				} catch (IOException e) {

					e.printStackTrace();
					System.exit(-1);
				}

				dumpProcessingController.processMostRecentJsonDump();

				break;
			case JUST_ONE_DAILY_FOR_TEST:
				dumpProcessingController.processDump(dumpProcessingController
						.getMostRecentDump(DumpContentType.DAILY));
				break;
			default:
				throw new RuntimeException("Unsupported dump processing type "
						+ this.dumpProcessingMode);
			}
		} catch (TimeoutException e) {

		}
		System.out.println("AQUI IRIA LA COMPACTACION!!!");
		// Print final timer results:
		entityTimerProcessor.close();
	}

	// --------------------------------------------------------------------------------------------------

}