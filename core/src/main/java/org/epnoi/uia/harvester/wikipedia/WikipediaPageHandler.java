package org.epnoi.uia.harvester.wikipedia;

import gate.Document;
import gate.Factory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Context;
import org.epnoi.model.WikipediaPage;
import org.epnoi.uia.commons.BoundedExecutor;
import org.epnoi.uia.commons.StringUtils;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.harvester.wikipedia.parse.edu.jhu.nlp.wikipedia.PageCallbackHandler;
import org.epnoi.uia.harvester.wikipedia.parse.edu.jhu.nlp.wikipedia.WikiPage;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;

//-------------------------------------------------------------------------------------------------------------------

class WikipediaPageHandler implements PageCallbackHandler {
	private static final Logger logger = Logger.getLogger(WikipediaPageHandler.class.getName());

	Core core;
	Set<String> alreadyStored;
	WikipediaHarvesterParameters parameters;
	private boolean incremental;
	private BoundedExecutor executor;

	// -------------------------------------------------------------------------------------------------------------------

	public WikipediaPageHandler() {
	}

	public void init(Core core, Set<String> alreadyStored, WikipediaHarvesterParameters parameters) {
		logger.info("Initializing the WikipediaPageHandler with the following parameters: " + parameters);
		this.core = core;
		this.alreadyStored = alreadyStored;
		this.parameters = parameters;
		this.incremental = (boolean) parameters.getParameterValue(WikipediaHarvesterParameters.INCREMENTAL);
		int numberOfThreads = (Integer) this.parameters.getParameterValue(WikipediaHarvesterParameters.NUMBER_OF_THREADS);
		this.executor = new BoundedExecutor(Executors.newFixedThreadPool(numberOfThreads), numberOfThreads);

	}

	// -------------------------------------------------------------------------------------------------------------------

	public void processWikipediaPage(WikiPage page) {
	

		String cleanedPageTitle = page.getTitle().replaceAll("\\n", "").replaceAll("\\s+$", "");

		String localPartOfTermURI = StringUtils.cleanOddCharacters(page.getTitle());

		localPartOfTermURI = localPartOfTermURI.replaceAll("\\n", "").replaceAll("\\s+$", "").replaceAll("\\s+", "_");

		String uri=WikipediaHarvester.wikipediaPath + localPartOfTermURI;
		
		
		boolean isAlreadyStored = alreadyStored.contains(uri);

		//if (wikipediaPage.getSections().size() > WikipediaHarvester.MIN_SECTIONS) {

			if (incremental)
				if (!isAlreadyStored) {
					logger.info("Introducing " + uri);
					_introduceWikipediaPage(page, isAlreadyStored);
				} else {
					logger.info("The WikipediaPage " + uri + " was already stored");
				}

			else {
				_introduceWikipediaPage(page, isAlreadyStored);
			}
		}
	//}

	// -------------------------------------------------------------------------------------------------------------------

	private void _introduceWikipediaPage(WikiPage wikipediaPage, boolean isAlreadyStored) {

		try {
			
			Runnable task = new WikipediaPageIntroductionTask(core, wikipediaPage, Context.getEmptyContext(),
					isAlreadyStored);
			
		//	System.out.println("TASK> "+task);
			executor.submitTask(task);
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("This wikipedia page couldn't be introduced " + wikipediaPage.getTitle());
		}
	}

}