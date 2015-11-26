package org.epnoi.api.rest.clients;



import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Map;

public class KnowledgeBaseClient {

	public static void main(String[] args) {
		ClientConfig config = new DefaultClientConfig();

		Client client = Client.create(config);
		URI testServiceURI = UriBuilder.fromUri("http://localhost:8080/epnoi/rest").build();
		WebResource service = client.resource(testServiceURI);



		String knowledgeBasePath = "/uia/knowledgebase";



		Boolean areRelated = service.path(knowledgeBasePath+"/relations/hypernymy").queryParam("source", "cat").queryParam("target", "feline")
				.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
				.get(Boolean.class);
		System.out.println("Are related? " + areRelated);

		System.out.println("Checking the stemmer");
		System.out.println(service.path(knowledgeBasePath + "/stem").queryParam("term", "cats").get(Map.class));
		System.out.println("Testing the hypernyms!");
		System.out.println(service.path(knowledgeBasePath+"/relations/hypernymy/targets").queryParam("source", "cat")
				.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON)
				.get(Map.class));
	}

}