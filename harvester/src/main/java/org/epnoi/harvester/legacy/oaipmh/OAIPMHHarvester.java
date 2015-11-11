package org.epnoi.harvester.legacy.oaipmh;

import gate.Document;
import org.apache.commons.io.FileUtils;
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class OAIPMHHarvester extends CommandLineTool {
	public static final String PARAMETER_COMMAND = "-command";
	public static final String PARAMETER_NAME = "-name";
	public static final String PARAMETER_URL = "-URL";
	public static final String PARAMETER_OUT = "-out";
	public static final String PARAMETER_IN = "-in";
	public static final String PARAMETER_FROM = "-from";
	public static final String PARAMETER_TO = "-to";
	public static final String PARAMETER_COMMAND_INIT = "init";
	public static final String PARAMETER_COMMAND_HARVEST = "harvest";
	private static final Logger logger = Logger.getLogger(OAIPMHHarvester.class
			.getName());

	private static final String LAST_HARVESTED_FILENAME = "lastHarvested";
	private static String lastHarvested = null;;
	private final int PROGRESS_BEFORE_UPDATE = 20;
	private static File progressFile;

	DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
	//Core core = null;

	 Core core = CoreUtility.getUIACore();
	

	/*
	 * OAIPMHIndexer -in where-oaipmh-harvest-dir -repository name
	 * 
	 * -name arxiv -in /JUNK (/JUNK/OAIPMH/harvests/arxiv/harvest should exist )
	 * 
	 * 
	 * 
	 * 
	 */
	 
	 public OAIPMHHarvester(){
		
		 
		 org.w3c.dom.Node testNode = null;
		 
		 
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

		OAIPMHHarvester harvester = new OAIPMHHarvester();
		int numIndexed = 0;

		File folder = new File(harvestDir);

		try {
			progressFile = new File(folder.getAbsolutePath() + "/"
					+ OAIPMHHarvester.LAST_HARVESTED_FILENAME);

			List<String> lines = FileUtils.readLines(progressFile);
			System.out.println("lo leido----> " + lines.get(0));
			lastHarvested = lines.get(0);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		if(lastHarvested!=null && lastHarvested.length()==0){
			lastHarvested=null;
		}
			
		File[] listOfFiles = folder.listFiles();
		System.out.println("Harvesting the directory/repository "
				+ folder.getAbsolutePath());

		// _updateLastHarvested((new Date()).toString(), pro);

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String filePath = "file://" + listOfFiles[i].getAbsolutePath();
				System.out.println("Found the file: " + filePath);
				if (filePath.endsWith(".xml")) {
					harvester.harvestFile(filePath, progressFile);

				}
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Directory " + listOfFiles[i].getName());
			}
		}

		long end = new Date().getTime();

		System.out.println("Indexing " + numIndexed + " files took "
				+ (end - start) + " milliseconds");
	}

	// --------------------------------------------------------------------------------------------------------------------------

	public void harvestFile(String filepath, File progressFile)
			throws Exception {

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

		System.out.println("---> " + nodes.getLength());
		int progressCounter = 0;
		boolean lastHarvestedFound = false || (lastHarvested == null);
		System.out.println("---lh" +lastHarvested);
		for (int i = 0; i < nodes.getLength(); i++) {

			Element recordElement = (Element) nodes.item(i);
			

			if (!lastHarvestedFound) {
				String identifier = showIdentifier(recordElement);
				System.out.println("----> paso de este->"+identifier);
				lastHarvestedFound = identifier.equals(lastHarvested);
			} else {

				System.out.println("Mete aqui el " + showIdentifier(recordElement));
				
				Paper paper = _harvestRecord(recordElement, simpleDateFormat);

				// System.out.println("Harvesting paper ------------------->" +
				// paper);

				/*EN ESTE CONTEXTO DEBERIAS METER YA EL CONTENIDO ANOTAD*/ 
				
				core.getInformationHandler().put(paper, Context.getEmptyContext());
				 
				Selector selector = new Selector();
				selector.setProperty(SelectorHelper.URI, paper.getUri());
				selector.setProperty(SelectorHelper.TYPE, RDFHelper.PAPER_CLASS);
								 
				Content<String> content=core.getInformationHandler().getContent(selector);

				Document annotatedContent=this.core.getNLPHandler().process(content.getContent());
				System.out.println("------)> "+annotatedContent.toXml());
											

				Selector annotationSelector = new Selector();
				annotationSelector.setProperty(SelectorHelper.URI, paper.getUri());
				annotationSelector.setProperty(SelectorHelper.ANNOTATED_CONTENT_URI, paper.getUri()+"/"+AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE);
				annotationSelector.setProperty(SelectorHelper.TYPE, RDFHelper.PAPER_CLASS);
				
			
				core.getInformationHandler().setAnnotatedContent(annotationSelector, new Content<Object>(annotatedContent.toXml(), ContentHelper.CONTENT_TYPE_TEXT_XML));
				
				//System.out.println("-----|>"+core.getInformationAccess().getAnnotatedContent(paper.getURI(), RDFHelper.PAPER_CLASS));

				
				
				
				progressCounter++;
				if (progressCounter == PROGRESS_BEFORE_UPDATE) {

					
					
					String identifier = showIdentifier(recordElement);
					

					System.out
							.println("updating the identifier> " + identifier);
					_updateLastHarvested(identifier, progressFile);
					progressCounter = 0;
				}

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
			String identifier = ((org.w3c.dom.Node)identifierNodeList.item(0)).getTextContent();

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
		if ((setSpecNodeList != null) && (setSpecNodeList.item(0) != null)) {
			String setSpec = setSpecNodeList.item(0).getTextContent();
			core.getAnnotationHandler().label(paper.getURI(), setSpec);
		}
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

	private static void _updateLastHarvested(String id, File progressFile) {

		try {
			FileWriter fileWriter = new FileWriter(progressFile);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(id);
			bufferedWriter.newLine();
			bufferedWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}