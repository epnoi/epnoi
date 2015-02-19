package org.epnoi.uia.harvester.filesystem;

import gate.Document;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Content;
import org.epnoi.model.ContentHelper;
import org.epnoi.model.Context;
import org.epnoi.model.Feed;
import org.epnoi.model.InformationSource;
import org.epnoi.model.Item;
import org.epnoi.model.Paper;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.exceptions.EpnoiInitializationException;
import org.epnoi.uia.harvester.rss.parse.RSSFeedParser;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParserFactory;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.rdf.InformationSourceRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.learner.nlp.TermCandidatesFinder;
import org.epnoi.uia.parameterization.manifest.Manifest;
import org.xml.sax.ContentHandler;

class FilesystemHarvester {
	private Core core;
	private Manifest manifest;
	private String directoryPath;
	private String datePattern = "MM/dd/yyyy";

	public static final String path = "/JUNK/drinventorcorpus/corpus";
	public static final String FIRST_REVIEW_CORPUS = "FirstReviewCorpus";

	private static final Logger logger = Logger
			.getLogger(FilesystemHarvester.class.getName());
	TermCandidatesFinder termCandidatesFinder;

	// ----------------------------------------------------------------------------------------

	public FilesystemHarvester() {

	}

	// ----------------------------------------------------------------------------------------

	// ----------------------------------------------------------------------------------------

	private String _scanContent(String resourceURI) {
		Metadata metadata = new Metadata();
		metadata.set(Metadata.RESOURCE_NAME_KEY, resourceURI);
		InputStream is = null;
		ContentHandler handler = null;
		try {
			is = new URL(resourceURI).openStream();

			Parser parser = new AutoDetectParser();
			handler = new BodyContentHandler(-1);

			ParseContext context = new ParseContext();
			context.set(Parser.class, parser);

			parser.parse(is, handler, metadata, new ParseContext());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return handler.toString();
	}

	// ----------------------------------------------------------------------------------------

	public void run() {
		logger.info("Starting a harversting task " + this.manifest);
		harvest(this.directoryPath);
	}

	// ----------------------------------------------------------------------------------------

	public void harvest(String directoryToHarvest) {
		HashMap<String, Item> items = new HashMap<String, Item>();
		try {
			File harvestDirectoy = new File(directoryToHarvest);

			String[] filesToHarvest = scanFilesToHarverst(harvestDirectoy);

			// System.out.println("..........> "
			// + Arrays.toString(filesToHarvest));
			for (String fileToHarvest : filesToHarvest) {
				System.out.println(">Harvesting :"
						+ harvestDirectoy.getAbsolutePath() + "/"
						+ fileToHarvest);
				Context context = new Context();
				Paper paper = _harvestFile(directoryToHarvest + "/"
						+ fileToHarvest);
				if (this.core != null) {
					core.getInformationHandler().put(paper,
							Context.getEmptyContext());
					core.getAnnotationHandler().label(paper.getURI(),
							FilesystemHarvester.FIRST_REVIEW_CORPUS);

					Document annotatedContent = this.termCandidatesFinder
							.findTermCandidates(paper.getDescription());
					// System.out.println("------)> "+annotatedContent.toXml());
					Selector annotationSelector = new Selector();
					annotationSelector.setProperty(SelectorHelper.URI,
							paper.getURI());
					annotationSelector
							.setProperty(
									SelectorHelper.ANNOTATED_CONTENT_URI,
									paper.getURI()
											+ "/"
											+ AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE);
					annotationSelector.setProperty(SelectorHelper.TYPE,
							RDFHelper.PAPER_CLASS);

					core.getInformationHandler().put(paper,
							Context.getEmptyContext());

					core.getInformationHandler().setAnnotatedContent(
							annotationSelector,
							new Content<>(annotatedContent.toXml(),
									ContentHelper.CONTENT_TYPE_TEXT_XML));

				} else {

					System.out.println("Result: Paper> " + paper);

					System.out.println("Result: Context> " + context);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ----------------------------------------------------------------------------------------

	private String[] scanFilesToHarverst(File directoryToHarvest) {
		String[] filesToHarvest = directoryToHarvest.list(new FilenameFilter() {

			public boolean accept(File current, String name) {
				File file = new File(current, name);
				return (file.isFile()) && (!file.isHidden());
			}

		});
		return filesToHarvest;
	}

	// ----------------------------------------------------------------------------------------

	protected String convertDateFormat(String dateExpression) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = dateFormat.parse(dateExpression);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		return (dt1.format(date) + "^^xsd:date");

	}

	// ----------------------------------------------------------------------------------------

	public Paper _harvestFile(String filePath) {

		Paper paper = new Paper();

		String fileContent = _scanContent("file://" + filePath);
		paper.setURI(filePath);
		paper.setTitle(filePath);
		paper.setDescription(fileContent);
		return paper;
	}

	// ----------------------------------------------------------------------------------------

	private String getDate(String filePath) {
		System.out.println("filePath> " + filePath);
		int bracketOpeningPosition = filePath.indexOf("[");
		int bracketClosingPosition = filePath.indexOf("]");
		String filePathDatePart = filePath.substring(
				bracketOpeningPosition + 1, bracketClosingPosition);
		return filePathDatePart;
	}

	// ----------------------------------------------------------------------------------------

	private void handleError(String errorMessage, String exceptionMessage) {
		if (exceptionMessage != null) {
			logger.severe(errorMessage);
		} else {
			logger.severe(errorMessage);
			logger.severe("The exception message was: " + errorMessage);
		}

	}

	// -------------------------------------------------------------------------------------------------------------------

	public void init(Core core) throws EpnoiInitializationException {

		this.core = core;
		this.termCandidatesFinder = new TermCandidatesFinder();
		this.termCandidatesFinder.init();

	}

	// -------------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		System.out.println("Starting the harvesting!");

		FilesystemHarvester harvester = new FilesystemHarvester();
		Core core = CoreUtility.getUIACore();
		try {
			harvester.init(core);
		} catch (EpnoiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		harvester.harvest(FilesystemHarvester.path);

		System.out.println("Ending the harvesting!");
	}

}