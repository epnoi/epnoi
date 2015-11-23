package org.epnoi.rest.clients;



import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Map;

public class KnowledgeBaseClient {

	public static void main(String[] args) {
		ClientConfig config = new ClientConfig();

		Client client = ClientBuilder.newClient(config);
		URI testServiceURI = UriBuilder.fromUri("http://localhost:8080/epnoi/rest").build();
		WebTarget service = client.target(testServiceURI);



		String knowledgeBasePath = "/uia/knowledgebase";



		Boolean areRelated = service.path(knowledgeBasePath+"/relations/hypernymy").queryParam("source", "cat").queryParam("target", "feline").request()
				.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON)
				.get(Boolean.class);
		System.out.println("Are related? " + areRelated);

		System.out.println("Checking the stemmer");
		System.out.println(service.path(knowledgeBasePath + "/stem").queryParam("term", "cats").request().get(Map.class));
		System.out.println("Testing the hypernyms!");
		System.out.println(service.path(knowledgeBasePath+"/relations/hypernymy/targets").queryParam("source", "cat").request()
				.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON)
				.get(Map.class));
	}

}