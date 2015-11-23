package org.epnoi.rest.clients;


import gate.Document;
import org.epnoi.uia.commons.GateUtils;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class NLPClient {

	public static void main(String[] args) {
		// Core core = CoreUtility.getUIACore();

		String uri = "http://en.wikipedia.org/wiki/Autism/first/object/gate";

		Document document = _retrieveAnnotatedDocument(uri);
		System.out.println("> " + document);
	}

	private static Document _retrieveAnnotatedDocument(String uri) {

		ClientConfig config = new ClientConfig();

		Client client = ClientBuilder.newClient(config);
		URI testServiceURI = UriBuilder.fromUri("http://localhost:8080/epnoi/rest").build();
		WebTarget service = client.target(testServiceURI);





		String basePath = "/uia/nlp/process";


		// http://en.wikipedia.org/wiki/Autism/first/object/gate

		String content = service.path(basePath).queryParam("content", "My taylor is rich and my mother is in the kitchen")
				.request().accept(javax.ws.rs.core.MediaType.APPLICATION_XML).get(String.class);

		Document document = GateUtils.deserializeGATEDocument(content);
		return document;
	}

}