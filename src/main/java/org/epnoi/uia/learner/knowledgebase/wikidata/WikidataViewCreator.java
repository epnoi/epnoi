package org.epnoi.uia.learner.knowledgebase.wikidata;

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
import org.epnoi.uia.learner.knowledgebase.wikidata.WikidataHandlerParameters.DumpProcessingMode;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocumentProcessor;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemIdValue;
import org.wikidata.wdtk.datamodel.interfaces.MonolingualTextValue;
import org.wikidata.wdtk.datamodel.interfaces.PropertyDocument;
import org.wikidata.wdtk.datamodel.interfaces.Snak;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
import org.wikidata.wdtk.datamodel.interfaces.StatementDocument;
import org.wikidata.wdtk.datamodel.interfaces.StatementGroup;
import org.wikidata.wdtk.datamodel.interfaces.ValueSnak;
import org.wikidata.wdtk.dumpfiles.DumpContentType;
import org.wikidata.wdtk.dumpfiles.DumpProcessingController;
import org.wikidata.wdtk.dumpfiles.EntityTimerProcessor;
import org.wikidata.wdtk.dumpfiles.EntityTimerProcessor.TimeoutException;

public class WikidataViewCreator {
	private static final Logger logger = Logger
			.getLogger(WikidataHandlerBuilder.class.getName());
	private Core core;
	private WikidataHandlerParameters parameters;
	private boolean offlineMode;
	private String dumpPath;
	private DumpProcessingMode dumpProcessingMode;
	private int timeout;
	private String wikidataViewURI;
	private WikidataDumProcessor wikidataDumpProcessor;
	private Map<String, Set<String>> labelsDictionary = new HashMap<>();

	private Map<String, Set<String>> labelsReverseDictionary = new HashMap<>();

	private Map<String, Set<String>> hypernymRelations = new HashMap<>();
	Map<String, Map<String, Set<String>>> relationsTable = new HashMap<>();

	// --------------------------------------------------------------------------------------------------

	public void init(Core core, WikidataHandlerParameters parameters)
			throws EpnoiInitializationException {
		logger.info("Initialiazing the WikidataViewCreator with the following parameters: "
				+ parameters);
		this.core = core;
		this.parameters = parameters;

		this.dumpPath = (String) this.parameters
				.getParameterValue(WikidataHandlerParameters.DUMP_PATH);

		this.offlineMode = (boolean) this.parameters
				.getParameterValue(WikidataHandlerParameters.OFFLINE_MODE);

		this.dumpProcessingMode = (DumpProcessingMode) this.parameters
				.getParameterValue(WikidataHandlerParameters.DUMP_FILE_MODE);
		this.timeout = (int) this.parameters
				.getParameterValue(WikidataHandlerParameters.TIMEOUT);

		this.wikidataViewURI = (String) this.parameters
				.getParameterValue(WikidataHandlerParameters.WIKIDATA_VIEW_URI);
		this.wikidataDumpProcessor = new WikidataDumProcessor();
		this.wikidataDumpProcessor.init(parameters);

		HypernymRelationsEntityProcessor wikidataEntitiesProcessor = new HypernymRelationsEntityProcessor();

		// Subscribe to the most recent entity documents of type wikibase item:
		this.wikidataDumpProcessor
				.registerEntityDocumentProcessor(wikidataEntitiesProcessor);

	}

	// --------------------------------------------------------------------------------------------------

	public WikidataView create() {
		logger.info("Creating a WikidataView with the following parameters: "
				+ parameters);
		WikidataView wikidataView = null;

		relationsTable.put(RelationHelper.HYPERNYM, hypernymRelations);
		this.wikidataDumpProcessor.processEntitiesFromWikidataDump();
		wikidataView = new WikidataView(wikidataViewURI, labelsDictionary,
				labelsReverseDictionary, relationsTable);
		return wikidataView;
	}

	public void store(WikidataView wikidataView) {
		this.core.getInformationHandler().remove(this.wikidataViewURI,
				RDFHelper.WIKIDATA_VIEW_CLASS);

		this.core.getInformationHandler().put(wikidataView,
				Context.getEmptyContext());
	}

	// --------------------------------------------------------------------------------------------------

	public static void main(String[] args) throws IOException {
		System.out.println("Starting the WikidataViewCreator");
		
		for(String arg: args){
			System.out.println("----------------> "+arg);
		}

		Core core = CoreUtility.getUIACore();

		WikidataHandlerParameters parameters = new WikidataHandlerParameters();

		parameters.setParameter(WikidataHandlerParameters.WIKIDATA_VIEW_URI,
				"http://wikidataView");
		parameters.setParameter(WikidataHandlerParameters.OFFLINE_MODE, true);
		parameters.setParameter(WikidataHandlerParameters.DUMP_FILE_MODE,
				DumpProcessingMode.JSON);
		parameters.setParameter(WikidataViewCreatorParameters.TIMEOUT, 100);
		parameters.setParameter(WikidataViewCreatorParameters.DUMP_PATH, "/opt/epnoi/epnoideployment/wikidata");

		WikidataViewCreator wikidataViewCreator = new WikidataViewCreator();
		try {
			wikidataViewCreator.init(core, parameters);
		} catch (EpnoiInitializationException e) {

			e.printStackTrace();
		}

		WikidataView wikidataView = wikidataViewCreator.create();

		System.out.println("Ending the WikidataViewCreator");
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
						Snak mainSnak = statement.getClaim().getMainSnak();
						if (mainSnak instanceof ValueSnak) {
							String object = ((ItemIdValue) ((ValueSnak) mainSnak)
									.getValue()).getId();

							hyponyms.add(object);
						}
					}
					hypernymRelations.put(subject, hyponyms);
				}

			}
		}
		// --------------------------------------------------------------------------------------------------

	}
}
