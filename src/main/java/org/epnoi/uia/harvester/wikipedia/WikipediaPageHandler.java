package org.epnoi.uia.harvester.wikipedia;

import gate.Document;
import gate.Factory;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Context;
import org.epnoi.model.WikipediaPage;
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

	// -------------------------------------------------------------------------------------------------------------------

	public WikipediaPageHandler(Core core, Set<String> alreadyStored,
			WikipediaHarvesterParameters parameters) {
		this.core = core;
		this.alreadyStored = alreadyStored;
		this.parameters = parameters;
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

	public void _putWikipediaPageAnnotatedContent(WikipediaPage wikipediaPage,
			Context context) {
		List<String> sections = wikipediaPage.getSections();

		for (int i = sections.size() - 1; i >= 0; i--) {

			String sectionContent = wikipediaPage.getSectionsContent().get(
					sections.get(i));

			String annotatedContentURI = _extractURI(wikipediaPage.getURI(),
					sections.get(i),

					AnnotatedContentHelper.CONTENT_TYPE_OBJECT_XML_GATE);
			_putWikipediaPageSectionAnnnotatedContent(wikipediaPage,
					sectionContent, annotatedContentURI);
		}

	}

	// -------------------------------------------------------------------------------------------------------------------

	private String _extractURI(String URI, String section, String annotationType) {

		String cleanedSection = section.replaceAll("\\s+$", "").replaceAll(
				"\\s+", "_");

		return URI + "/" + cleanedSection + "/" + annotationType;
	}

	// -------------------------------------------------------------------------------------------------------------------

	private void _putWikipediaPageSectionAnnnotatedContent(
			WikipediaPage wikipediaPage, String sectionContent,
			String annotatedContentURI) {
		// First we obtain the linguistic annotation of the content of the
		// section
		Document sectionAnnotatedContent = this.core.getNLPHandler().process(
				sectionContent);

		// Then we introduce it in the UIA
		// We create the selector
		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.URI, wikipediaPage.getURI());
		selector.setProperty(SelectorHelper.ANNOTATED_CONTENT_URI,
				annotatedContentURI);

		selector.setProperty(SelectorHelper.TYPE,
				RDFHelper.WIKIPEDIA_PAGE_CLASS);

		// Then we store it
		core.getInformationHandler().setAnnotatedContent(
				selector,
				new org.epnoi.model.Content<Object>(sectionAnnotatedContent,
						AnnotatedContentHelper.CONTENT_TYPE_OBJECT_XML_GATE));
		Factory.deleteResource(sectionAnnotatedContent);
	}

	// -------------------------------------------------------------------------------------------------------------------

	private void _putWikipediaPage(WikipediaPage page, Context context) {
		// If the wikipedia page is already stored, we delete it

		if (this.alreadyStored.contains(page.getURI())) {
			this.core.getInformationHandler().remove(page.getURI(),
					RDFHelper.WIKIPEDIA_PAGE_CLASS);

		}

		long currenttime = System.currentTimeMillis();
		_putWikipediaPageAnnotatedContent(page, context);
		core.getInformationHandler().put(page, context);
		long time = System.currentTimeMillis() - currenttime;
		logger.info(page.getURI() + " took " + time
				+ " to be annotated and stored");

	}

	// -------------------------------------------------------------------------------------------------------------------

	private void _introduceWikipediaPage(WikipediaPage wikipediaPage) {

		try {

			_putWikipediaPage(wikipediaPage, Context.getEmptyContext());

		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("This wikipedia page couldn't be introduced "
					+ wikipediaPage.getURI());
		}
	}
}