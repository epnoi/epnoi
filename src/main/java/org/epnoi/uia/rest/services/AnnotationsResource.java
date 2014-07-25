package org.epnoi.uia.rest.services;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.epnoi.model.Annotation;
import org.epnoi.uia.informationstore.dao.rdf.AnnotationRDFHelper;

@Path("/uia/annotations")
public class AnnotationsResource extends UIAService {

	@Context
	ServletContext context;

	// ----------------------------------------------------------------------------------------
	@PostConstruct
	public void init() {
		logger = Logger.getLogger(AnnotationsResource.class.getName());
		logger.info("Initializing ResearchObjectResource");
		this.core = this.getUIACore();

	}

	// -----------------------------------------------------------------------------------------

	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAnnotations(@QueryParam("uri") String URI) {
		logger.info("GET Annotations> " + URI);

		ArrayList<String> annotations = new ArrayList<String>(this.core.getAnnotationHandler()
				.getAnnotations(URI));

		return Response.status(200).entity(annotations).build();
	}

	// -----------------------------------------------------------------------------------------

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/annotation")
	public Response getAnnotation(@QueryParam("uri") String uri) {

		System.out.println("GET Annotation> uri=" + uri);

		Annotation annotation = (Annotation) this.core.getInformationAccess()
				.get(uri, AnnotationRDFHelper.ANNOTATION_CLASS);

		if (annotation != null) {
			return Response.status(200).entity(annotation).build();

		} else {

			return Response.status(404).build();
		}

	}

	// -----------------------------------------------------------------------------------------

	@POST
	@Path("annotation")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response annotate(@QueryParam("uri") String URI,
			@QueryParam("annotationuri") String annotationURI) {
		System.out.println(" ro " + URI + " annotationURI " + annotationURI);

		core.getAnnotationHandler().annotate(URI, annotationURI);

		System.out.println("....> "
				+core.getAnnotationHandler().getAnnotations(URI));
		
		
		return Response.ok().status(201).build();

	}

	// -----------------------------------------------------------------------------------------

	@DELETE
	@Path("/annotation")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeAnnotation(@QueryParam("uri") String URI,
			@QueryParam("annotationuri") String annotationURI) {
		System.out.println("DELETE Annotation  > " + URI + " resource> " + URI);
		this.core.getAnnotationHandler().removeAnnotation(URI, annotationURI);
		return Response.ok().status(201).build();
	}

}
