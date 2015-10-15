package org.epnoi.rest.clients;

import java.util.Set;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class KnowledgeBaseClient {

	public static void main(String[] args) {

		ClientConfig config = new DefaultClientConfig();
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(config);
		String knowledgeBasePath = "/uia/knowledgebase";

		WebResource service = client.resource("http://localhost:8080/epnoi/rest");

		Boolean areRelated = service.path(knowledgeBasePath+"/relations/hypernymy").queryParam("source", "cat").queryParam("target", "feline")
				.type(javax.ws.rs.core.MediaType.APPLICATION_JSON).type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
				.get(Boolean.class);
		System.out.println("Are related? " + areRelated);

		System.out.println("Checking the stemmer");
		System.out.println(service.path(knowledgeBasePath + "/stem").queryParam("term", "catty").get(Set.class));
	}

}