package org.epnoi.rest.clients;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import org.epnoi.model.Paper;
import org.epnoi.model.ResearchObject;
import org.epnoi.model.rdf.DublinCoreRDFHelper;

public class UIAResourceClient {

	public static void main(String[] args) {
		try {

			ResearchObject researchObject = new ResearchObject();
			researchObject.setUri("http://testResearchObject");
			researchObject.getAggregatedResources().add("http://resourceA");
			researchObject.getAggregatedResources().add("http://resourceB");
			researchObject.getDcProperties().addPropertyValue(
					DublinCoreRDFHelper.TITLE_PROPERTY,
					"First RO, loquetienesquebuscar");
			researchObject.getDcProperties().addPropertyValue(
					DublinCoreRDFHelper.DESCRIPTION_PROPERTY,
					"Description of the test Research Object about matrices");
			researchObject.getDcProperties().addPropertyValue(
					DublinCoreRDFHelper.DATE_PROPERTY, "2005-02-28T00:00:00Z");

			ClientConfig config = new DefaultClientConfig();
			config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
					Boolean.TRUE);
			Client client = Client.create(config);

			WebResource service = client
					.resource("http://localhost:8081/epnoiUIA/rest");

			String paperURI = "oai:arXiv.org:1012.2513";
			System.out.println("Lets get the paper ");
			Paper paper = service.path("/uia/resources/papers")
					.queryParam("uri", paperURI)
					.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
					.get(Paper.class);
			System.out.println("The retrieved paper is " + paper);

			System.out.println("First we put the RO " + researchObject);
			service.path("/uia/researchobjects/researchobject")
					.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
					.put(researchObject);

			System.out.println("Lets send it back");
			service.path("/uia/resources/papers")
					.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
					.post(paper);

			/*
			 * System.out.println("Then we get it "); Object response = service
			 * .path("/uia/researchobjects/researchobject") .queryParam("uri",
			 * researchObject.getURI())
			 * .type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
			 * .get(ResearchObject.class);
			 * System.out.println("The response was: " + response);
			 * 
			 * System.out.println("Lets modify the RO aggregation");
			 * service.path("/uia/researchobjects/researchobject/aggregation")
			 * .queryParam("uri", researchObject.getURI())
			 * .queryParam("resourceuri", "http://newResource")
			 * .type(javax.ws.rs.core.MediaType.APPLICATION_JSON).post();
			 * 
			 * service.path("/uia/researchobjects/researchobject/aggregation")
			 * .queryParam("uri", researchObject.getURI())
			 * .queryParam("resourceuri", "http://newResourceThatNeverWas")
			 * .type(javax.ws.rs.core.MediaType.APPLICATION_JSON).post();
			 * service.path("/uia/researchobjects/researchobject/aggregation")
			 * .queryParam("uri", researchObject.getURI())
			 * .queryParam("resourceuri", "http://newResourceThatNeverWas")
			 * .type(javax.ws.rs.core.MediaType.APPLICATION_JSON).delete();
			 * 
			 * 
			 * service.path("/uia/researchobjects/researchobject/dc/date")
			 * .queryParam("uri", researchObject.getURI()) .queryParam("value",
			 * "2015-12-28T00:00:00Z")
			 * .type(javax.ws.rs.core.MediaType.APPLICATION_JSON).post();
			 * 
			 * 
			 * System.out.println("Then we get it "); ResearchObject retrievedRO
			 * = service .path("/uia/researchobjects/researchobject")
			 * .queryParam("uri", researchObject.getURI())
			 * .type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
			 * .get(ResearchObject.class);
			 * System.out.println("The modified response was: " + retrievedRO);
			 * 
			 * retrievedRO.getAggregatedResources().remove("http://resourceA");
			 * 
			 * System.out.println("----> " + retrievedRO);
			 * 
			 * System.out
			 * .println("Lets modify the RO aggregation again, using an RO post"
			 * ); service.path("/uia/researchobjects/researchobject")
			 * .type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
			 * .post(ResearchObject.class, retrievedRO);
			 * 
			 * System.out.println("Then we get it, again "); retrievedRO =
			 * service.path("/uia/researchobjects/researchobject")
			 * .queryParam("uri", researchObject.getURI())
			 * .type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
			 * .get(ResearchObject.class);
			 * System.out.println("The modified with the POST RO response was: "
			 * + response);
			 */
			/*
			 * System.out.println("Lets modify the RO title");
			 * service.path("/uia/researchobjects/researchobject/dc/title")
			 * .queryParam("uri", researchObject.getURI()) .queryParam("value",
			 * "New title!")
			 * .type(javax.ws.rs.core.MediaType.APPLICATION_JSON).post();
			 * 
			 * 
			 * System.out.println("Now we deleted the aggreagated resource added"
			 * );
			 * service.path("/uia/researchobjects/researchobject/aggregation")
			 * .queryParam("uri", researchObject.getURI())
			 * .queryParam("resourceuri", "http://newResource")
			 * .type(javax.ws.rs.core.MediaType.APPLICATION_JSON).delete();
			 * 
			 * 
			 * response = service .path("/uia/researchobjects/researchobject")
			 * .queryParam("uri", researchObject.getURI())
			 * .type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
			 * .get(ResearchObject.class);
			 * System.out.println("After deleting the resource we have: " +
			 * response);
			 */

			service.path("/uia/researchobjects/researchobject")
					.queryParam("uri", researchObject.getUri())
					.type(javax.ws.rs.core.MediaType.APPLICATION_JSON).delete();
			/*
			 * System.out.println("Now if we get it, something should fail ");
			 * Object failedResponse = service
			 * .path("/uia/researchobjects/researchobject") .queryParam("uri",
			 * researchObject.getURI())
			 * .type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
			 * .get(ResearchObject.class); System.out.println("failed response "
			 * + failedResponse);
			 */
			/*
			 * 
			 * Client client = Client.create();
			 * 
			 * 
			 * WebResource webResource = client .resource(
			 * "http://localhost:8081/epnoiUIA/rest/UIA/ResearchObjects/ResearchObject"
			 * );
			 * 
			 * 
			 * webResource.accept("application/json")
			 * .post(ResearchObject.class, researchObject);
			 */

		} catch (Exception e) {

			e.printStackTrace();

		}

	}
}
