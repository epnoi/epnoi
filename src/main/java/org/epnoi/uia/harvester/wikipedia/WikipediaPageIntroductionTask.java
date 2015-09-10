package org.epnoi.uia.harvester.wikipedia;

import gate.Document;
import gate.Factory;

import java.util.List;
import java.util.Set;

import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Context;
import org.epnoi.model.WikipediaPage;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;

public class WikipediaPageIntroductionTask implements Runnable{
private Core core;
private Set<String> alreadyStored;
	@Override
	public void run() {
		
		
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
			//logger.info(page.getURI() + " took " + time
			//		+ " to be annotated and stored");

		}

		// -------------------------------------------------------------------------------------------------------------------

		

}
