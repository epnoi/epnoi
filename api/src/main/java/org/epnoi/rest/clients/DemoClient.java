package org.epnoi.rest.clients;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import org.apache.lucene.index.Terms;
import org.epnoi.model.Domain;
import org.epnoi.model.Relation;
import org.epnoi.model.ResearchObject;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class DemoClient {

	public static void main(String[] args) {

		String domainURI = "http://www.epnoi.org/CGTestCorpusDomain";
		String domainType = "paper";
		String domainsPath = "/uia/domains/domain";
		String researchObjectsPath = "/uia/researchobjects/researchobject";
		String corpusDirectoryPath = "/opt/epnoi/epnoideployment/firstReviewResources/CGCorpus/";

		String resourcePath = "/opt/epnoi/epnoideployment/firstReviewResources/CGCorpus/A33_C03_Capturing_and_Animating_Occluded_Cloth__CORPUS__v3.xml";

		Domain domain = new Domain();

		domain.setUri(domainURI);

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

		// -----------------------------------------------------------------------------

		System.out.println("Then we retrieve it");
		Domain retrievedDomain = service.path(domainsPath)
				.queryParam("uri", domainURI)
				.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
				.get(Domain.class);
		System.out.println(retrievedDomain);

		List<Terms> response = service.path(domainsPath + "/terms")
				.queryParam("uri", domainURI)
				.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
				.get(ArrayList.class);
		System.out.println("The list of terms is " + response);

		System.out.println("Let's add a resource");

		int numberOfResources = 1;
		int i = 0;

		for (String filePath : scanFilesToHarverst(new File(corpusDirectoryPath))) {
			if (i < numberOfResources) {
				System.out
						.println("Lets modify the domain resources aggregation with the file : "
								+ corpusDirectoryPath + filePath);
				service.path(domainsPath + "/resources")
						.queryParam("uri", domainURI)
						.queryParam("resourceuri",
								"file://" + corpusDirectoryPath + filePath)
						.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
						.post();
			}
			i++;
		}

		response = service.path(domainsPath + "/terms")
				.queryParam("uri", domainURI)
				.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
				.get(ArrayList.class);
		System.out.println("The list of terms now is " + response);

		System.out.println("Then we retrieve it");
		retrievedDomain = service.path(domainsPath)
				.queryParam("uri", domainURI)
				.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
				.get(Domain.class);
		System.out.println("----> " + retrievedDomain);

		System.out
				.println("Then we get it, again after modifiying the resources");
		ResearchObject retrievedResearchObject = service
				.path(researchObjectsPath)
				.queryParam("uri", domainURI + "/resources")
				.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
				.get(ResearchObject.class);
		System.out.println("The modified with the POST RO response was: "
				+ retrievedResearchObject);

		List<Relation> relationsResponse = service
				.path(domainsPath + "/relations").queryParam("uri", domainURI)
				.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
				.get(ArrayList.class);
		System.out.println("The list of relations is " + relationsResponse);

		System.out.println("Lets delete the domain!");
		/*
		 * service.path(domainsPath).queryParam("uri", domainURI)
		 * .type(javax.ws.rs.core.MediaType.APPLICATION_JSON).delete();
		 */
	}

	// ----------------------------------------------------------------------------------------

	public static String[] scanFilesToHarverst(File directoryToHarvest) {
		String[] filesToHarvest = directoryToHarvest.list(new FilenameFilter() {

			public boolean accept(File current, String name) {
				File file = new File(current, name);
				return (file.isFile()) && (!file.isHidden());
			}

		});
		return filesToHarvest;
	}
}