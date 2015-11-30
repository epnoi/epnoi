package org.epnoi.api.rest.clients;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.epnoi.model.Domain;
import org.epnoi.model.ResearchObject;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class DomainResourceClient {

	public static void main(String[] args) {

		String domainURI = "http://www.epnoi.org/testDomain";
		String domainType = "paper";
		String domainsPath = "/uia/domains/domain";
		String researchObjectsPath = "/uia/researchobjects/researchobject";
		Domain domain = new Domain();
		domain.setUri("http://testResearchObject");
		ClientConfig config = new DefaultClientConfig();

		Client client = Client.create(config);
		URI testServiceURI = UriBuilder.fromUri("http://localhost:8080/epnoi/rest").build();
		WebResource service = client.resource(testServiceURI);

/*LET HERE FOR FUTURE FIXING : How to translate an empty put in jersey 2

		System.out.println("We create a domain");
		service.path(domainsPath).queryParam("uri", domainURI)
				.queryParam("type", domainType).request()
				.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(Entity.entity(""));
*/
		// -----------------------------------------------------------------------------
		System.out.println("Then we retrieve it");
		Domain retrievedDomain = service.path(domainsPath)
				.queryParam("uri", domainURI)
				.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
				.get(Domain.class);

		System.out.println("The retrieved domain was: " + retrievedDomain);

		// -----------------------------------------------------------------------------
		List<String> properties = Arrays.asList("label", "type", "expression");
/*LET HERE FOR FUTURE FIXING : How to translate an empty post in jersey 2
		for (String property : properties) {
			service.path(domainsPath + "/properties/" + property)
					.queryParam("uri", domainURI)
					.queryParam("value", property + "Value").request()
					.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).post();

			System.out.println("Then we retrieve it");
			retrievedDomain = service.path(domainsPath)
					.queryParam("uri", domainURI).request()
					.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON)
					.get(Domain.class);

			System.out.println("The retrieved domain, with its new " + property
					+ ", is: " + retrievedDomain);

		}
		*/
		// -----------------------------------------------------------------------------
		System.out
				.println("-----------------------------------------------------------------------------");
		System.out
				.println("Initial version of the research object associated with the domain");
		ResearchObject retrievedResearchObject = service
				.path(researchObjectsPath)
				.queryParam("uri", domainURI + "/resources")
				.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
				.get(ResearchObject.class);
		System.out.println("The modified with the POST RO response was: "
				+ retrievedResearchObject);
/*LET HERE FOR FUTURE FIXING : How to translate an empty post in jersey 2
		System.out.println("Lets modify the domain resources aggregation");
		service.path(domainsPath + "/resources").queryParam("uri", domainURI)
				.queryParam("resourceuri", "http://newResource").request()
				.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).post();

		service.path(domainsPath + "/resources").queryParam("uri", domainURI)
				.queryParam("resourceuri", "http://newResourceThatNeverWas").request()
				.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).post();
*/
		service.path(domainsPath + "/resources").queryParam("uri", domainURI)
				.queryParam("resourceuri", "http://newResourceThatNeverWas")
				.type(javax.ws.rs.core.MediaType.APPLICATION_JSON).delete();

		System.out
				.println("Then we get it, again after modifiying the resources");
		retrievedResearchObject = service.path(researchObjectsPath)
				.queryParam("uri", domainURI + "/resources")
				.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
				.get(ResearchObject.class);
		System.out.println("The modified with the POST RO response was: "
				+ retrievedResearchObject);
		System.out
				.println("-----------------------------------------------------------------------------");

		System.out.println("Lets delete the domain!");

		service.path(domainsPath).queryParam("uri", domainURI).type(javax.ws.rs.core.MediaType.APPLICATION_JSON).delete();

		System.out.println("Now if we get it, something should fail ");

		try {
			Object failedResponse = service.path(domainURI)
					.queryParam("uri", domainURI).type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
					.get(Domain.class);
			System.out.println("failed response " + failedResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out
				.println("The associated research object should not be there...");
		try {
			 System.out.println("----> " + service.path(researchObjectsPath)
					 .queryParam("uri", domainURI + "/resources").
					 type(javax.ws.rs.core.MediaType.APPLICATION_JSON).head().getStatus());

			

		} catch (Exception e) {
			System.out.println("---> " + retrievedResearchObject);
			e.printStackTrace();

		}

	}
}
