package org.epnoi.uia.rest.services;

import java.net.URI;
import java.net.URISyntaxException;
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
import org.epnoi.model.Resource;
import org.epnoi.uia.informationstore.dao.rdf.AnnotationRDFHelper;

import com.sun.jersey.api.Responses;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/uia/labels")
@Api(value = "/uia/labels", description = "Operations about resource labeling")
public class LabelsResource extends UIAService {

	@Context
	ServletContext context;

	// ----------------------------------------------------------------------------------------
	@PostConstruct
	public void init() {
		logger = Logger.getLogger(LabelsResource.class.getName());
		logger.info("Initializing " + getClass());
		this.core = this.getUIACore();

	}

	// -----------------------------------------------------------------------------------------

	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets labels of a resource", notes = "", response = String.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Something went wrong in the UIA"),
			@ApiResponse(code = 200, message = "The resource URI exists, its labels are returned"),
			@ApiResponse(code = 404, message = "A resource with such URI could not be found") })
	public Response getLabels(
			@ApiParam(value = "Resource URI", required = true, allowMultiple = false) @QueryParam("uri") String URI) {
		logger.info("GET labels> " + URI);

		Resource resource = this.core.getInformationAccess().get(URI);
		if (resource != null) {

			ArrayList<String> labels = new ArrayList<String>(this.core
					.getAnnotationHandler().getLabels(URI));

			return Response.ok().entity(labels).build();
		} else {

			return Response.status(Responses.NOT_FOUND).build();
		}

	}

	// -----------------------------------------------------------------------------------------

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/label")
	@ApiOperation(value = "Returns the label with the provided URI", notes = "", response = Annotation.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The label annotation URI has been retrieved"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA"),
			@ApiResponse(code = 404, message = "A label annotation with such URI could not be found") })
	public Response getLabel(
			@ApiParam(value = "URI of the label annotation ", required = true, allowMultiple = false) @QueryParam("uri") String uri) {

		logger.info("GET label uri=" + uri);

		Annotation annotation = (Annotation) this.core.getInformationAccess()
				.get(uri, AnnotationRDFHelper.ANNOTATION_CLASS);

		if (annotation != null) {
			return Response.ok().entity(annotation).build();

		} else {

			return Response.status(Responses.NOT_FOUND).build();
		}
	}

	// -----------------------------------------------------------------------------------------

	@POST
	@Path("/label")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Annotates the resource using the label", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "An annotation has been created and used to label the resource with that URI"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA") })
	public Response label(
			@ApiParam(value = "URI of the resource to be labeled", required = true, allowMultiple = false) @QueryParam("uri") String URI,
			@ApiParam(value = "Label used to annotate the resource", required = true, allowMultiple = false) @QueryParam("label") String label) {
		logger.info("POST label uri=" + URI + " label " + label);

		Annotation annotation = core.getAnnotationHandler().label(URI, label);

		URI annotationURI = null;
		try {
			annotationURI = new URI(annotation.getURI());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Response.created(annotationURI).build();

	}

	// -----------------------------------------------------------------------------------------

	@DELETE
	@Path("/label")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Unlabels the resource URI with label", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The resource URI is no longer annotated using that label"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA") })
	public Response removeAnnotation(
			@ApiParam(value = "URI of the resource to be unlabeled", required = true, allowMultiple = false) @QueryParam("uri") String URI,
			@ApiParam(value = "Label that no longer annotates the resource", required = true, allowMultiple = false) @QueryParam("label") String label) {

		this.core.getAnnotationHandler().removeLabel(URI, label);

		return Response.ok().build();
	}
}
