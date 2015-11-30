package org.epnoi.api.rest.clients;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.epnoi.model.WikipediaPage;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class WikipediaPageClient {
	public static void main(String[] args) {

		
		
		String domainsPath = "/uia/resources/bytype/wikipediapages/resource";
		String wikipediaPageURI = "http://en.wikipedia.org/wiki/Autism";
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		URI testServiceURI = UriBuilder.fromUri("http://localhost:8080/epnoi/rest").build();
		WebResource service = client.resource(testServiceURI);



		// -----------------------------------------------------------------------------
		System.out.println("Then we retrieve it");
		WikipediaPage retrievedDomain = service.path(domainsPath).queryParam("uri", wikipediaPageURI)
				.type(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(WikipediaPage.class);

		System.out.println("The retrieved domain was: " + retrievedDomain);

		// ---------------
	}
}
