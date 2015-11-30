package org.epnoi.api.rest.clients;


public class UIAResourceClient {

	public static void main(String[] args) {
		try {
/*
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

			Client client = ClientBuilder.newClient(config);
			ClientConfig config = new ClientConfig();
			Client client = ClientBuilder.newClient(config);
			URI uri = UriBuilder.fromUri("http://localhost:8081/epnoiUIA/rest").build();
			WebTarget service = client
					.target(uri);

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
*/
		/*

			service.path("/uia/researchobjects/researchobject")
					.queryParam("uri", researchObject.getUri())
					.type(javax.ws.rs.core.MediaType.APPLICATION_JSON).delete();

*/
		} catch (Exception e) {

			e.printStackTrace();

		}

	}
}
