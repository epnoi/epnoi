package org.epnoi.uia.learner.relations.knowledgebase.wikidata;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.epnoi.model.RelationHelper;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.uia.learner.relations.knowledgebase.wikidata.WikidataHandlerParameters.DumpProcessingMode;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocumentProcessor;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemIdValue;
import org.wikidata.wdtk.datamodel.interfaces.MonolingualTextValue;
import org.wikidata.wdtk.datamodel.interfaces.PropertyDocument;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
import org.wikidata.wdtk.datamodel.interfaces.StatementDocument;
import org.wikidata.wdtk.datamodel.interfaces.StatementGroup;
import org.wikidata.wdtk.datamodel.interfaces.ValueSnak;
import org.wikidata.wdtk.dumpfiles.DumpContentType;
import org.wikidata.wdtk.dumpfiles.DumpProcessingController;
import org.wikidata.wdtk.dumpfiles.EntityTimerProcessor;
import org.wikidata.wdtk.dumpfiles.EntityTimerProcessor.TimeoutException;

public class WikiDataHandlerBuilder {

	private WikidataHandlerParameters parameters;
	private boolean offlineMode;
	private String dumpPath;
	private DumpProcessingMode dumpProcessingMode;
	private int timeout;
	private DumpProcessingController dumpProcessingController;
	private Map<String, Set<String>> labelsDictionary = new HashMap<>();

	private Map<String, Set<String>> labelsReverseDictionary = new HashMap<>();
	private Map<String, Set<String>> hypernymRelations = new HashMap<>();

	// --------------------------------------------------------------------------------------------------

	public void init(WikidataHandlerParameters parameters)
			throws EpnoiInitializationException {
		this.parameters = parameters;

		this.dumpPath = (String) this.parameters
				.getParameterValue(WikidataHandlerParameters.DUMP_PATH_PARAMETER);

		this.offlineMode = (boolean) this.parameters
				.getParameterValue(WikidataHandlerParameters.OFFLINE_MODE_PARAMETER);

		this.dumpProcessingMode = (DumpProcessingMode) this.parameters
				.getParameterValue(WikidataHandlerParameters.DUMP_FILE_MODE_PARAMETER);
		this.timeout = (int) this.parameters
				.getParameterValue(WikidataHandlerParameters.TIMEOUT_PARAMETER);

		// Controller object for processing dumps:
		this.dumpProcessingController = new DumpProcessingController(
				"wikidatawiki");
		this.dumpProcessingController.setOfflineMode(this.offlineMode);
		try {
			this.dumpProcessingController.setDownloadDirectory(this.dumpPath);
		} catch (IOException ioException) {

			ioException.printStackTrace();
		}

		// Should we process historic revisions or only current ones?
		boolean onlyCurrentRevisions;
		switch (this.dumpProcessingMode) {
		case ALL_REVS:
		case ALL_REVS_WITH_DAILIES:
			onlyCurrentRevisions = false;
			break;
		case CURRENT_REVS:
		case CURRENT_REVS_WITH_DAILIES:
		case JSON:
		case JUST_ONE_DAILY_FOR_TEST:
		default:
			onlyCurrentRevisions = true;
		}

		HypernymRelationsEntityProcessor entityStatisticsProcessor = new HypernymRelationsEntityProcessor();

		// Subscribe to the most recent entity documents of type wikibase item:
		dumpProcessingController.registerEntityDocumentProcessor(
				entityStatisticsProcessor, null, onlyCurrentRevisions);

		// Also add a timer that reports some basic progress information:
		EntityTimerProcessor entityTimerProcessor = new EntityTimerProcessor(
				this.timeout);
		dumpProcessingController.registerEntityDocumentProcessor(
				entityTimerProcessor, null, onlyCurrentRevisions);

	}

	// --------------------------------------------------------------------------------------------------

	public WikidataHandler build() {

		processEntitiesFromWikidataDump();

		Map<String, Map<String, Set<String>>> relationsTable = new HashMap<>();
		relationsTable.put(RelationHelper.HYPERNYM, hypernymRelations);

		return new WikidataHandlerImpl(labelsDictionary,
				labelsReverseDictionary, relationsTable);
	}

	// --------------------------------------------------------------------------------------------------

	private void processEntitiesFromWikidataDump() {
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
			// The timer caused a time out. Continue and finish normally.
		}

