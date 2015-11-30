package org.epnoi.api.rest.clients;


public class AnnotationsResourceClient {

	public static void main(String[] args) {
		/*
		try {

			ResearchObject researchObject = new ResearchObject();
			researchObject.setUri("http://testResearchObject2");
			researchObject.getAggregatedResources().add("http://resourceA");
			researchObject.getAggregatedResources().add("http://resourceB");
			researchObject.getDcProperties().addPropertyValue(
					DublinCoreRDFHelper.TITLE_PROPERTY,
					"First RO, loquetienesquebuscar");
			researchObject.getDcProperties().addPropertyValue(
					DublinCoreRDFHelper.DESCRIPTION_PROPERTY,
					"Description of the first RO");
			researchObject.getDcProperties().addPropertyValue(
					DublinCoreRDFHelper.DATE_PROPERTY, "2005-02-28T00:00:00Z");

			ClientConfig config = new DefaultClientConfig();
			config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
					Boolean.TRUE);
			Client client = Client.create(config);

			WebResource service = client
					.resource("http://localhost:8081/epnoiUIA/rest");

			// UIA/ResearchObjects/ResearchObject";

			System.out.println("First we put the RO " + researchObject);
			service.path("/uia/researchobjects/researchobject")
					.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
					.put(researchObject);

			System.out.println("Lets see the RO annotations");

			Object response = service.path("/uia/annotations")
					.queryParam("uri", researchObject.getUri())
					.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
					.get(ArrayList.class);

			System.out.println("-------> " + response);

			service.path("/uia/annotations/annotation")
					.queryParam("uri", researchObject.getUri())
					.queryParam("annotationuri", "http://topicA")
					.type(javax.ws.rs.core.MediaType.APPLICATION_JSON).post();

			service.path("/uia/annotations/annotation")
					.queryParam("uri", researchObject.getUri())
					.queryParam("annotationuri", "http://topicB")
					.type(javax.ws.rs.core.MediaType.APPLICATION_JSON).post();

			ArrayList<String> annotations = service.path("/uia/annotations")
					.queryParam("uri", researchObject.getUri())
					.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
					.get(ArrayList.class);

			System.out.println("-------> " + annotations);
			String annotationToDelete = null;
			for (String annotationURI : annotations) {
				System.out.println(" -> " + annotationURI);
				Annotation annotation = service
						.path("/uia/annotations/annotation")
						.queryParam("uri", annotationURI)
						.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
						.get(Annotation.class);
				System.out.println("> " + annotation);
				annotationToDelete = annotation.getHasTopic();
			}
			System.out.println("We are going to delete " + annotationToDelete);

			service.path("/uia/annotations/annotation")
					.queryParam("uri", researchObject.getUri())
					.queryParam("annotationuri",annotationToDelete )
					.type(javax.ws.rs.core.MediaType.APPLICATION_JSON).delete();

			annotations = service.path("/uia/annotations")
					.queryParam("uri", researchObject.getUri())
					.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
					.get(ArrayList.class);

			System.out.println("Annotations are finally-------> " + annotations);
			
			System.out.println("-----------------------------------------------------------------------------------------------------------------------");
			

			System.out.println("Lets see the RO annotations");

			response = service.path("/uia/labels")
					.queryParam("uri", researchObject.getUri())
					.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
					.get(ArrayList.class);

			System.out.println("<RO labels-------> " + response);

			service.path("/uia/labels/label")
					.queryParam("uri", researchObject.getUri())
					.queryParam("label", "labelA")
					.type(javax.ws.rs.core.MediaType.APPLICATION_JSON).post();

			service.path("/uia/labels/label")
					.queryParam("uri", researchObject.getUri())
					.queryParam("label", "labelB")
					.type(javax.ws.rs.core.MediaType.APPLICATION_JSON).post();

			ArrayList<String> labels = service.path("/uia/labels")
					.queryParam("uri", researchObject.getUri())
					.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
					.get(ArrayList.class);

			System.out.println("labels -------> " + labels);
			String labelToDelete = null;
			for (String annotationURI : labels) {
				System.out.println(" -> " + annotationURI);
				Annotation annotation = service
						.path("/uia/labels/label")
						.queryParam("uri", annotationURI)
						.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
						.get(Annotation.class);
				System.out.println("> " + annotation);
				labelToDelete = annotation.getLabel();
			}
			System.out.println("We are going to delete the label " +labelToDelete);

			service.path("/uia/labels/label")
					.queryParam("uri", researchObject.getUri())
					.queryParam("label",labelToDelete )
					.type(javax.ws.rs.core.MediaType.APPLICATION_JSON).delete();

			annotations = service.path("/uia/labels")
					.queryParam("uri", researchObject.getUri())
					.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
					.get(ArrayList.class);

			System.out.println("final labels-------> " + annotations);
					
			
			
			


		} catch (Exception e) {

			e.printStackTrace();

		}
*/
	}
}
