package org.epnoi.rest.clients;

import java.io.Reader;
import java.io.StringReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.epnoi.model.modules.Core;
import org.epnoi.uia.commons.GateUtils;
import org.epnoi.uia.core.CoreUtility;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import gate.Document;
import gate.corpora.DocumentImpl;
import gate.corpora.DocumentStaxUtils;

public class AnnotatedContentClient {

	public static void main(String[] args) {
		//Core core = CoreUtility.getUIACore();

		ClientConfig config = new DefaultClientConfig();
	
		Client client = Client.create(config);
		String knowledgeBasePath = "/uia/annotatedcontent";

		WebResource service = client
				.resource("http://localhost:8080/epnoi/rest");

		/*
		service.path(knowledgeBasePath).queryParam("source", "dog")
				.queryParam("target","animal")
				.type(javax.ws.rs.core.MediaType.APPLICATION_JSON).type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
				.get(Boolean.class);

		*/
		Document document = new DocumentImpl();
		String content=service.path(knowledgeBasePath).type(javax.ws.rs.core.MediaType.APPLICATION_XML).get(String.class);
	
		
		Reader reader = new StringReader(content);
		XMLInputFactory factory = XMLInputFactory.newInstance(); // Or newFactory()
		try {
			XMLStreamReader xmlReader = factory.createXMLStreamReader(reader);
			xmlReader.next();
			DocumentStaxUtils.readGateXmlDocument(xmlReader, document);
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("STRING CONTENT > "+ content);
		System.out.println("CONTENT > "+content);
	}

}