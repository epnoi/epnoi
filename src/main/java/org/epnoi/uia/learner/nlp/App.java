package org.epnoi.uia.learner.nlp;

import gate.Annotation;
import gate.Corpus;
import gate.Document;
import gate.creole.ExecutionException;
import gate.creole.SerialAnalyserController;
import gate.util.InvalidOffsetException;

import java.io.File;
import java.io.StringReader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.InputSource;

/**
 * Hello world!
 * 
 */
public class App {
	public void init() {
		URL u = getClass().getProtectionDomain().getCodeSource().getLocation();
		System.out.println("u> " + u);
		ControllerCreator controllerCreator = new ControllerCreator();
		// MainFrame.getInstance().setVisible(true);
		SerialAnalyserController controller = controllerCreator
				.createController();
		CorpusCreator corpusCreator = new CorpusCreator();

		String gateHomePath = App.class.getResource("").getPath()+"/gate";
		String documentsPath = App.class.getResource("").getPath()+"/documents";
		String resultsPath = gateHomePath + "/results";

		Corpus corpus = corpusCreator.create(documentsPath);

		for (int i = 0; i < corpus.getDocumentNames().size(); i++) {
			Document document = corpus.get(i);
			System.out.println("document------>  " + document);
			System.out.println("documentName-------> "
					+ corpus.getDocumentName(i));

		}

		System.out
				.println("________________________________________________________________________________________________________________________________________");
		System.out.println("																					");
		System.out.println("																					");
		System.out.println("																					");
		System.out.println("																					");
		System.out.println("																					");
		System.out
				.println("________________________________________________________________________________________________________________________________________");
		controller.setCorpus(corpus);
		try {
			controller.execute();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < corpus.getDocumentNames().size(); i++) {
			Document document = corpus.get(i);
		//	System.out.println("-------->! "+document.toString());
			writeToFile(document.toXml(),
					resultsPath + "/annotated" + corpus.getDocumentName(i)+".xml");
			
			
			for (Annotation annotation :document.getAnnotations().get("TermCandidate")){
				System.out.println("The rule :>"+annotation.getFeatures().get("rule"));
				annotation.getStartNode();
				try {
					System.out.println(document.getContent().getContent(annotation.getStartNode().getOffset(), annotation.getEndNode().getOffset()));
				} catch (InvalidOffsetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	public static void main(String[] args) {

		System.out
				.println("Starting the GATE TEST!!!!!!!================================================================");
		String applicationPath = System.getProperty("user.dir");
		System.out.println("-------> " + applicationPath);

		App app = new App();
		app.init();

		System.out
				.println("The GATE TEST is over!!!!!!!=================================================================");
	}

	private static org.w3c.dom.Document writeToFile(String xmlContent,
			String path) {
		System.out.println("This is the path " + path);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			org.w3c.dom.Document doc = builder.parse(new InputSource(
					new StringReader(xmlContent)));

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(path));

			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
