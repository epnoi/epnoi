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
	private boolean retrieve;
	private String wikidataViewURI;
	private DumpProcessingController dumpProcessingController;
	private WikidataViewCreator wikidataViewCreator = new WikidataViewCreator();
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
				.getParameterValue(WikidataHandlerParameters.DUMP_PATH);

		this.offlineMode = (boolean) this.parameters
				.getParameterValue(WikidataHandlerParameters.OFFLINE_MODE);

		this.dumpProcessingMode = (DumpProcessingMode) this.parameters
				.getParameterValue(WikidataHandlerParameters.DUMP_FILE_MODE);
		this.timeout = (int) this.parameters
				.getParameterValue(WikidataHandlerParameters.TIMEOUT);

		this.store = (boolean) this.parameters
				.getParameterValue(WikidataHandlerParameters.STORE_WIKIDATA_VIEW);

		this.retrieve = (boolean) this.parameters
				.getParameterValue(WikidataHandlerParameters.RETRIEVE_WIKIDATA_VIEW);

		this.wikidataViewURI = (String) this.parameters
				.getParameterValue(WikidataHandlerParameters.WIKIDATA_VIEW_URI);
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
	public WikidataHandler build() {
		logger.info("Building a WikidataHandler with the following parameters: "
				+ parameters);
		WikidataView wikidataView = null;

		relationsTable.put(RelationHelper.HYPERNYM, hypernymRelations);
		if (this.retrieve) {
			// If the retrieve flag is activated, we get the core of the handler
			// (the WikidataView) from the UIA
			logger.info("Retrieving the  WikidataView, since the retrieve flag was activated");
			wikidataView = (WikidataView) this.core.getInformationHandler()
					.get(this.wikidataViewURI, RDFHelper.WIKIDATA_VIEW_CLASS);
			logger.info("The WikidataView " + this.wikidataViewURI
					+ " has been retrieved: " + wikidataView.toString());

		} else {
			logger.info("Creating a new WikidataView, since the retrieve flag was set false");

			wikidataView = this.wikidataViewCreator.create();

		}

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

		return new WikidataHandlerImpl(wikidataView);

	}

	
	// --------------------------------------------------------------------------------------------------

	public static void main(String[] args) throws IOException {
		System.out.println("Starting the WikiDataHandlerBuilder");

		Core core = CoreUtility.getUIACore();

		WikidataHandlerParameters parameters = new WikidataHandlerParameters();

		parameters.setParameter(WikidataHandlerParameters.WIKIDATA_VIEW_URI,
				"http://wikidataView");
		parameters.setParameter(WikidataHandlerParameters.STORE_WIKIDATA_VIEW,
				true);

		parameters.setParameter(
				WikidataHandlerParameters.RETRIEVE_WIKIDATA_VIEW, false);
		parameters.setParameter(WikidataHandlerParameters.OFFLINE_MODE, true);
		parameters.setParameter(WikidataHandlerParameters.DUMP_FILE_MODE,
				DumpProcessingMode.JSON);
		parameters.setParameter(WikidataHandlerParameters.TIMEOUT, 100);
		parameters.setParameter(WikidataHandlerParameters.DUMP_PATH,
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
			if (sourceIRIs != null) {

				for (String sourceIRI : sourceIRIs) {
					// System.out.println("sourceIRI " + sourceIRI);
					for (String targetIRI : consideredRelations.get(sourceIRI)) {
						if (targetIRI != null) {
							if (this.wikidataView.getLabelsReverseDictionary()
									.get(targetIRI) != null) {

								for (String destinationTarget : this.wikidataView
										.getLabelsReverseDictionary().get(
												targetIRI)) {
									targetLabels.add(destinationTarget);
								}
							}

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


}
