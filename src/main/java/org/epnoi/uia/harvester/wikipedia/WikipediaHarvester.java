package org.epnoi.uia.harvester.wikipedia;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.Utils;
import gate.creole.ResourceInstantiationException;
import gate.util.InvalidOffsetException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.ContentHelper;
import org.epnoi.model.Context;
import org.epnoi.model.WikipediaPage;
import org.epnoi.uia.commons.StringUtils;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.exceptions.EpnoiInitializationException;
import org.epnoi.uia.harvester.oaipmh.OAIPMHHarvester;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.parser.Content;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.parser.Section;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParser;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParserFactory;
import org.epnoi.uia.harvester.wikipedia.parse.edu.jhu.nlp.wikipedia.PageCallbackHandler;
import org.epnoi.uia.harvester.wikipedia.parse.edu.jhu.nlp.wikipedia.WikiPage;
import org.epnoi.uia.harvester.wikipedia.parse.edu.jhu.nlp.wikipedia.WikiXMLParser;
import org.epnoi.uia.harvester.wikipedia.parse.edu.jhu.nlp.wikipedia.WikiXMLParserFactory;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.learner.nlp.TermCandidatesFinder;

public class WikipediaHarvester {
	private static String wikipediaDumpPath = "/epnoi/epnoideployment/definitionalSentencesCreator/wikipedia/";
	public static String wikipediaPath = "http://en.wikipedia.org/wiki/";

	private TermCandidatesFinder termCandidatesFinder;

	private int count = 0;
	private Core core;
	private static final Logger logger = Logger
			.getLogger(WikipediaHarvester.class.getName());

	List<WikiPage> wikipediaPages = new ArrayList<WikiPage>();

	// -------------------------------------------------------------------------------------------------------------------

	public WikipediaHarvester() {

	}

	// -------------------------------------------------------------------------------------------------------------------

	public void init(Core core) throws EpnoiInitializationException {
		this.termCandidatesFinder = new TermCandidatesFinder();
		this.core = core;
		this.termCandidatesFinder.init();
	}

	// -------------------------------------------------------------------------------------------------------------------

