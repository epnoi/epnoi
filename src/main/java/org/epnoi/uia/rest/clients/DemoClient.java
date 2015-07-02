package org.epnoi.uia.rest.clients;

import org.epnoi.model.Domain;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class DemoClient {

	public static void main(String[] args) {

		String domainURI = "http://www.epnoi.org/CGTestCorpusDomain";
		String domainType = "paper";
		String domainsPath = "/uia/domains/domain";
		String resourcePath = "/opt/epnoi/epnoideployment/firstReviewResources/CGCorpus/A33_C03_Capturing_and_Animating_Occluded_Cloth__CORPUS__v3.xml";

		Domain domain = new Domain();

		domain.setURI(domainURI);

		ClientConfig config = new DefaultClientConfig();
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
				Boolean.TRUE);
		Client client = Client.create(config);

		WebResource service = client
				.resource("http://localhost:8081/epnoi/rest");

		System.out.println("We create a domain");
		service.path(domainsPath).queryParam("uri", domainURI)
				.queryParam("type", domainType)
				.type(javax.ws.rs.core.MediaType.APPLICATION_JSON).put();

	}
}