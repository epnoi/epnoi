package org.epnoi.rest.clients;

import org.epnoi.model.WikipediaPage;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class WikipediaPageClient {
	public static void main(String[] args) {

		
		
		String domainsPath = "/uia/resources/bytype/wikipediapages/resource";
		String wikipediaPageURI = "http://en.wikipedia.org/wiki/Autism";
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		URI testServiceURI = UriBuilder.fromUri("http://localhost:8080/epnoi/rest").build();
		WebTarget service = client.target(testServiceURI);



		// -----------------------------------------------------------------------------
		System.out.println("Then we retrieve it");
		WikipediaPage retrievedDomain = service.path(domainsPath).queryParam("uri", wikipediaPageURI).request()
				.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(WikipediaPage.class);

		System.out.println("The retrieved domain was: " + retrievedDomain);

		// ---------------
	}
}
