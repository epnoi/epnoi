package org.epnoi.uia.commons;

import gate.Annotation;
import gate.Document;
import gate.DocumentContent;
import gate.Factory;
import gate.Utils;
import gate.creole.ResourceInstantiationException;
import gate.util.InvalidOffsetException;

public class GateUtils {
	
	//----------------------------------------------------------------------------------------------
	
	/**
	 * 
	 * @param serializedGATEDocument
	 * @return
	 */
	public static Document deserializeGATEDocument(String serializedGATEDocument) {
		Document document = null;
		try {
			document = (Document) Factory
					.createResource(
							"gate.corpora.DocumentImpl",
							Utils.featureMap(
									gate.Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME,
									serializedGATEDocument,
									gate.Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME,
									"text/xml"));

		} catch (ResourceInstantiationException e) { // TODO Auto-generated
			System.out
					.println("Couldn't retrieve de unserialized GATE document");
			e.printStackTrace();
		}
		return document;
	}

	//----------------------------------------------------------------------------------------------
	
	/**
	 * Method that obtains the content covered by an annotation in a given document
	 * @param annotation
	 * @param document
	 * @return Returns the content covered by the annotation, or null if the 
	 */
	public static DocumentContent extractAnnotationContent(Annotation annotation, Document document){
		try {
			Long sentenceStartOffset = annotation.getStartNode()
					.getOffset();
			Long sentenceEndOffset = annotation.getEndNode()
					.getOffset();

			return document.getContent().getContent(
					sentenceStartOffset, sentenceEndOffset);
		} catch (InvalidOffsetException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
}
	
