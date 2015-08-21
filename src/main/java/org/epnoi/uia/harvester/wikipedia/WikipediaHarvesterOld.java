package org.epnoi.uia.harvester.wikipedia;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.util.InvalidOffsetException;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Context;
import org.epnoi.model.WikipediaPage;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.uia.commons.GateUtils;
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
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.learner.nlp.TermCandidatesFinder;

public class WikipediaHarvesterOld {
	// -Xmx1g
	private static String wikipediaDumpPath = "/epnoi/epnoideployment/firstReviewResources/wikipedia/";
	public static String wikipediaPath = "http://en.wikipedia.org/wiki/";
	public static boolean INCREMENTAL = true;

	private TermCandidatesFinder termCandidatesFinder;

	private int count = 0;
	private Core core;
	private static final Logger logger = Logger
			.getLogger(WikipediaHarvesterOld.class.getName());

	private static final String templateRegExp = "TEMPLATE\\[";
	public static final int MIN_SECTIONS = 2;
	private MediaWikiParserFactory pf;
	private MediaWikiParser parser;
	private List<WikipediaPage> harvestedWikipediaPages = new ArrayList<WikipediaPage>();

	// -------------------------------------------------------------------------------------------------------------------

	public WikipediaHarvesterOld() {

	}

	// -------------------------------------------------------------------------------------------------------------------

	public void init(Core core) throws EpnoiInitializationException {
		this.termCandidatesFinder = new TermCandidatesFinder();
		this.core = core;
		this.termCandidatesFinder.init(core);
		pf = new MediaWikiParserFactory();
		parser = pf.createParser();
	}

	// -------------------------------------------------------------------------------------------------------------------

