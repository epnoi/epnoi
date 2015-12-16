package org.epnoi.knowledgebase.wikidata;

import org.epnoi.knowledgebase.wikidata.WikidataHandlerParameters.DumpProcessingMode;
import org.epnoi.model.Context;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.WikidataView;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.wikidata.wdtk.dumpfiles.DumpProcessingController;

import java.util.*;
import java.util.logging.Logger;

/**
 * A factory that creates WikidataHandlers
 * 
 * @author Rafael Gonzalez-Cabero {@link http://www.github.com/fitash}
 * 
 *
 */
public class WikidataHandlerBuilder {
	private static final Logger logger = Logger.getLogger(WikidataHandlerBuilder.class.getName());
	private Core core;
	private WikidataHandlerParameters parameters;
	private boolean inMemory;
	private String dumpPath;
	private DumpProcessingMode dumpProcessingMode;
	private int timeout;

	private boolean create;
	private String wikidataViewURI;
	private DumpProcessingController dumpProcessingController;
	private WikidataViewCreator wikidataViewCreator = new WikidataViewCreator();

	private Map<String, Set<String>> labelsDictionary = new HashMap<>();

	private Map<String, Set<String>> labelsReverseDictionary = new HashMap<>();

	private Map<String, Set<String>> hypernymRelations = new HashMap<>();
	Map<String, Map<String, Set<String>>> relationsTable = new HashMap<>();

	// --------------------------------------------------------------------------------------------------

	public void init(Core core, WikidataHandlerParameters parameters) throws EpnoiInitializationException {
		logger.info("Initialiazing the WikidataHandler with the following parameters: " + parameters);
		this.core = core;
		this.parameters = parameters;

		this.dumpPath = (String) this.parameters.getParameterValue(WikidataHandlerParameters.DUMP_PATH);

		this.inMemory = (boolean) this.parameters.getParameterValue(WikidataHandlerParameters.IN_MEMORY);

		this.dumpProcessingMode = (DumpProcessingMode) this.parameters
				.getParameterValue(WikidataHandlerParameters.DUMP_FILE_MODE);
		this.timeout = (int) this.parameters.getParameterValue(WikidataHandlerParameters.TIMEOUT);

		this.create = (boolean) this.parameters.getParameterValue(WikidataHandlerParameters.CREATE_WIKIDATA_VIEW);

		this.wikidataViewURI = (String) this.parameters.getParameterValue(WikidataHandlerParameters.WIKIDATA_VIEW_URI);

		wikidataViewCreator.init(core, parameters);

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
	public WikidataHandler build() throws EpnoiInitializationException {
		logger.info("Building a WikidataHandler with the following parameters: " + parameters);
		
		

		relationsTable.put(RelationHelper.HYPERNYMY, hypernymRelations);
		if (this.create) {
			_createWikidataVIew();
		}

		logger.info("Retrieving the  WikidataView");
		try {
			if (this.inMemory) {
				return _retrieveWikidataViewInMemory();
			} else {
				return _retriveWikidataViewFromCassandra();

			}
		} catch (Exception e) {
			logger.severe("The wikidataview with uri " + this.wikidataViewURI + " couldn't be retrieved ");
			throw new EpnoiInitializationException(
					"The wikidataview " + this.wikidataViewURI + " couldn't be retrieved " + e.getMessage());
		}


	}
	// --------------------------------------------------------------------------------------------------

	private WikidataHandler _retriveWikidataViewFromCassandra() {
		logger.info("The WikidataView will be accessed directly in Cassandra");
		
		CassandraWikidataView wikidataView = new CassandraWikidataView(core, this.wikidataViewURI);
		
		return new WikidataHandlerCassandraImpl(wikidataView);
	}

	// --------------------------------------------------------------------------------------------------
	
	private WikidataHandler _retrieveWikidataViewInMemory() {
		WikidataView inMemoryWikidataView= (WikidataView) this.core.getInformationHandler().get(this.wikidataViewURI,
				RDFHelper.WIKIDATA_VIEW_CLASS);
		
		return new WikidataHandlerInMemoryImpl(inMemoryWikidataView);
	}
	
	// --------------------------------------------------------------------------------------------------

	private void _createWikidataVIew() {
		
		logger.info(
				"Creating a new WikidataView, since the retrieve flag was set false, and the create flag was set as true");
		//WikidataView inMemoryWikidataView = _generateWikidataview();
	


		WikidataView inMemoryWikidataView = this.wikidataViewCreator.create();
	
		logger.info("Storing the new built WikidataView, since the store flag was activated");
		// First we remove the WikidataWiew if there is one with the same
		// URI
		
		 
		this.core.getInformationHandler().remove(this.wikidataViewURI, RDFHelper.WIKIDATA_VIEW_CLASS);

		this.core.getInformationHandler().put(inMemoryWikidataView, Context.getEmptyContext());
	
	}
	private static WikidataView _generateWikidataview() {
//System.out.println("Creating the dictionary ");
		Map<String, Set<String>> labelsDictionary = new HashMap<>();
		Map<String, Set<String>> labelsReverseDictionary = new HashMap<>();
		
		
		Map<String, Map<String, Set<String>>> relations = new HashMap<>();
		Map<String, Set<String>> hypernymRelations = new HashMap<>();
		Set<String> destionationSet = new HashSet<String>();
		destionationSet.add("http://testTargetA");
		destionationSet.add("http://testTargetB");
		hypernymRelations.put("http://testSource", destionationSet);
		relations.put(RelationHelper.HYPERNYMY, hypernymRelations);

		
		
		Set<String> labelDictionary = new HashSet<String>();
		labelDictionary.add("http://testTargetA");
		//labelDictionary.add("http://testTargetB");
		labelsDictionary.put("target label", labelDictionary);
		labelsDictionary.put("source label", new HashSet<String>(Arrays.asList("http://testSource")));
		
		
		labelsReverseDictionary.put("http://testSource", new HashSet<String>(Arrays.asList("source label")));
		
		labelsReverseDictionary.put("http://testTargetA", new HashSet<String>(Arrays.asList("target label")));
		

		WikidataView wikidataView = new WikidataView(
				WikidataHandlerParameters.DEFAULT_URI, labelsDictionary,
				labelsReverseDictionary, relations);
		return wikidataView;
	}

}
