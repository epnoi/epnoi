package org.epnoi.uia.harvester.wikipedia;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.Utils;
import gate.creole.ResourceInstantiationException;
import gate.util.InvalidOffsetException;

import java.util.ArrayList;
import java.util.List;

import org.epnoi.model.ContentHelper;
import org.epnoi.model.Context;
import org.epnoi.model.WikipediaPage;
import org.epnoi.uia.commons.StringUtils;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
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
	public static String wikipediaPath = "http://en.wikipedia.org/wiki/";
	private static String wikipediaDumpPath;
	private TermCandidatesFinder termCandidatesFinder;
	private String wikipediaDumpFilename = "test.xml.gz";
	private Core core;

	// -------------------------------------------------------------------------------------------------------------------

	public WikipediaHarvester() {
		this.termCandidatesFinder = new TermCandidatesFinder();
		this.core = CoreUtility.getUIACore();
		this.termCandidatesFinder.init();
	}

	// -------------------------------------------------------------------------------------------------------------------

	public void harvest() {
		System.out
				.println("Starting the harvesting ----------------------------------------------------------------------");

		WikipediaHarvester.wikipediaDumpPath = WikipediaHarvester.class
				.getResource(wikipediaDumpFilename).getPath();

		WikiXMLParser wxsp = WikiXMLParserFactory
				.getSAXParser(WikipediaHarvester.wikipediaDumpPath);

		List<WikipediaPage> wikipediaPages = new ArrayList<WikipediaPage>();

		try {

			wxsp.setPageCallback(new WikipediaPageHandler());

			wxsp.parse();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * System.out.println("----------------->" +
		 * this.core.getInformationHandler().get(
		 * "http://en.wikipedia.org/wiki/Autism",
		 * RDFHelper.WIKIPEDIA_PAGE_CLASS));
		 */
		System.out
				.println("Finishing the harvesting  ---------------------------------------------------------------------");
	}

	// -------------------------------------------------------------------------------------------------------------------

	// -------------------------------------------------------------------------------------------------------------------

	private String _extractURI(String URI, String section) {

		String cleanedSection = section.replaceAll("\\s+$", "").replaceAll(
				"\\s+", "_");

		return URI + "/" + cleanedSection;
	}

	// -------------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		WikipediaHarvester wikipediaHarvester = new WikipediaHarvester();
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

			ParsedPage parsedPage = parser.parse(page.getWikiText());
			WikipediaPage wikipediaPage = new WikipediaPage();
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

				/*
				 * System.out .println(
				 * "============================================================================================================================================ "
				 * + sectionName);
				 */
				for (Content content : section.getContentList()) {
					// System.out.println();
					String parsedText = content.getText();
					// System.out.println(">" + parsedText + "<");
					if (!parsedText.equals(sectionName)) {

						String lineWithoutTemplates = _cleanTemplates(parsedText);

						/*
						 * String regexp2 = "\\("; String cleanLine =
						 * StringUtils.outerMatching(lineWithoutTemplates,
						 * regexp2, '(', ')');
						 */
						// System.out.println("The cleaned result is: " +
						// cleanLine);

						lineWithoutTemplates = lineWithoutTemplates.replaceAll(
								"\\s+", " ");

						sectionContent = sectionContent + lineWithoutTemplates;
					}

				}
				wikipediaPage.addSectionContent(sectionName, sectionContent);

			}
			if (!wikipediaPage.getSections().contains("first")) {
				wikipediaPage.getSections().add("first");
				wikipediaPage.getSectionsContent().put("first", "");
			}
			_introduceAnnotatedContent(wikipediaPage);
			// System.out.println("LA URI ES " + wikipediaPage.getURI());
			String termDefinition = _createTermDefinition(wikipediaPage);
			wikipediaPage.setTermDefinition(termDefinition);
			core.getInformationHandler().put(wikipediaPage,
					Context.getEmptyContext());

			System.out.println("The definition term of "
					+ wikipediaPage.getTerm() + " is "
					+ wikipediaPage.getTermDefinition());

		}

		// -------------------------------------------------------------------------------------------------------------------

		private String _createTermDefinition(WikipediaPage wikipediaPage) {
			System.out.println("URI> " + wikipediaPage.getURI());
			Document annotatedFirstSection = retrieveAnnotatedDocument(wikipediaPage
					.getURI() + "/first");

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

		private void _introduceAnnotatedContent(WikipediaPage wikipediaPage) {
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
				annotatedContent = termCandidatesFinder
						.findTermCandidates(sectionContent);
				core.getInformationHandler().setAnnotatedContent(
						_extractURI(wikipediaPage.getURI(), sections.get(i)),
						new org.epnoi.model.Content<String>(annotatedContent
								.toXml(), ContentHelper.CONTENT_TYPE_TEXT_XML));
				/*
				 * if (sectionContent.length() < 4)
				 * 
				 * System.out.println("---------------Z> " +
				 * core.getInformationHandler().getAnnotatedContent(
				 * _extractURI(wikipediaPage.getURI(), sections.get(i))));
				 */
			}

		}

		// -------------------------------------------------------------------------------------------------------------------

		private String _cleanTemplates(String parsedText) {

			return StringUtils.outerMatching(parsedText, templateRegExp, '[',
					']');

		}

		// -------------------------------------------------------------------------------------------------------------------

		private Document retrieveAnnotatedDocument(String URI) {
			org.epnoi.model.Content<String> annotatedContent = core
					.getInformationHandler().getAnnotatedContent(URI);
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

			} catch (ResourceInstantiationException e) { // TODO Auto-generated
				System.out
						.println("Couldn't retrieve the GATE document that represents the annotated content of "
								+ URI);
				e.printStackTrace();
			}
			return document;
		}

	}
}