	public void harvest() {
		logger.info("Starting the harvesting ----------------------------------------------------------------------");

		File folder = new File(WikipediaHarvesterOld.wikipediaDumpPath);

		File[] listOfFiles = folder.listFiles();
		logger.info("Harvesting the directory/repository "
				+ folder.getAbsolutePath());

		for (int i = 0; i < listOfFiles.length; i++) {
			logger.info("Harvesting " + listOfFiles[i]);

			WikiXMLParser wxsp = WikiXMLParserFactory
					.getSAXParser(listOfFiles[i].getAbsolutePath());
			WikipediaPageHandler wikipediaPageHandler = new WikipediaPageHandler();
			try {

				wxsp.setPageCallback(wikipediaPageHandler);

				wxsp.parse();

			} catch (Exception e) {
				e.printStackTrace();

			} finally {
				wikipediaPageHandler = null;

			}
			count = 1;

			Iterator<WikipediaPage> harvestedWikipediaPagesIt = harvestedWikipediaPages
					.iterator();
			Context context = new Context();
			while (harvestedWikipediaPagesIt.hasNext()) {
				WikipediaPage wikipediaPage = harvestedWikipediaPagesIt.next();
				System.out.println(count++ + "Introducing  "
						+ wikipediaPage.getURI());
				try {

					_introduceWikipediaPage(wikipediaPage, context);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("This wikipedia page failed > "
							+ wikipediaPage.getURI());
				}
				harvestedWikipediaPagesIt.remove();
				context.clear();

				/*
				 * WikipediaPage resouce = (WikipediaPage) this.core
				 * .getInformationHandler().get(wikipediaPage.getURI(),
				 * RDFHelper.WIKIPEDIA_PAGE_CLASS);
				 * 
				 * System.out .println(
				 * "LO QUE SALE ============================================================="
				 * ); if(resouce.getSections().size()>3){
				 * System.out.println(wikipediaPage.getURI()+
				 * "----------------------------------------------------------------------"
				 * ); for(String section: resouce.getSections()){
				 * System.out.println("The section is "+ section);
				 * System.out.println
				 * ("---------------------------------------");
				 * System.out.println
				 * (resouce.getSectionsContent().get(section));
				 * System.out.println
				 * ("---------------------------------------");
				 * 
				 * } }
				 * 
				 * 
				 * System.out
				 * 
				 * 
				 * .println(
				 * "=========================================================================="
				 * );
				 * 
				 * Selector selector = new Selector();
				 * selector.setProperty(SelectorHelper.URI,
				 * wikipediaPage.getURI()); selector.setProperty(
				 * SelectorHelper.ANNOTATED_CONTENT_URI, wikipediaPage.getURI()
				 * + "/first/" +
				 * AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE);
				 * selector.setProperty(SelectorHelper.TYPE,
				 * RDFHelper.WIKIPEDIA_PAGE_CLASS);
				 */
				/*
				 * System.out.println("-----> " +
				 * core.getInformationHandler().getAnnotatedContent( selector));
				 */
				// if(count % 20==0){
				/*
				 * try { Thread.sleep(5000); } catch (InterruptedException e) {
				 * // TODO Auto-generated catch block e.printStackTrace(); }
				 */
				// }
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

	private String _createTermDefinition(WikipediaPage wikipediaPage,
			Context context) {
		// System.out.println("URI> " + wikipediaPage.getURI());
		/*
		 * LO DE ANTES Document annotatedFirstSection =
		 * retrieveAnnotatedDocument( wikipediaPage.getURI(),
		 * wikipediaPage.getURI() + "/first" +
		 * AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE);
		 */
		String annotatedFirstSectionSerialized = (String) context.getElements()
				.get(wikipediaPage.getURI() + "/first/"
						+ AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE);

		Document annotatedFirstSection = GateUtils
				.deserializeGATEDocument(annotatedFirstSectionSerialized);

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
			Factory.deleteResource(annotatedFirstSection);

			return firstSentence;

		} else {
			Factory.deleteResource(annotatedFirstSection);
			return "";
		}
	}

	// -------------------------------------------------------------------------------------------------------------------

	// -------------------------------------------------------------------------------------------------------------------

	public void _introduceAnnotatedContent(WikipediaPage wikipediaPage,
			Context context) {
		String serializedAnnotatedContent = null;
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
			Document annotatedContent = this.termCandidatesFinder
					.findTermCandidates(sectionContent);
			serializedAnnotatedContent = annotatedContent.toXml();

			// Once it has been serialized, we must free the associated GATE
			// resources
			Factory.deleteResource(annotatedContent);

			Selector selector = new Selector();
			selector.setProperty(SelectorHelper.URI, wikipediaPage.getURI());
			selector.setProperty(SelectorHelper.ANNOTATED_CONTENT_URI,
					annotatedContentURI);
			selector.setProperty(SelectorHelper.TYPE,
					RDFHelper.WIKIPEDIA_PAGE_CLASS);

			core.getInformationHandler().setAnnotatedContent(
					selector,
					new org.epnoi.model.Content<Object>(serializedAnnotatedContent,
							AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE));
		}

	}

	// -------------------------------------------------------------------------------------------------------------------

	private String _cleanTemplates(String parsedText) {
		/*
		 * System.out.println("------> " + StringUtils.outerMatching(parsedText,
		 * templateRegExp, '[', ']'));
		 */
		return StringUtils.outerMatching(parsedText, templateRegExp, '[', ']');

	}

	// -------------------------------------------------------------------------------------------------------------------

	private void _introduceWikipediaPage(WikipediaPage page, Context context) {

		String termDefinition = "";
		// String termDefinition = _createTermDefinition(page, context);
		// System.out.println("----> " + termDefinition);
		// page.setTermDefinition(termDefinition);

		/*
		 * System.out .println(
		 * "LO QUE ENTRA ============================================================="
		 * ); System.out.println(page); System.out .println(
		 * "=========================================================================="
		 * );
		 */
		if (!core.getInformationHandler().contains(page.getURI(),
				RDFHelper.WIKIPEDIA_PAGE_CLASS)) {
			long currenttime = System.currentTimeMillis();
			_introduceAnnotatedContent(page, context);
			core.getInformationHandler().put(page, context);
			long time = System.currentTimeMillis() - currenttime;
			System.out.println(page.getURI() + " took " + time);
		}

	}

	// -------------------------------------------------------------------------------------------------------------------
	/*
	 * private Document deserializeAnnotatedDocument( String
	 * serializedAnnotatedDocument) {
	 * 
	 * Document document = null; try { document = (Document) Factory
	 * .createResource( "gate.corpora.DocumentImpl", Utils.featureMap(
	 * gate.Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME,
	 * serializedAnnotatedDocument,
	 * gate.Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME, "text/xml"));
	 * 
	 * } catch (ResourceInstantiationException e) { System.out .println(
	 * "Couldn't retrieve the GATE document that represents the annotated content of "
	 * + serializedAnnotatedDocument); e.printStackTrace(); } return document; }
	 */
	// -------------------------------------------------------------------------------------------------------------------

	class WikipediaPageHandler implements PageCallbackHandler {

		// -------------------------------------------------------------------------------------------------------------------

		public WikipediaPageHandler() {

		}

		// -------------------------------------------------------------------------------------------------------------------

		public void processWikipediaPage(WikiPage page) {
			// System.out.println("> " + page.getTitle() + " " + count++);

			ParsedPage parsedPage = parser.parse(page.getWikiText());

			WikipediaPage wikipediaPage = new WikipediaPage();
			Context context = new Context();

			String cleanedPageTitle = page.getTitle().replaceAll("\\n", "")
					.replaceAll("\\s+$", "");

			String localPartOfTermURI = page.getTitle().replaceAll("\\n", "")
					.replaceAll("\\s+$", "").replaceAll("\\s+", "_");

			wikipediaPage.setURI(WikipediaHarvesterOld.wikipediaPath
					+ localPartOfTermURI);
			wikipediaPage.setTerm(cleanedPageTitle);

			for (Section section : parsedPage.getSections()) {

				String sectionName = (section.getTitle() == null) ? "first"
						: section.getTitle();
				wikipediaPage.addSection(sectionName);
				String sectionContent = "";

				String parsedText;
				String lineWithoutTemplates;
				for (Content content : section.getContentList()) {

					parsedText = content.getText();
					lineWithoutTemplates = _cleanTemplates(parsedText);

					if (!lineWithoutTemplates.equals(sectionName)) {

						/*
						 * System.out.println(); System.out.println();
						 * System.out .println(
						 * "-------------------------------------------------------------------------"
						 * ); System.out.println("SectionWith:" + parsedText);
						 * System.out.println("ContentWithout:" +
						 * lineWithoutTemplates); System.out.println();
						 * System.out.println();
						 */
						lineWithoutTemplates = lineWithoutTemplates.replaceAll(
								"\\s+", " ");

						sectionContent = sectionContent + lineWithoutTemplates;
					} else {
						// System.out.println("It was equal "+lineWithoutTemplates);
					}

					// sectionContent = sectionContent + parsedText;

				}

				// System.out.println("--("+sectionName+")="+sectionContent);

				wikipediaPage.addSectionContent(sectionName, sectionContent);

			}
			// System.out.println("----------------> " + wikipediaPage);
			// The first section is missing in some articles... we simply add it
			if (!wikipediaPage.getSections().contains("first")) {
				wikipediaPage.getSections().add("first");
				wikipediaPage.getSectionsContent().put("first", "");
			}
			if (wikipediaPage.getSections().size() > WikipediaHarvesterOld.MIN_SECTIONS) {
				if (WikipediaHarvesterOld.INCREMENTAL) {
					if (!core.getInformationHandler().contains(
							wikipediaPage.getURI(),
							RDFHelper.WIKIPEDIA_PAGE_CLASS)) {
						// System.out.println("--->" +
						// wikipediaPage.getSections());

						System.out.println("Introducing "
								+ wikipediaPage.getURI());
						try {
							_introduceWikipediaPage(wikipediaPage,
									Context.getEmptyContext());

						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("This wikipedia page failed > "
									+ wikipediaPage.getURI());
						}
					} else {
						System.out.println("The WikipediaPage "
								+ wikipediaPage.getURI()
								+ " was already stored");
					}
				} else {
					_introduceWikipediaPage(wikipediaPage,
							Context.getEmptyContext());
				}
			}
			// _introduceWikipediaPage(wikipediaPage, context);
			// harvestedWikipediaPages.add(wikipediaPage);
		}

	}

	// -------------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		WikipediaHarvesterOld wikipediaHarvester = new WikipediaHarvesterOld();
		// Core core = null;
		Core core = CoreUtility.getUIACore();

		try {
			wikipediaHarvester.init(core);
		} catch (EpnoiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wikipediaHarvester.harvest();
	}
}
