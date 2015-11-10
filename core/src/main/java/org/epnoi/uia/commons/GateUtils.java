package org.epnoi.uia.commons;

import gate.Annotation;
import gate.Document;
import gate.DocumentContent;
import gate.corpora.DocumentImpl;
import gate.corpora.DocumentStaxUtils;
import gate.util.InvalidOffsetException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.Reader;
import java.io.StringReader;

public class GateUtils {

	// ----------------------------------------------------------------------------------------------

	/**
	 * 
	 * @param serializedGATEDocument
	 * @return
	 */
	/*
	 * public static Document deserializeGATEDocument(String
	 * serializedGATEDocument) { Document document = null; try { document =
	 * (Document) Factory .createResource( "gate.corpora.DocumentImpl",
	 * Utils.featureMap( gate.Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME,
	 * serializedGATEDocument, gate.Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME,
	 * "text/xml"));
	 * 
	 * } catch (ResourceInstantiationException e) { // TODO Auto-generated
	 * System.out .println("Couldn't retrieve de unserialized GATE document");
	 * e.printStackTrace(); } return document; }
	 */
	public static Document deserializeGATEDocument(String serializedGATEDocument) {
		Document document = new DocumentImpl();
		Reader reader = new StringReader(serializedGATEDocument);
		XMLInputFactory factory = XMLInputFactory.newInstance(); // Or
																	// newFactory()
		try {
			XMLStreamReader xmlReader = factory.createXMLStreamReader(reader);
			xmlReader.next();
			DocumentStaxUtils.readGateXmlDocument(xmlReader, document);
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return document;
	}

	// ----------------------------------------------------------------------------------------------

	/**
	 * Method that obtains the content covered by an annotation in a given
	 * document
	 * 
	 * @param annotation
	 * @param document
	 * @return Returns the content covered by the annotation, or null if the
	 */
	public static DocumentContent extractAnnotationContent(Annotation annotation, Document document) {
		try {
			Long sentenceStartOffset = annotation.getStartNode().getOffset();
			Long sentenceEndOffset = annotation.getEndNode().getOffset();

			return document.getContent().getContent(sentenceStartOffset, sentenceEndOffset);
		} catch (InvalidOffsetException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
}
