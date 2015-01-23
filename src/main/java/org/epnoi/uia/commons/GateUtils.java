package org.epnoi.uia.commons;

import gate.Document;
import gate.Factory;
import gate.Utils;
import gate.creole.ResourceInstantiationException;

public class GateUtils {
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
}