		// Print final timer results:
		entityTimerProcessor.close();
	}

	// --------------------------------------------------------------------------------------------------

	public static void main(String[] args) throws IOException {
		System.out.println("Starting the EntityStatisticsProcessor");

		WikidataHandlerParameters parameters = new WikidataHandlerParameters();

		parameters.setParameter(
				WikidataHandlerParameters.OFFLINE_MODE_PARAMETER, true);
		parameters.setParameter(
				WikidataHandlerParameters.DUMP_FILE_MODE_PARAMETER,
				DumpProcessingMode.JSON);
		parameters
				.setParameter(WikidataHandlerParameters.TIMEOUT_PARAMETER, 10);
		parameters.setParameter(WikidataHandlerParameters.DUMP_PATH_PARAMETER,
				"/Users/rafita/Documents/workspace/wikidataParsingTest");

		WikiDataHandlerBuilder wikidataBuilder = new WikiDataHandlerBuilder();
		try {
			wikidataBuilder.init(parameters);
		} catch (EpnoiInitializationException e) {

			e.printStackTrace();
		}
		wikidataBuilder.processEntitiesFromWikidataDump();
		System.out.println("Ending the EntityStatisticsProcessor");
	}

	// --------------------------------------------------------------------------------------------------

	private class WikidataHandlerImpl implements WikidataHandler {

		private Map<String, Set<String>> labelsDictionary;

		private Map<String, Set<String>> labelsReverseDictionary;
		private Map<String, Map<String, Set<String>>> relations;

		// --------------------------------------------------------------------------------------------------

		private WikidataHandlerImpl(Map<String, Set<String>> labelsDictionary,
				Map<String, Set<String>> labelsReverseDictionary,
				Map<String, Map<String, Set<String>>> relations) {
			super();
			this.labelsDictionary = labelsDictionary;
			this.labelsReverseDictionary = labelsReverseDictionary;
			this.relations = relations;
		}

		@Override
		public Set<String> getRelated(String source, String type) {

			Map<String, Set<String>> consideredRelations = relations.get(type);

			// Firstly we retrieve the IRIs
			Set<String> sourceIRIs = this.labelsDictionary.get(source);

			// For each of them we must retrieve

			labelsDictionary.get(source);

			return this.relations.get(type).get(source);
		}
	}

	// -------------------------------------------------------------------------------------------------------------------------------------

	class HypernymRelationsEntityProcessor implements EntityDocumentProcessor {
		private static final String EN = "en";
		public static final String INSTANCE_OF = "http://www.wikidata.org/entity/P31";

		// --------------------------------------------------------------------------------------------------

		@Override
		public void processItemDocument(ItemDocument itemDocument) {

			processItem(itemDocument);

			processStatements(itemDocument);

		}

		// --------------------------------------------------------------------------------------------------
		/**
		 * 
		 * @param itemDocument
		 */

		private void processItem(ItemDocument itemDocument) {
			String itemIRI = itemDocument.getEntityId().getIri();

			// First we add the label->IRI relation
			if (itemDocument.getLabels().get(
					HypernymRelationsEntityProcessor.EN) != null) {
				String label = itemDocument.getLabels()
						.get(HypernymRelationsEntityProcessor.EN).getText();

				_addToDictionary(itemIRI, label, labelsDictionary);
				_addToDictionary(label, itemIRI, labelsReverseDictionary);
			}
			// Now, for each alias of the label we also add the relation
			// alias->IRI
			if (itemDocument.getAliases().get(
					HypernymRelationsEntityProcessor.EN) != null) {
				for (MonolingualTextValue alias : itemDocument.getAliases()
						.get(HypernymRelationsEntityProcessor.EN)) {
					_addToDictionary(itemIRI, alias.getText(), labelsDictionary);
					_addToDictionary(alias.getText(), itemIRI,
							labelsReverseDictionary);
				}
			}
			// aliasesResolutionTable.put(itemDocument.getAliases(EN));

		}

		// ---------------------------------------------------------------------

		private void _addToDictionary(String value, String key,
				Map<String, Set<String>> dictionary) {
			Set<String> values = dictionary.get(key);
			if (values == null) {
				values = new HashSet<>();
				labelsDictionary.put(key, values);
			}
			values.add(value);
		}

		// --------------------------------------------------------------------------------------------------

		@Override
		public void processPropertyDocument(PropertyDocument propertyDocument) {

		}

		// --------------------------------------------------------------------------------------------------

		protected void processStatements(StatementDocument statementDocument) {

			for (StatementGroup statementGroup : statementDocument
					.getStatementGroups()) {

				String property = statementGroup.getProperty().getIri();

				if (INSTANCE_OF.equals(property)) {
					String subject = statementGroup.getSubject().getIri();

					Set<String> hyponyms = new HashSet<String>();

					for (Statement statement : statementGroup.getStatements()) {

						// if (statement.getClaim().getMainSnak() instanceof
						// ValueSnak) {

						String object = ((ItemIdValue) ((ValueSnak) statement
								.getClaim().getMainSnak()).getValue()).getIri();

						hyponyms.add(object);

						/*
						 * System.out.println("Adding :> (" + subject + ", " +
						 * object + ")");
						 */
						// }
					}
					hypernymRelations.put(subject, hyponyms);
				}

			}
		}

		// --------------------------------------------------------------------------------------------------

	}
}
