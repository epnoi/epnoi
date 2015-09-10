package org.epnoi.uia.harvester.wikipedia;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Context;
import org.epnoi.model.WikipediaPage;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.uia.commons.WikipediaPagesRetriever;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.harvester.wikipedia.parse.edu.jhu.nlp.wikipedia.PageCallbackHandler;
import org.epnoi.uia.harvester.wikipedia.parse.edu.jhu.nlp.wikipedia.WikiPage;
import org.epnoi.uia.harvester.wikipedia.parse.edu.jhu.nlp.wikipedia.WikiXMLParser;
import org.epnoi.uia.harvester.wikipedia.parse.edu.jhu.nlp.wikipedia.WikiXMLParserFactory;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;

import gate.Document;
import gate.Factory;

public class WikipediaHarvester {
	// -Xmx1g
	private WikipediaHarvesterParameters parameters;
	private String wikipediaDumpPath = "/opt/epnoi/epnoideployment/firstReviewResources/wikipedia/";
	public static String wikipediaPath = "http://en.wikipedia.org/wiki/";
	public static boolean incremental = false;

	private Core core;
	private static final Logger logger = Logger.getLogger(WikipediaHarvester.class.getName());

	public static final int MIN_SECTIONS = 2;

	private Set<String> alreadyStoredWikipediaPages;

	// -------------------------------------------------------------------------------------------------------------------

	public WikipediaHarvester() {

	}

	// -------------------------------------------------------------------------------------------------------------------

	public void init(Core core, WikipediaHarvesterParameters parameters) throws EpnoiInitializationException {
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

	private String _extractURI(String URI, String section, String annotationType) {

		String cleanedSection = section.replaceAll("\\s+$", "").replaceAll("\\s+", "_");

		return URI + "/" + cleanedSection + "/" + annotationType;
	}

	// -------------------------------------------------------------------------------------------------------------------

	public void _putWikipediaPageAnnotatedContent(WikipediaPage wikipediaPage, Context context) {
		List<String> sections = wikipediaPage.getSections();

		for (int i = sections.size() - 1; i >= 0; i--) {

			String sectionContent = wikipediaPage.getSectionsContent().get(sections.get(i));

			String annotatedContentURI = _extractURI(wikipediaPage.getURI(), sections.get(i),

			AnnotatedContentHelper.CONTENT_TYPE_OBJECT_XML_GATE);
			_putWikipediaPageSectionAnnnotatedContent(wikipediaPage, sectionContent, annotatedContentURI);
		}

	}

	// -------------------------------------------------------------------------------------------------------------------

	private void _putWikipediaPageSectionAnnnotatedContent(WikipediaPage wikipediaPage, String sectionContent,
			String annotatedContentURI) {
		// First we obtain the linguistic annotation of the content of the
		// section
		Document sectionAnnotatedContent = this.core.getNLPHandler().process(sectionContent);

		// Then we introduce it in the UIA
		// We create the selector
		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.URI, wikipediaPage.getURI());
		selector.setProperty(SelectorHelper.ANNOTATED_CONTENT_URI, annotatedContentURI);

		selector.setProperty(SelectorHelper.TYPE, RDFHelper.WIKIPEDIA_PAGE_CLASS);

		// Then we store it
		core.getInformationHandler().setAnnotatedContent(selector, new org.epnoi.model.Content<Object>(
				sectionAnnotatedContent, AnnotatedContentHelper.CONTENT_TYPE_OBJECT_XML_GATE));
		Factory.deleteResource(sectionAnnotatedContent);
	}

	// -------------------------------------------------------------------------------------------------------------------

	private void _putWikipediaPage(WikipediaPage page, Context context) {
		// If the wikipedia page is already stored, we delete it

		if (this.alreadyStoredWikipediaPages.contains(page.getURI())) {
			this.core.getInformationHandler().remove(page.getURI(), RDFHelper.WIKIPEDIA_PAGE_CLASS);

		}

		long currenttime = System.currentTimeMillis();
		_putWikipediaPageAnnotatedContent(page, context);
		core.getInformationHandler().put(page, context);
		long time = System.currentTimeMillis() - currenttime;
		logger.info(page.getURI() + " took " + time + " to be annotated and stored");

	}

	// -------------------------------------------------------------------------------------------------------------------

	class WikipediaPageHandler implements PageCallbackHandler {

		// -------------------------------------------------------------------------------------------------------------------

		public WikipediaPageHandler() {

		}

		// -------------------------------------------------------------------------------------------------------------------

		public void processWikipediaPage(WikiPage page) {
			WikipediaPageParser parser = new WikipediaPageParser();

			WikipediaPage wikipediaPage = parser.parse(page);
			if (wikipediaPage.getSections().size() > WikipediaHarvester.MIN_SECTIONS) {

				if (incremental)
					if (!alreadyStoredWikipediaPages.contains(wikipediaPage.getURI())) {
						logger.info("Introducing " + wikipediaPage.getURI());
						_introduceWikipediaPage(wikipediaPage);
					} else {
						logger.info("The WikipediaPage " + wikipediaPage.getURI() + " was already stored");
					}

				else {
					_introduceWikipediaPage(wikipediaPage);
				}
			}
		}

		// -------------------------------------------------------------------------------------------------------------------

		private void _introduceWikipediaPage(WikipediaPage wikipediaPage) {

			try {

				_putWikipediaPage(wikipediaPage, Context.getEmptyContext());

			} catch (Exception e) {
				e.printStackTrace();
				logger.severe("This wikipedia page couldn't be introduced " + wikipediaPage.getURI());
			}
		}
	}
	// -------------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		WikipediaHarvester wikipediaHarvester = new WikipediaHarvester();
		WikipediaHarvesterParameters parameters = new WikipediaHarvesterParameters();

		parameters.setParameter(WikipediaHarvesterParameters.DUMPS_DIRECTORY_PATH,
				"/opt/epnoi/epnoideployment/firstReviewResources/wikipedia/");
		parameters.setParameter(WikipediaHarvesterParameters.INCREMENTAL, true);
		parameters.setParameter(WikipediaHarvesterParameters.NUMBER_OF_THREADS, 3);
		// Core core = null;
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
