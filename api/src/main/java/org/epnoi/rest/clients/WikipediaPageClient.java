package org.epnoi.rest.clients;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.epnoi.model.WikipediaPage;

public class WikipediaPageClient {
	public static void main(String[] args) {

		
		
		String domainsPath = "/uia/resources/bytype/wikipediapages/resource";
		String wikipediaPageURI = "http://en.wikipedia.org/wiki/Autism";
		
		ClientConfig config = new DefaultClientConfig();
	//config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(config);

		WebResource service = client.resource("http://localhost:8080/epnoi/rest");

		// -----------------------------------------------------------------------------
		System.out.println("Then we retrieve it");
		WikipediaPage retrievedDomain = service.path(domainsPath).queryParam("uri", wikipediaPageURI)
				.type(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(WikipediaPage.class);

		System.out.println("The retrieved domain was: " + retrievedDomain);

		// ---------------
	}
}
