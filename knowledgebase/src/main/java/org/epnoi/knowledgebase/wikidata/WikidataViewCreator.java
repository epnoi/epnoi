package org.epnoi.knowledgebase.wikidata;

import org.epnoi.knowledgebase.wikidata.WikidataHandlerParameters.DumpProcessingMode;
import org.epnoi.model.Context;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.WikidataView;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.wikidata.wdtk.datamodel.interfaces.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

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
	private WikidataDumpProcessor wikidataDumpProcessor;
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
		this.wikidataDumpProcessor = new WikidataDumpProcessor();
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

		relationsTable.put(RelationHelper.HYPERNYMY, hypernymRelations);
		this.wikidataDumpProcessor.processEntitiesFromWikidataDump();
		wikidataView = new WikidataView(wikidataViewURI, labelsDictionary,
				labelsReverseDictionary, relationsTable);
		
		WikidataViewCompressor wikidataViewCompressor = new WikidataViewCompressor();
		
		wikidataView= wikidataViewCompressor.compress(wikidataView);

		return wikidataView;
	}

	// --------------------------------------------------------------------------------------------------

	public void store(WikidataView wikidataView) {
		this.core.getInformationHandler().remove(wikidataView.getUri(),
				RDFHelper.WIKIDATA_VIEW_CLASS);

		this.core.getInformationHandler().put(wikidataView,
				Context.getEmptyContext());
	}

	// ---------------------------------------------------------------------------------------------------

	public WikidataView retrieve(String uri) {
		return (WikidataView) this.core.getInformationHandler().get(uri,
				RDFHelper.WIKIDATA_VIEW_CLASS);
	}

	// --------------------------------------------------------------------------------------------------
/*
 FOR_TEST
 
	public static void main(String[] args) throws IOException {
		System.out.println("Starting the WikidataViewCreator");

		for (String arg : args) {
			System.out.println("----------------> " + arg);
		}

		Core core = CoreUtility.getUIACore();

		WikidataHandlerParameters parameters = new WikidataHandlerParameters();

		parameters.setParameter(WikidataHandlerParameters.WIKIDATA_VIEW_URI,
				WikidataHandlerParameters.DEFAULT_URI);
		parameters.setParameter(WikidataHandlerParameters.OFFLINE_MODE, true);
		parameters.setParameter(WikidataHandlerParameters.DUMP_FILE_MODE,
				DumpProcessingMode.JSON);
		parameters.setParameter(WikidataHandlerParameters.TIMEOUT, 100);
		parameters.setParameter(WikidataHandlerParameters.DUMP_PATH,
				"/opt/epnoi/epnoideployment/wikidata");

		WikidataViewCreator wikidataViewCreator = new WikidataViewCreator();
		try {
			wikidataViewCreator.init(core, parameters);
		} catch (EpnoiInitializationException e) {

			e.printStackTrace();
		}

		WikidataView wikidataView = wikidataViewCreator.create();

		System.out.println("Ending the WikidataViewCreator");
	}
*/
	// -------------------------------------------------------------------------------------------------------------------------------------

	class HypernymRelationsEntityProcessor implements EntityDocumentProcessor {
		private static final String EN = "en";
		public static final String INSTANCE_OF = "http://www.wikidata.org/entity/P31";

		// --------------------------------------------------------------------------------------------------

		@Override
		public void processItemDocument(ItemDocument itemDocument) {

			if (_valuableItem(itemDocument)) {

				processStatements(itemDocument);

				processItem(itemDocument);

			}
		}

		private boolean _valuableItem(ItemDocument itemDocument) {

			return (itemDocument.getLabels().get(
					HypernymRelationsEntityProcessor.EN) != null);

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
				// Though we don't stemm the label, we at least use only
				// lowercase letters
				label = label.toLowerCase();
				if (_validLabel(label)) {
					//_addToDictionary(label, itemIRI, labelsDictionary);
					_addToDictionary(itemIRI, label, labelsReverseDictionary);
				}
			}
			// Now, for each alias of the label we also add the relation
			// alias->IRI
			/*
			if (itemDocument.getAliases().get(
					HypernymRelationsEntityProcessor.EN) != null) {
				for (MonolingualTextValue alias : itemDocument.getAliases()
						.get(HypernymRelationsEntityProcessor.EN)) {
					String aliasText = alias.getText();
					aliasText = aliasText.toLowerCase();
					if (_validLabel(aliasText)) {
					//	_addToDictionary(aliasText, itemIRI, labelsDictionary);
						_addToDictionary(itemIRI, aliasText,
						labelsReverseDictionary);
					}
				}
			}
			*/

		}

		// ---------------------------------------------------------------------

		private boolean _validLabel(String label) {
			return ((label != null) && (!label.contains("disambiguation")));

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
			// We consider that isValid if contains instanceOf properties
			
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
