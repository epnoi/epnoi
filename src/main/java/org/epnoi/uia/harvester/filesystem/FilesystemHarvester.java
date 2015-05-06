package org.epnoi.uia.harvester.filesystem;

import gate.Document;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.Item;
import org.epnoi.model.Paper;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.learner.nlp.TermCandidatesFinder;
import org.xml.sax.ContentHandler;

class FilesystemHarvester {
	private Core core;
	private String datePattern = "MM/dd/yyyy";
	private FilesystemHarvesterParameters parameters;

	public String path = "/JUNK/drinventorcorpus/corpus";
	private boolean verbose;
	private String corpusLabel;

	private static final Logger logger = Logger
			.getLogger(FilesystemHarvester.class.getName());
	TermCandidatesFinder termCandidatesFinder;

	// ----------------------------------------------------------------------------------------

	public FilesystemHarvester() {

	}

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

		String content = handler.toString();
		content = content.replaceAll("\\r\\n|\\r|\\n", " ");
		content = content.replaceAll("\\s+", " ");
		// System.out.println("----> " + content);
		return content;
	}

	// ----------------------------------------------------------------------------------------

	public void run() {

		harvest(this.path);
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
				logger.info("Harvesting the file "
						+ harvestDirectoy.getAbsolutePath() + "/"
						+ fileToHarvest);
				Context context = new Context();
				Paper paper = _harvestFile(directoryToHarvest + "/"
						+ fileToHarvest);
				if (this.core != null) {
					// First the paper is added to the UIA
					core.getInformationHandler().put(paper,
							Context.getEmptyContext());

					// Later it is annotated as belonging to the harvested
					// corpus
					long startTme = System.currentTimeMillis();
					core.getAnnotationHandler().label(paper.getURI(),
							this.corpusLabel);

					long totalTime = Math.abs(startTme
							- System.currentTimeMillis());
					logger.info("It took " + totalTime
							+ " ms to add it to the UIA and label it");
					// The annotated version of the paper is also stored in the
					// UIA

					startTme = System.currentTimeMillis();
					Document annotatedContent = this.termCandidatesFinder
							.findTermCandidates(paper.getDescription());

					Selector annotationSelector = new Selector();
					annotationSelector.setProperty(SelectorHelper.URI,
							paper.getURI());
					annotationSelector
							.setProperty(
									SelectorHelper.ANNOTATED_CONTENT_URI,
									paper.getURI()
											+ "/"
											+ AnnotatedContentHelper.CONTENT_TYPE_OBJECT_XML_GATE);
					annotationSelector.setProperty(SelectorHelper.TYPE,
							RDFHelper.PAPER_CLASS);

					core.getInformationHandler().setAnnotatedContent(
							annotationSelector,
							new Content<Object>(annotatedContent,
									AnnotatedContentHelper.CONTENT_TYPE_OBJECT_XML_GATE));
					
					totalTime = Math.abs(startTme
							- System.currentTimeMillis());
					logger.info("It took "
							+ totalTime
							+ "ms to add it to annotate its content and add it to the UIA");

				} else {

					logger.info("Paper: " + paper);

					logger.info("Result: " + context);
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
		paper.setURI("file://" + filePath);
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

	public void init(Core core, FilesystemHarvesterParameters parameters)
			throws EpnoiInitializationException {

		this.core = core;
		this.termCandidatesFinder = new TermCandidatesFinder();
		this.termCandidatesFinder.init();

		this.path = (String) parameters
				.getParameterValue(FilesystemHarvesterParameters.FILEPATH_PARAMETER);

		this.verbose = (boolean) parameters
				.getParameterValue(FilesystemHarvesterParameters.VERBOSE_PARAMETER);

		this.corpusLabel = (String) parameters
				.getParameterValue(FilesystemHarvesterParameters.CORPUS_LABEL_PARAMETER);

	}

	// -------------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		logger.info("Starting the harvesting!");

		FilesystemHarvester harvester = new FilesystemHarvester();
		FilesystemHarvesterParameters parameters = new FilesystemHarvesterParameters();

		parameters.setParameter(
				FilesystemHarvesterParameters.CORPUS_LABEL_PARAMETER,
				"CGTestCorpus");
		parameters.setParameter(
				FilesystemHarvesterParameters.VERBOSE_PARAMETER, true);

		parameters.setParameter(
				FilesystemHarvesterParameters.OVERWRITE_PARAMETER, true);

		
		
		parameters.setParameter(
				FilesystemHarvesterParameters.FILEPATH_PARAMETER,
				"/epnoi/epnoideployment/firstReviewResources/CGCorpus");

		Core core = CoreUtility.getUIACore();
		try {
			harvester.init(core, parameters);
		} catch (EpnoiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		harvester.run();

		logger.info("Ending the harvesting!");
	}

}