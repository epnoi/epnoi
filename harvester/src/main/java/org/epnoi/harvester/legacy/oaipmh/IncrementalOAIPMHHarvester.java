package org.epnoi.harvester.legacy.oaipmh;

import gate.Document;
import org.epnoi.model.*;
import org.epnoi.model.commons.CommandLineTool;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class IncrementalOAIPMHHarvester extends CommandLineTool {
	public static final String PARAMETER_COMMAND = "-command";
	public static final String PARAMETER_NAME = "-name";
	public static final String PARAMETER_URL = "-URL";
	public static final String PARAMETER_OUT = "-out";
	public static final String PARAMETER_IN = "-in";
	public static final String PARAMETER_FROM = "-from";
	public static final String PARAMETER_TO = "-to";
	public static final String PARAMETER_COMMAND_INIT = "init";
	public static final String PARAMETER_COMMAND_HARVEST = "harvest";
	private static final Logger logger = Logger
			.getLogger(IncrementalOAIPMHHarvester.class.getName());

	DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
	// Core core = null;

	Core core = CoreUtility.getUIACore();

	/*
	 * OAIPMHIndexer -in where-oaipmh-harvest-dir -repository name
	 * 
	 * -name arxiv -in /JUNK (/JUNK/OAIPMH/harvests/arxiv/harvest should exist )
	 */

	public IncrementalOAIPMHHarvester() {
	}

	public static void main(String[] args) throws Exception {

		HashMap<String, String> options = getOptions(args);

		String in = (String) options.get(PARAMETER_IN);

		String name = (String) options.get(PARAMETER_NAME);

		String harvestDir = in + "/OAIPMH/harvests/" + name + "/harvest";

		System.out
				.println("Updating the repository harvest with the following paraneters: -in "
						+ in);

		logger.info("Updating the repository harvest with the following paraneters: -in "
				+ in);

		long start = new Date().getTime();

		IncrementalOAIPMHHarvester harvester = new IncrementalOAIPMHHarvester();
		int numIndexed = 0;

		File folder = new File(harvestDir);

		File[] listOfFiles = folder.listFiles();
		logger.info("Harvesting the directory/repository "
				+ folder.getAbsolutePath());

		// _updateLastHarvested((new Date()).toString(), pro);

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String filePath = "file://" + listOfFiles[i].getAbsolutePath();
				logger.info("Found the file: " + filePath);
				if (filePath.endsWith(".xml")) {
					harvester.harvestFile(filePath);

				}
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Directory " + listOfFiles[i].getName());
			}
		}

		long end = new Date().getTime();

		logger.info("Indexing " + numIndexed + " files took " + (end - start)
				+ " milliseconds");
	}

	// --------------------------------------------------------------------------------------------------------------------------

	public void harvestFile(String filepath) throws Exception {

		System.out.println("Indexing the file " + filepath);
		logger.info("Indexing the file " + filepath);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

		domFactory.setNamespaceAware(false);
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		org.w3c.dom.Document harvestDocument = builder.parse(filepath);

		XPath xpath = XPathFactory.newInstance().newXPath();
		// XPath Query for showing all nodes value
		XPathExpression expr = xpath.compile("//record");

		Object result = expr.evaluate(harvestDocument, XPathConstants.NODESET);

		NodeList nodes = (NodeList) result;

		for (int i = 0; i < nodes.getLength(); i++) {

			Element recordElement = (Element) nodes.item(i);

			String identifier = showIdentifier(recordElement);

			if (!core.getInformationHandler().contains(identifier,
					RDFHelper.PAPER_CLASS)) {

				Paper paper = _harvestRecord(recordElement, simpleDateFormat);

				core.getInformationHandler().put(paper,
						Context.getEmptyContext());

				Selector selector = new Selector();
				selector.setProperty(SelectorHelper.URI, paper.getUri());
				selector.setProperty(SelectorHelper.TYPE, RDFHelper.PAPER_CLASS);
				Content<String> content = core.getInformationHandler()
						.getContent(selector);
				// System.out.println("The content is >" + content);
				Document annotatedContent = this.core.getNLPHandler()
						.process(content.getContent());
				// System.out.println("------)> " + annotatedContent.toXml());

				Selector annotationSelector = new Selector();
				annotationSelector.setProperty(SelectorHelper.URI,
						paper.getUri());
				annotationSelector
						.setProperty(
								SelectorHelper.ANNOTATED_CONTENT_URI,
								paper.getUri()
										+ "/"
										+ AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE);
				annotationSelector.setProperty(SelectorHelper.TYPE,
						RDFHelper.PAPER_CLASS);

				core.getInformationHandler().setAnnotatedContent(
						annotationSelector,
						new Content<Object>(annotatedContent.toXml(),
								ContentHelper.CONTENT_TYPE_TEXT_XML));

				// System.out.println("-----|>"+core.getInformationHandler().getAnnotatedContent(paper.getURI()));

				System.out.println("updating the identifier> " + identifier);
			} else {
				System.out.println(identifier
						+ " was already stored in the UIA");

				/*
				 * System.out.println("C-----|>"+core.getInformationHandler().
				 * getContent(identifier));
				 * System.out.println("AC-----|>"+core.getInformationHandler
				 * ().getAnnotatedContent(identifier));
				 */
			}
		}
	}

	// --------------------------------------------------------------------------------------------------------------------------

	public String showIdentifier(Element recordElement) {

		String id = null;
		Element headerElement = (Element) recordElement.getElementsByTagName(
				"header").item(0);

		// Identifier
		NodeList identifierNodeList = headerElement
				.getElementsByTagName("identifier");

		if ((identifierNodeList != null)
				&& (identifierNodeList.item(0) != null)) {
			id = identifierNodeList.item(0).getTextContent();

			// newDocument.setField("id", identifier);

		}

		return id;

	}

	// --------------------------------------------------------------------------------------------------------------------------

	private Paper _harvestRecord(Element recordElement,
			SimpleDateFormat simpleDateFormat) {

		Paper paper = new Paper();

		Element headerElement = (Element) recordElement.getElementsByTagName(
				"header").item(0);

		// Identifier
		NodeList identifierNodeList = headerElement
				.getElementsByTagName("identifier");

		if ((identifierNodeList != null)
				&& (identifierNodeList.item(0) != null)) {
			String identifier = ((org.w3c.dom.Node) identifierNodeList.item(0))
					.getTextContent();

			// newDocument.setField("id", identifier);
			paper.setUri(identifier);
		}
		// setSpec
		NodeList setSpecNodeList = recordElement
				.getElementsByTagName("setSpec");

		for (int j = 0; j < setSpecNodeList.getLength(); j++) {
			String setSpec = setSpecNodeList.item(j).getTextContent();
			core.getAnnotationHandler().label(paper.getUri(), setSpec);
		}
		/*
		 * if ((setSpecNodeList != null) && (setSpecNodeList.item(0) != null)) {
		 * String setSpec = setSpecNodeList.item(0).getTextContent();
		 * core.getAnnotationHandler().label(paper.getURI(), setSpec); }
		 */

		NodeList newnodes = recordElement.getElementsByTagName("dc:title");

		for (int j = 0; j < newnodes.getLength(); j++) {

			paper.setTitle(newnodes.item(j).getTextContent());

		}

		newnodes = recordElement.getElementsByTagName("dc:identifier");

		for (int j = 0; j < newnodes.getLength(); j++) {
			String identifier = newnodes.item(j).getTextContent();

			if (identifier.startsWith("http://")) {
				// System.out.println("Este es el URL " + identifier);

			}
		}

		newnodes = recordElement.getElementsByTagName("dc:creator");

		for (int j = 0; j < newnodes.getLength(); j++) {
			// System.out.println("-" + newnodes.item(j).getTextContent());
			paper.getAuthors().add(newnodes.item(j).getTextContent());

		}

		newnodes = recordElement.getElementsByTagName("dc:subject");

		for (int j = 0; j < newnodes.getLength(); j++) {

			// newDocument.addField("subject",
			// newnodes.item(j).getTextContent());

			String subject = newnodes.item(j).getTextContent();
			core.getAnnotationHandler().label(paper.getUri(), subject);
		}

		newnodes = recordElement.getElementsByTagName("dc:date");

		ArrayList<Date> dates = new ArrayList<Date>();
		for (int j = 0; j < newnodes.getLength(); j++) {

			try {

				SimpleDateFormat formatoDeFecha = new SimpleDateFormat(
						"yyyy-MM-dd");
				Date date = formatoDeFecha.parse(newnodes.item(j)
						.getTextContent());
				dates.add(date);

			} catch (DOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if (dates.size() > 1) {
			Date date = _getEarliestDate(dates);
			// System.out.println("The earliest date is " + date);
			String indexFormatDate = simpleDateFormat.format(date);

			paper.setPubDate(indexFormatDate);
		} else if (dates.size() == 1) {
			String indexFormatDate = simpleDateFormat.format(dates.get(0));
			paper.setPubDate(indexFormatDate);
		}

		newnodes = recordElement.getElementsByTagName("dc:description");
		for (int j = 0; j < newnodes.getLength(); j++) {
			paper.setDescription(newnodes.item(j).getTextContent());
		}

		newnodes = recordElement.getElementsByTagName("dc:type");

		for (int j = 0; j < newnodes.getLength(); j++) {
			String type = newnodes.item(j).getTextContent();

		}

		return paper;

	}

	// -----------------------------------------------------------------------------------------------------------------

	private Date _getEarliestDate(List<Date> dates) {
		Date earliestDate = dates.get(0);
		for (int i = 1; i < dates.size(); i++) {
			if (earliestDate.compareTo(dates.get(i)) > 0) {
				earliestDate = dates.get(i);
			}
		}
		return earliestDate;
	}

	// -----------------------------------------------------------------------------------------------------------------

}