	public void harvest() {
		logger.info("Starting the harvesting ----------------------------------------------------------------------");

		File folder = new File(WikipediaHarvester.wikipediaDumpPath);

		File[] listOfFiles = folder.listFiles();
		logger.info("Harvesting the directory/repository "
				+ folder.getAbsolutePath());

		for (int i = 0; i < listOfFiles.length; i++) {
			logger.info("Harvesting " + listOfFiles[i]);

			WikiXMLParser wxsp = WikiXMLParserFactory
					.getSAXParser(listOfFiles[i].getAbsolutePath());

			try {

				wxsp.setPageCallback(new WikipediaPageHandler());

				wxsp.parse();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		logger.info("Finishing the harvesting  ---------------------------------------------------------------------> "
				+ wikipediaPages.size());
	}

	// -------------------------------------------------------------------------------------------------------------------

	// -------------------------------------------------------------------------------------------------------------------

	private String _extractURI(String URI, String section, String annotationType) {

		String cleanedSection = section.replaceAll("\\s+$", "").replaceAll(
				"\\s+", "_");

		return URI + "/" + cleanedSection + "/" + annotationType;
	}

	// -------------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		WikipediaHarvester wikipediaHarvester = new WikipediaHarvester();

		Core core = CoreUtility.getUIACore();
		try {
			wikipediaHarvester.init(core);
		} catch (EpnoiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wikipediaHarvester.harvest();
	}

	// -------------------------------------------------------------------------------------------------------------------

	class WikipediaPageHandler implements PageCallbackHandler {
		private static final String templateRegExp = "TEMPLATE\\[";
		MediaWikiParserFactory pf = new MediaWikiParserFactory();
		MediaWikiParser parser = pf.createParser();

		// -------------------------------------------------------------------------------------------------------------------

		public WikipediaPageHandler() {

		}

		// -------------------------------------------------------------------------------------------------------------------

		public void process(WikiPage page) {
			System.out.println("> " + page.getTitle() + " " + count++);
			wikipediaPages.add(page);

			ParsedPage parsedPage = parser.parse(page.getWikiText());

			WikipediaPage wikipediaPage = new WikipediaPage();
			Context context = new Context();

			String cleanedPageTitle = page.getTitle().replaceAll("\\n", "")
					.replaceAll("\\s+$", "");

			String localPartOfTermURI = page.getTitle().replaceAll("\\n", "")
					.replaceAll("\\s+$", "").replaceAll("\\s+", "_");

			wikipediaPage.setURI(WikipediaHarvester.wikipediaPath
					+ localPartOfTermURI);
			wikipediaPage.setTerm(cleanedPageTitle);

			for (Section section : parsedPage.getSections()) {

				String sectionName = (section.getTitle() == null) ? "first"
						: section.getTitle();
				wikipediaPage.addSection(sectionName);
				String sectionContent = "";

				for (Content content : section.getContentList()) {
					String parsedText = content.getText();
					if (!parsedText.equals(sectionName)) {

						String lineWithoutTemplates = _cleanTemplates(parsedText);

						lineWithoutTemplates = lineWithoutTemplates.replaceAll(
								"\\s+", " ");

						sectionContent = sectionContent + lineWithoutTemplates;
					}

				}
				wikipediaPage.addSectionContent(sectionName, sectionContent);

			}
			// System.out.println("----------------> " + wikipediaPage);
			// The first section is missing in some articles... we simply add it
			if (!wikipediaPage.getSections().contains("first")) {
				wikipediaPage.getSections().add("first");
				wikipediaPage.getSectionsContent().put("first", "");
			}
			_introduceAnnotatedContent(wikipediaPage, context);

			// String termDefinition = _createTermDefinition(wikipediaPage);
			// wikipediaPage.setTermDefinition(termDefinition);

			core.getInformationHandler().put(wikipediaPage, context);

			System.out.println("The definition term of "
					+ wikipediaPage.getTerm() + " is "
					+ wikipediaPage.getTermDefinition());

		}

		// -------------------------------------------------------------------------------------------------------------------

		private String _createTermDefinition(WikipediaPage wikipediaPage,
				Context context) {
			System.out.println("URI> " + wikipediaPage.getURI());
			/*
			 * LO DE ANTES Document annotatedFirstSection =
			 * retrieveAnnotatedDocument( wikipediaPage.getURI(),
			 * wikipediaPage.getURI() + "/first" +
			 * AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE);
			 */
			Document annotatedFirstSection = (Document) context
					.getElements()
					.get(wikipediaPage.getURI() + "/first/"
							+ AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE);

			// System.out.println("-----> "+annotatedFirstSection.toXml());

			AnnotationSet sentenceAnnotations = annotatedFirstSection
					.getAnnotations().get("Sentence");
			if (!sentenceAnnotations.isEmpty()) {
				Long offset = annotatedFirstSection.getAnnotations()
						.get("Sentence").firstNode().getOffset();
				Annotation annotation = sentenceAnnotations.get("Sentence")
						.get(offset).iterator().next();

				String firstSentence = "";
				try {

					firstSentence = annotatedFirstSection
							.getContent()
							.getContent(annotation.getStartNode().getOffset(),
									annotation.getEndNode().getOffset())
							.toString();

					firstSentence = StringUtils.outerMatching(firstSentence,
							"\\(", '(', ')');
				} catch (InvalidOffsetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return firstSentence;

			} else {
				return "";
			}
		}

		// -------------------------------------------------------------------------------------------------------------------

		private void _introduceAnnotatedContent(WikipediaPage wikipediaPage,
				Context context) {
			Document annotatedContent = null;
			List<String> sections = wikipediaPage.getSections();
			System.out.println("S> " + sections + " " + wikipediaPage.getURI());
			for (int i = sections.size() - 1; i >= 0; i--) {

				String sectionContent = wikipediaPage.getSectionsContent().get(
						sections.get(i));
				/*
				 * System.out.println(" " + sections.get(i) + "  " +
				 * wikipediaPage.getSectionsContent().get( sections.get(i)));
				 */

				String annotatedContentURI = _extractURI(
						wikipediaPage.getURI(), sections.get(i),
						AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE);
				annotatedContent = termCandidatesFinder
						.findTermCandidates(sectionContent);

				/*
				 * LO ANTERIOR core.getInformationHandler()
				 * .setAnnotatedContent( wikipediaPage.getURI(),
				 * annotatedContentURI, new org.epnoi.model.Content<String>(
				 * annotatedContent.toXml(),
				 * ContentHelper.CONTENT_TYPE_TEXT_XML));
				 */
				context.getElements()
						.put(annotatedContentURI, annotatedContent);
			}

		}

		// -------------------------------------------------------------------------------------------------------------------

		private String _cleanTemplates(String parsedText) {

			return StringUtils.outerMatching(parsedText, templateRegExp, '[',
					']');

		}

		// -------------------------------------------------------------------------------------------------------------------
/*
		private Document retrieveAnnotatedDocument(String URI,
				String annotatedContentURI) {
			org.epnoi.model.Content<String> annotatedContent = core
					.getInformationHandler().getAnnotatedContent(URI,
							annotatedContentURI);
			Document document = null;
			try {
				document = (Document) Factory
						.createResource(
								"gate.corpora.DocumentImpl",
								Utils.featureMap(
										gate.Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME,
										annotatedContent.getContent(),
										gate.Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME,
										"text/xml"));

			} catch (ResourceInstantiationException e) { 
				System.out
						.println("Couldn't retrieve the GATE document that represents the annotated content of "
								+ annotatedContentURI);
				e.printStackTrace();
			}
			return document;
		}
*/
	}
}
