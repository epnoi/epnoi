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

	private static final String templateRegExp = "TEMPLATE\\[";
	private MediaWikiParserFactory pf;
	private MediaWikiParser parser;
	private List<WikipediaPage> harvestedWikipediaPages = new ArrayList<WikipediaPage>();

	// -------------------------------------------------------------------------------------------------------------------

	public WikipediaHarvester() {

	}

	// -------------------------------------------------------------------------------------------------------------------

	public void init(Core core) throws EpnoiInitializationException {
		this.termCandidatesFinder = new TermCandidatesFinder();
		this.core = core;
		this.termCandidatesFinder.init();
		pf = new MediaWikiParserFactory();
		parser = pf.createParser();
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
			count=1;
			for (WikipediaPage page : this.harvestedWikipediaPages) {
				System.out.println(count+++"||> "+page.getURI());
				//if(!core.getInformationHandler().contains(page.getURI(), RDFHelper.WIKIPEDIA_PAGE_CLASS))
				_introduceWikipediaPage(page);
			}
		}

		logger.info("Finishing the harvesting  ---------------------------------------------------------------------> ");
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

	private String _createTermDefinition(WikipediaPage wikipediaPage,
			Context context) {
		System.out.println("URI> " + wikipediaPage.getURI());
		/*
		 * LO DE ANTES Document annotatedFirstSection =
		 * retrieveAnnotatedDocument( wikipediaPage.getURI(),
		 * wikipediaPage.getURI() + "/first" +
		 * AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE);
		 */
		Document annotatedFirstSection = (Document) context.getElements().get(
				wikipediaPage.getURI() + "/first/"
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
								annotation.getEndNode().getOffset()).toString();

				firstSentence = StringUtils.outerMatching(firstSentence, "\\(",
						'(', ')');
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
		// System.out.println("S> " + sections + " " +
		// wikipediaPage.getURI());
		for (int i = sections.size() - 1; i >= 0; i--) {

			String sectionContent = wikipediaPage.getSectionsContent().get(
					sections.get(i));
			/*
			 * System.out.println(" " + sections.get(i) + "  " +
			 * wikipediaPage.getSectionsContent().get( sections.get(i)));
			 */

			String annotatedContentURI = _extractURI(wikipediaPage.getURI(),
					sections.get(i),
					AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE);
			annotatedContent = termCandidatesFinder
					.findTermCandidates(sectionContent);

			context.getElements().put(annotatedContentURI, annotatedContent);
		}

	}

	// -------------------------------------------------------------------------------------------------------------------

	private String _cleanTemplates(String parsedText) {

		return StringUtils.outerMatching(parsedText, templateRegExp, '[', ']');

	}
	
	private void _introduceWikipediaPage(WikipediaPage page) {
		Context context = new Context();
		_introduceAnnotatedContent(page, context);

		String termDefinition = _createTermDefinition(page, context);

		page.setTermDefinition(termDefinition);

		core.getInformationHandler().put(page, context);
		/*
		 * System.out.println("The definition term of " +
		 * wikipediaPage.getTerm() + " is " +
		 * wikipediaPage.getTermDefinition());
		 */
	}

	// -------------------------------------------------------------------------------------------------------------------
	/*
	 * private Document retrieveAnnotatedDocument(String URI, String
	 * annotatedContentURI) { org.epnoi.model.Content<String> annotatedContent =
	 * core .getInformationHandler().getAnnotatedContent(URI,
	 * annotatedContentURI); Document document = null; try { document =
	 * (Document) Factory .createResource( "gate.corpora.DocumentImpl",
	 * Utils.featureMap( gate.Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME,
	 * annotatedContent.getContent(),
	 * gate.Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME, "text/xml"));
	 * 
	 * } catch (ResourceInstantiationException e) { System.out .println(
	 * "Couldn't retrieve the GATE document that represents the annotated content of "
	 * + annotatedContentURI); e.printStackTrace(); } return document; }
	 */

	// -------------------------------------------------------------------------------------------------------------------

	class WikipediaPageHandler implements PageCallbackHandler {

		// -------------------------------------------------------------------------------------------------------------------

		public WikipediaPageHandler() {

		}

		// -------------------------------------------------------------------------------------------------------------------

		public void process(WikiPage page) {
			// System.out.println("> " + page.getTitle() + " " + count++);

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

				String parsedText;
				for (Content content : section.getContentList()) {
					parsedText = content.getText();
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

			harvestedWikipediaPages.add(wikipediaPage);
		}

	
	}
}
