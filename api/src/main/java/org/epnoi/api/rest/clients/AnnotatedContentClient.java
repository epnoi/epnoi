package org.epnoi.api.rest.clients;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import gate.Document;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.commons.GateUtils;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class AnnotatedContentClient {

	public static void main(String[] args) {
		// Core core = CoreUtility.getUIACore();

		String uri = "http://en.wikipedia.org/wiki/Autism/first/object/gate";
		
		Document document = _retrieveAnnotatedDocument(uri);
		System.out.println("> "+ document);
	}

	private static Document _retrieveAnnotatedDocument(String uri) {
		ClientConfig config = new DefaultClientConfig();

		Client client = Client.create(config);
		URI testServiceURI = UriBuilder.fromUri("http://localhost:8080/epnoi/rest").build();
		WebResource service = client.resource(testServiceURI);


		String knowledgeBasePath = "/uia/annotatedcontent";



			//		  http://en.wikipedia.org/wiki/Autism/first/object/gate
		
		String content = service.path(knowledgeBasePath).queryParam("uri", uri)
				.queryParam("type", RDFHelper.WIKIPEDIA_PAGE_CLASS).type(javax.ws.rs.core.MediaType.APPLICATION_XML)
				.get(String.class);

	
		
		Document document = GateUtils.deserializeGATEDocument(content);
		return document;
	}

}