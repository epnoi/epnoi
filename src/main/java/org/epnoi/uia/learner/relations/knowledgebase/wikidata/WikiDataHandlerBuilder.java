package org.epnoi.uia.learner.relations.knowledgebase.wikidata;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.epnoi.model.RelationHelper;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
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

	private Core core;
	private WikidataHandlerParameters parameters;
	private boolean offlineMode;
	private String dumpPath;
	private DumpProcessingMode dumpProcessingMode;
	private int timeout;
	private boolean retrieve;
	private String wikidataViewURI;
	private DumpProcessingController dumpProcessingController;
	private Map<String, Set<String>> labelsDictionary = new HashMap<>();

	private Map<String, Set<String>> labelsReverseDictionary = new HashMap<>();
	private Map<String, Set<String>> hypernymRelations = new HashMap<>();

	// --------------------------------------------------------------------------------------------------

	public void init(Core core, WikidataHandlerParameters parameters)
			throws EpnoiInitializationException {
		this.core = core;
		this.parameters = parameters;

		this.dumpPath = (String) this.parameters
				.getParameterValue(WikidataHandlerParameters.DUMP_PATH_PARAMETER);

		this.offlineMode = (boolean) this.parameters
				.getParameterValue(WikidataHandlerParameters.OFFLINE_MODE_PARAMETER);

		this.dumpProcessingMode = (DumpProcessingMode) this.parameters
				.getParameterValue(WikidataHandlerParameters.DUMP_FILE_MODE_PARAMETER);
		this.timeout = (int) this.parameters
				.getParameterValue(WikidataHandlerParameters.TIMEOUT_PARAMETER);

		this.retrieve = (boolean) this.parameters
				.getParameterValue(WikidataHandlerParameters.RETRIEVE_WIKIDATA_VIEW_PARAMETER);
		this.wikidataViewURI = (String) this.parameters
				.getParameterValue(WikidataHandlerParameters.WIKIDATA_VIEW_URI_PARAMETER);

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
		WikidataView wikidataView = null;
		if (this.retrieve) {
			wikidataView = (WikidataView) this.core.getInformationHandler()
					.get(this.wikidataViewURI, RDFHelper.WIKIDATA_VIEW_CLASS);
		} else {
			processEntitiesFromWikidataDump();

			Map<String, Map<String, Set<String>>> relationsTable = new HashMap<>();
			relationsTable.put(RelationHelper.HYPERNYM, hypernymRelations);

			wikidataView = new WikidataView(wikidataViewURI, labelsDictionary,
					labelsReverseDictionary, relationsTable);
		}

		return new WikidataHandlerImpl(wikidataView);

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
				// MwDumpFile dumpFile
				// =dumpProcessingController.getMostRecentDump(DumpContentType.JSON);

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
		System.out.println("Starting the WikiDataHandlerBuilder");

		Core core = CoreUtility.getUIACore();

		WikidataHandlerParameters parameters = new WikidataHandlerParameters();

		parameters.setParameter(
				WikidataHandlerParameters.WIKIDATA_VIEW_URI_PARAMETER,
				"http://wikidataView");
		parameters.setParameter(
				WikidataHandlerParameters.RETRIEVE_WIKIDATA_VIEW_PARAMETER,
				false);
		parameters.setParameter(
				WikidataHandlerParameters.OFFLINE_MODE_PARAMETER, true);
		parameters.setParameter(
				WikidataHandlerParameters.DUMP_FILE_MODE_PARAMETER,
				DumpProcessingMode.JSON);
		parameters.setParameter(WikidataHandlerParameters.TIMEOUT_PARAMETER,
				100);
		parameters.setParameter(WikidataHandlerParameters.DUMP_PATH_PARAMETER,
				"/Users/rafita/Documents/workspace/wikidataParsingTest");

		WikiDataHandlerBuilder wikidataBuilder = new WikiDataHandlerBuilder();
		try {
			wikidataBuilder.init(core, parameters);
		} catch (EpnoiInitializationException e) {

			e.printStackTrace();
		}
		WikidataHandler wikidataHandler = wikidataBuilder.build();
		System.out.println("dog -----> "
				+ wikidataHandler.getRelated("dog", RelationHelper.HYPERNYM));

		System.out.println("Ending the WikiDataHandlerBuilder");
	}

	// --------------------------------------------------------------------------------------------------

	private class WikidataHandlerImpl implements WikidataHandler {

		private WikidataView wikidataView;

		// --------------------------------------------------------------------------------------------------

		private WikidataHandlerImpl(WikidataView wikidataView) {
			this.wikidataView = wikidataView;
		}

		// --------------------------------------------------------------------------------------------------

		@Override
		public Set<String> getRelated(String sourceLabel, String type) {

			Set<String> targetLabels = new HashSet<String>();

			Map<String, Set<String>> consideredRelations = this.wikidataView
					.getRelations().get(type);

			System.out.println("considered relations # "
					+ consideredRelations.size());

			// Firstly we retrieve the IRIs
			Set<String> sourceIRIs = this.wikidataView.getLabelsDictionary()
					.get(sourceLabel);
			// System.out.println("sourceIRIs >>>> " + sourceIRIs);
			// For each of them we must retrieve

			for (String sourceIRI : sourceIRIs) {
				// System.out.println("sourceIRI " + sourceIRI);
				for (String targetIRI : consideredRelations.get(sourceIRI)) {
					if (targetIRI != null) {
						for (String destinationTarget : this.wikidataView
								.getLabelsReverseDictionary().get(targetIRI)) {
							targetLabels.add(destinationTarget);
						}
					}
				}
			}

			return targetLabels;
		}

		@Override
		public String toString() {
			return "WikidataHandlerImpl [wikidataView=" + wikidataView + "]";
		}

		// --------------------------------------------------------------------------------------------------

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
