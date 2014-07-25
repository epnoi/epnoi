package org.epnoi.uia.rest.clients;

import org.epnoi.uia.search.SearchResult;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class SearchServiceClient {

	public static void main(String[] args) {
		try {

			ClientConfig config = new DefaultClientConfig();
			config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
					Boolean.TRUE);
			Client client = Client.create(config);

			WebResource service = client
					.resource("http://localhost:8081/epnoiUIA/rest");

			System.out.println("Search ");
			Object response = service.path("/uia/searchs")
					.queryParam("facet", "date")
					.queryParam("query", "content:Scaling")
					.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
					.get(SearchResult.class);
			System.out.println("The response was: " + response);

		} catch (Exception e) {

			e.printStackTrace();

		}

	}
}
