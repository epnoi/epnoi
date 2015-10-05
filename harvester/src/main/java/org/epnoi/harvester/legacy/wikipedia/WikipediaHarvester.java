package org.epnoi.harvester.legacy.wikipedia;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.epnoi.harvester.legacy.wikipedia.parse.edu.jhu.nlp.wikipedia.WikiXMLParser;
import org.epnoi.harvester.legacy.wikipedia.parse.edu.jhu.nlp.wikipedia.WikiXMLParserFactory;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.Core;
import org.epnoi.uia.commons.WikipediaPagesRetriever;
import org.epnoi.uia.core.CoreUtility;

public class WikipediaHarvester {
	private static final Logger logger = Logger.getLogger(WikipediaHarvester.class.getName());
	// -Xmx1g
	private WikipediaHarvesterParameters parameters;
	private String wikipediaDumpPath = "/opt/epnoi/epnoideployment/firstReviewResources/wikipedia/";
	public static String wikipediaPath = "http://en.wikipedia.org/wiki/";
	public static boolean incremental = false;

	private Core core;
	

	public static final int MIN_SECTIONS = 2;

	private Set<String> alreadyStoredWikipediaPages;


	// -------------------------------------------------------------------------------------------------------------------

	public WikipediaHarvester() {

	}

	// -------------------------------------------------------------------------------------------------------------------

	public void init(Core core, WikipediaHarvesterParameters parameters) throws EpnoiInitializationException {
		logger.info("Initializing the WikipediaHarvester with the following parameters " + parameters);

		this.parameters = parameters;
		incremental = (boolean) parameters.getParameterValue(WikipediaHarvesterParameters.INCREMENTAL);
		this.wikipediaDumpPath = (String) parameters
				.getParameterValue(WikipediaHarvesterParameters.DUMPS_DIRECTORY_PATH);
		this.core = core;

		_findAlreadyStoredWikidpediaPages();
	}

	// -------------------------------------------------------------------------------------------------------------------

	private void _findAlreadyStoredWikidpediaPages() {
		List<String> wikipediaPages = WikipediaPagesRetriever.getWikipediaArticles(core);
		this.alreadyStoredWikipediaPages = new HashSet<String>(wikipediaPages);
		logger.info("Found " + this.alreadyStoredWikipediaPages.size() + " already stored in the UIA");
	}

	// -------------------------------------------------------------------------------------------------------------------

	public void harvest() {
		logger.info("Starting the wikipedia harvesting!");
		logger.info("The parameters are: " + this.parameters);
		_harvestWikipediaDumpsPath();

		logger.info(
				"Finishing the harvesting  ---------------------------------------------------------------------> ");
	}

	// -------------------------------------------------------------------------------------------------------------------

	private void _harvestWikipediaDumpsPath() {
		File folder = new File(this.wikipediaDumpPath);

		File[] listOfDumps = folder.listFiles();

		logger.info("Harvesting the path " + folder.getAbsolutePath());

		for (int i = 0; i < listOfDumps.length; i++) {
			_harvestWikipediaDump(listOfDumps[i]);

		}
	}

	// -------------------------------------------------------------------------------------------------------------------

	private void _harvestWikipediaDump(File dump) {
		logger.info("Harvesting the wikipedia dump " + dump);

		WikiXMLParser wikipediaDumpParser = WikiXMLParserFactory.getSAXParser(dump.getAbsolutePath());
		WikipediaPageHandler wikipediaPageHandler = new WikipediaPageHandler();
		wikipediaPageHandler.init(core, alreadyStoredWikipediaPages, parameters);
		try {
			wikipediaDumpParser.setPageCallback(wikipediaPageHandler);
			wikipediaDumpParser.parse();
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			wikipediaPageHandler = null;
			wikipediaDumpParser = null;
			wikipediaPageHandler = null;

		}

	}

	// -------------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		WikipediaHarvester wikipediaHarvester = new WikipediaHarvester();
		WikipediaHarvesterParameters parameters = new WikipediaHarvesterParameters();

		parameters.setParameter(WikipediaHarvesterParameters.DUMPS_DIRECTORY_PATH,
				"/opt/epnoi/epnoideployment/firstReviewResources/wikipedia/");
		parameters.setParameter(WikipediaHarvesterParameters.INCREMENTAL, true);
		parameters.setParameter(WikipediaHarvesterParameters.NUMBER_OF_THREADS, 8);

		Core core = CoreUtility.getUIACore();

		try {
			wikipediaHarvester.init(core, parameters);
		} catch (EpnoiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		wikipediaHarvester.harvest();
	}
}
