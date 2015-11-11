package org.epnoi.model.commons;

import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringReader;



public class XMLUtils{

public static org.w3c.dom.Document writeToFile(String xmlContent,
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