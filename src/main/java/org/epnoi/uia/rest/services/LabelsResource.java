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

@Path("/uia/labels")
public class LabelsResource extends UIAService {

	@Context
	ServletContext context;

	// ----------------------------------------------------------------------------------------
	@PostConstruct
	public void init() {
		logger = Logger.getLogger(LabelsResource.class.getName());
		logger.info("Initializing "+getClass());
		this.core = this.getUIACore();

	}

	// -----------------------------------------------------------------------------------------

	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLabels(@QueryParam("uri") String URI) {
		logger.info("GET labels> " + URI);

		System.out.println("--|>"+this.core.getAnnotationHandler()
				.getLabels(URI));
		
		
		ArrayList<String> labels = new ArrayList<String>(this.core.getAnnotationHandler()
				.getLabels(URI));

		return Response.status(200).entity(labels).build();
	}

	// -----------------------------------------------------------------------------------------

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/label")
	public Response getLabel(@QueryParam("uri") String uri) {

		System.out.println("GET label uri=" + uri);

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
	@Path("/label")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response label(@QueryParam("uri") String URI,
			@QueryParam("label") String label) {
		System.out.println(" ro " + URI + " label " + label);

		core.getAnnotationHandler().label(URI, label);
		
		return Response.ok().status(201).build();

	}

	// -----------------------------------------------------------------------------------------

	@DELETE
	@Path("/label")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeAnnotation(@QueryParam("uri") String URI,
			@QueryParam("label") String label) {
		System.out.println("DELETE label > " + URI + " resource> " + URI);
		this.core.getAnnotationHandler().removeLabel(URI,label);
		return Response.ok().status(201).build();
	}

}
