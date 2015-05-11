package org.epnoi.uia.learner.relations.knowledgebase.wikidata;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.epnoi.model.Context;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.learner.relations.knowledgebase.wikidata.WikidataHandlerParameters.DumpProcessingMode;
import org.tartarus.snowball.ext.EnglishStemmer;
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

/**
 * A factory that creates WikidataHandlers
 * 
 * @author Rafael Gonzalez-Cabero {@link http://www.github.com/fitash}
 * 
 *
 */
public class WikidataHandlerBuilder {
	private static final Logger logger = Logger
			.getLogger(WikidataHandlerBuilder.class.getName());
	private Core core;
	private WikidataHandlerParameters parameters;
	private boolean offlineMode;
	private String dumpPath;
	private DumpProcessingMode dumpProcessingMode;
	private int timeout;
	private boolean store;
	private String wikidataViewURI;
	private DumpProcessingController dumpProcessingController;
	private Map<String, Set<String>> labelsDictionary = new HashMap<>();

	private Map<String, Set<String>> labelsReverseDictionary = new HashMap<>();

	private Map<String, Set<String>> hypernymRelations = new HashMap<>();
	Map<String, Map<String, Set<String>>> relationsTable = new HashMap<>();

	// --------------------------------------------------------------------------------------------------

	public void init(Core core, WikidataHandlerParameters parameters)
			throws EpnoiInitializationException {
		logger.info("Initialiazing the WikidataHandler with the following parameters: "
				+ parameters);
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

		this.store = (boolean) this.parameters
				.getParameterValue(WikidataHandlerParameters.STORE_WIKIDATA_VIEW_PARAMETER);
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

	/**
	 * Builds a WikidataHandler. If the parameter
	 * WikidataHandlerParameters.STORE_PARAMETER (see
	 * {@link WikidataHandlerParameters}) is set to true, the associated
	 * WikidataView is stored in the UIA for later use
	 * 
	 * @return A WikidataHandler with its associated WikidataView
	 */
	public WikidataHandler build() {
		logger.info("Building a WikidataHandler with the following parameters: "
				+ parameters);
		WikidataView wikidataView = null;
		processEntitiesFromWikidataDump();

		relationsTable.put(RelationHelper.HYPERNYM, hypernymRelations);

		wikidataView = new WikidataView(wikidataViewURI, labelsDictionary,
				labelsReverseDictionary, relationsTable);

		if (this.store) {
			logger.info("Storing the new built WikidataView, since the store flag was activated");
			// First we remove the WikidataWiew if there is one with the same
			// URI
			this.core.getInformationHandler().remove(this.wikidataViewURI,
					RDFHelper.WIKIDATA_VIEW_CLASS);

			this.core.getInformationHandler().put(wikidataView,
					Context.getEmptyContext());
		}
		return new WikidataHandlerImpl(wikidataView);
	}

	// --------------------------------------------------------------------------------------------------

	public WikidataHandler retrieve() {
		logger.info("Retrieving a WikidataHandler from a WikidataView stored in the UIA");
		WikidataView wikidataView = null;
		/*
		 * wikidataView = (WikidataView) this.core.getInformationHandler().get(
		 * this.wikidataViewURI, RDFHelper.WIKIDATA_VIEW_CLASS);
		 */
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

		}
		System.out.println("Aqui deberia ir el processing 2");
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
				WikidataHandlerParameters.STORE_WIKIDATA_VIEW_PARAMETER, true);
		parameters.setParameter(
				WikidataHandlerParameters.OFFLINE_MODE_PARAMETER, true);
		parameters.setParameter(
				WikidataHandlerParameters.DUMP_FILE_MODE_PARAMETER,
				DumpProcessingMode.JSON);
		parameters.setParameter(WikidataHandlerParameters.TIMEOUT_PARAMETER,
				100);
		parameters.setParameter(WikidataHandlerParameters.DUMP_PATH_PARAMETER,
				"/Users/rafita/Documents/workspace/wikidataParsingTest");

