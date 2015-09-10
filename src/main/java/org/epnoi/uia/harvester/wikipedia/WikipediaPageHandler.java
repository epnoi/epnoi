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
import org.epnoi.uia.core.Core;
import org.epnoi.uia.harvester.wikipedia.parse.edu.jhu.nlp.wikipedia.PageCallbackHandler;
import org.epnoi.uia.harvester.wikipedia.parse.edu.jhu.nlp.wikipedia.WikiPage;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;

//-------------------------------------------------------------------------------------------------------------------

class WikipediaPageHandler implements PageCallbackHandler {
	private static final Logger logger = Logger
			.getLogger(WikipediaPageHandler.class.getName());

	Core core;
	Set<String> alreadyStored;
	WikipediaHarvesterParameters parameters;
	private boolean incremental;
	private BoundedExecutor executor;
	

	// -------------------------------------------------------------------------------------------------------------------

	public WikipediaPageHandler() {
	}
	
	public void init(Core core, Set<String> alreadyStored,
			WikipediaHarvesterParameters parameters){
		this.core = core;
		this.alreadyStored = alreadyStored;
		this.parameters = parameters;
		this.incremental= (boolean) parameters.getParameterValue(WikipediaHarvesterParameters.INCREMENTAL);
		int numberOfThreads = (Integer) parameters
				.getParameterValue(WikipediaHarvesterParameters.NUMBER_OF_THREADS);
		this.executor = new BoundedExecutor(
				Executors.newFixedThreadPool(numberOfThreads), numberOfThreads);
	
	}

	// -------------------------------------------------------------------------------------------------------------------

	public void processWikipediaPage(WikiPage page) {
		WikipediaPageParser parser = new WikipediaPageParser();

		WikipediaPage wikipediaPage = parser.parse(page);
		if (wikipediaPage.getSections().size() > WikipediaHarvester.MIN_SECTIONS) {

			if (incremental)
				if (!alreadyStored.contains(wikipediaPage.getURI())) {
					logger.info("Introducing " + wikipediaPage.getURI());
					_introduceWikipediaPage(wikipediaPage);
				} else {
					logger.info("The WikipediaPage " + wikipediaPage.getURI()
							+ " was already stored");
				}

			else {
				_introduceWikipediaPage(wikipediaPage);
			}
		}
	}
	
	// -------------------------------------------------------------------------------------------------------------------

	
	private void _introduceWikipediaPage(WikipediaPage wikipediaPage) {

		try {

	//		_putWikipediaPage(wikipediaPage, Context.getEmptyContext());

		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("This wikipedia page couldn't be introduced "
					+ wikipediaPage.getURI());
		}
	}

	
}