		WikidataHandlerBuilder wikidataBuilder = new WikidataHandlerBuilder();
		try {
			wikidataBuilder.init(core, parameters);
		} catch (EpnoiInitializationException e) {

			e.printStackTrace();
		}

		Long startTime = System.currentTimeMillis();

		System.out.print("");
		WikidataHandler wikidataHandler = wikidataBuilder.retrieve();
		System.out.println("------> " + wikidataHandler.stem("thieves"));

		System.exit(0);
		System.out.println("(size)---------------> "
				+ wikidataHandler.getView());

		Long endTime = System.currentTimeMillis();
		System.out.println("It took " + ((endTime - startTime) / 1000)
				+ " to create and store the wikidata curated table");

		startTime = System.currentTimeMillis();

		WikidataHandler handler = wikidataBuilder.retrieve();
		System.out.println("---------------> " + handler.getView());

		endTime = System.currentTimeMillis();

		System.out.println("It took " + ((endTime - startTime) / 1000)
				+ " to load the wikidata curated table");
		/*
		 * System.out.println("dog -----> " + wikidataHandler.getRelated("dog",
		 * RelationHelper.HYPERNYM));
		 */

		for (String label : wikidataHandler.getView().getLabelsDictionary()
				.keySet()) {
			if (!handler.getView().getLabelsDictionary().keySet()
					.contains(label)) {
				System.out.println("....> este no esta " + label);
			}
		}
		System.out.println("Ending the WikiDataHandlerBuilder");
	}

	// --------------------------------------------------------------------------------------------------

	private class WikidataHandlerImpl implements WikidataHandler {
		private WikidataStemmer stemmer = new WikidataStemmer();
		private WikidataView wikidataView;

		// --------------------------------------------------------------------------------------------------

		private WikidataHandlerImpl(WikidataView wikidataView) {
			this.wikidataView = wikidataView;
		}

		@Override
		public WikidataView getView() {
			return this.wikidataView;
		}

		// --------------------------------------------------------------------------------------------------

		@Override
		public Set<String> getRelated(String sourceLabel, String type) {

			Set<String> targetLabels = new HashSet<String>();

			Map<String, Set<String>> consideredRelations = this.wikidataView
					.getRelations().get(type);

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

		// --------------------------------------------------------------------------------------------------
		@Override
		public String stem(String term) {
			return this.stemmer.stem(term);
		}

		// --------------------------------------------------------------------------------------------------

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
			String itemIRI = itemDocument.getEntityId().getId();

			// First we add the label->IRI relation
			if (itemDocument.getLabels().get(
					HypernymRelationsEntityProcessor.EN) != null) {
				String label = itemDocument.getLabels()
						.get(HypernymRelationsEntityProcessor.EN).getText();

				_addToDictionary(label, itemIRI, labelsDictionary);
				_addToDictionary(itemIRI, label, labelsReverseDictionary);
			}
			// Now, for each alias of the label we also add the relation
			// alias->IRI
			if (itemDocument.getAliases().get(
					HypernymRelationsEntityProcessor.EN) != null) {
				for (MonolingualTextValue alias : itemDocument.getAliases()
						.get(HypernymRelationsEntityProcessor.EN)) {
					_addToDictionary(alias.getText(), itemIRI, labelsDictionary);
					_addToDictionary(itemIRI, alias.getText(),
							labelsReverseDictionary);
				}
			}

		}

		// ---------------------------------------------------------------------

		/**
		 * 
		 * @param value
		 * @param key
		 * @param dictionary
		 */
		private void _addToDictionary(String key, String value,
				Map<String, Set<String>> dictionary) {
			Set<String> values = dictionary.get(key);
			if (values == null) {
				values = new HashSet<>();
				dictionary.put(key, values);
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
					String subject = statementGroup.getSubject().getId();

					Set<String> hyponyms = new HashSet<String>();

					for (Statement statement : statementGroup.getStatements()) {

						String object = ((ItemIdValue) ((ValueSnak) statement
								.getClaim().getMainSnak()).getValue()).getId();

						hyponyms.add(object);

					}
					hypernymRelations.put(subject, hyponyms);
				}

			}
		}
		// --------------------------------------------------------------------------------------------------

	}
}
