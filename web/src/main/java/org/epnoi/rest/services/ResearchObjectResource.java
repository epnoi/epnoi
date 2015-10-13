package org.epnoi.rest.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.epnoi.model.DublinCoreMetadataElementsSetHelper;
import org.epnoi.model.ResearchObject;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.rest.services.response.jsonld.JSONLDResearchObjectResponseBuilder;
import org.epnoi.rest.services.response.jsonld.JSONLDResponse;

import com.sun.jersey.api.Responses;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/uia/researchobjects/researchobject")
@Api(value = "/uia/researchobjects/researchobject", description = "Operations for handling Research Objects")
public class ResearchObjectResource extends UIAService {

	@Context
	ServletContext context;

	// ----------------------------------------------------------------------------------------
	@PostConstruct
	public void init() {
		logger = Logger.getLogger(ResearchObjectResource.class.getName());
		logger.info("Initializing ResearchObjectResource");
		this.core = this.getUIACore();

	}

	// -----------------------------------------------------------------------------------------

	@PUT
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Creates a Researh Object", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "The Research Object has been created"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA") })
	public Response createResearchObject(ResearchObject researchObject) {
		logger.info("PUT RO> " + researchObject);

		URI researchObjectURI = null;
		try {
			researchObjectURI = new URI(researchObject.getUri());
		} catch (URISyntaxException e) {
			throw new WebApplicationException();
		}
		this.core.getInformationHandler().put(researchObject,
				org.epnoi.model.Context.getEmptyContext());
		return Response.created(researchObjectURI).build();
	}

	// -----------------------------------------------------------------------------------------

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The Research Object has been retrieved"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA"),
			@ApiResponse(code = 404, message = "A Research Object with such URI could not be found") })
	@ApiOperation(value = "Returns the label with the provided URI", notes = "", response = ResearchObject.class)
	public Response getResearchObject(
			@ApiParam(value = "Research Object URI", required = true, allowMultiple = false) @QueryParam("uri") String uri,
			@ApiParam(value = "Desired JSON format for the retrieved Research Object", required = true, allowMultiple = false, allowableValues = "json,jsonld") @QueryParam("format") String format) {

		logger.info("GET RO> uri=" + uri + " format=" + format);

		ResearchObject researchObject = (ResearchObject) core
				.getInformationHandler().get(uri,
						RDFHelper.RESEARCH_OBJECT_CLASS);

		/*
		 * ResearchObject researchObject = new ResearchObject();
		 * researchObject.setURI("http://testResearchObject");
		 * researchObject.getAggregatedResources().add("http://resourceA");
		 * researchObject.getAggregatedResources().add("http://resourceB");
		 * researchObject.getDcProperties().addPropertyValue(
		 * DublinCoreRDFHelper.TITLE_PROPERTY,
		 * "First RO, loquetienesquebuscar");
		 * researchObject.getDcProperties().addPropertyValue(
		 * DublinCoreRDFHelper.DESCRIPTION_PROPERTY,
		 * "Description of the first RO");
		 * researchObject.getDcProperties().addPropertyValue(
		 * DublinCoreRDFHelper.DATE_PROPERTY, "2005-02-28T00:00:00Z");
		 */
		if (researchObject != null) {

			if ("jsonld".equals(format)) {
				JSONLDResponse researchObjectResponse = JSONLDResearchObjectResponseBuilder
						.build(researchObject);
				Map<String, Object> responseMap = researchObjectResponse
						.convertToMap();

				return Response.ok(responseMap, MediaType.APPLICATION_JSON)
						.build();
			} else {
				return Response.ok(researchObject, MediaType.APPLICATION_JSON)
						.build();
			}
		} else {

			return Response.status(Responses.NOT_FOUND).build();
		}
	}

	// -----------------------------------------------------------------------------------------

	@POST
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Updates a Research Object", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The resource has been deleted from the Research Object "),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA"),
			@ApiResponse(code = 404, message = "Either a Research Object or the an aggregated resource with such URI could not be found") })
	public Response updateResearchObject(ResearchObject researchObject) {
		logger.info("POST RO> " + researchObject);

		ResearchObject researchObjectToBeUpdated = (ResearchObject) core
				.getInformationHandler().get(researchObject.getUri(),
						RDFHelper.RESEARCH_OBJECT_CLASS);

		if (researchObjectToBeUpdated != null) {
			core.getInformationHandler().update(researchObject);
			return Response.ok().build();
		} else {
			return Response.status(Responses.NOT_FOUND).build();
		}

	}

	// -----------------------------------------------------------------------------------------

	@POST
	@Path("/aggregation")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Sets a Researh Object Dublin Core property", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The resource has been deleted from the Research Object "),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA"),
			@ApiResponse(code = 404, message = "Either a Research Object or the an aggregated resource with such URI could not be found") })
	public Response addAggregatedResource(
			@ApiParam(value = "Research Object uri", required = true, allowMultiple = false) @QueryParam("uri") String URI,
			@ApiParam(value = "Resource to be aggregated to the Research Object", required = true, allowMultiple = false) @QueryParam("resourceuri") String resourceURI) {
		logger.info(" ro " + URI + " aggr " + resourceURI);

		ResearchObject researchObject = (ResearchObject) core
				.getInformationHandler().get(URI,
						RDFHelper.RESEARCH_OBJECT_CLASS);

		if (researchObject != null) {
			if (!researchObject.getAggregatedResources().contains(resourceURI)) {
				researchObject.getAggregatedResources().add(resourceURI);
				core.getInformationHandler().update(researchObject);
			}

		} else {
			return Response.status(Responses.NOT_FOUND).build();
		}

		return Response.ok().build();

	}

	// -----------------------------------------------------------------------------------------

	@POST
	@Path("/dc/{PROPERTY}")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Sets a Researh Object Dublin Core property", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The Research Object has been retrieved"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA"),
			@ApiResponse(code = 404, message = "A Research Object with such URI could not be found") })
	public Response updateDCProperty(
			@ApiParam(value = "Research Object URI", required = true, allowMultiple = false) @QueryParam("uri") String URI,
			@ApiParam(value = "Dublin Core property value", required = true, allowMultiple = false) @QueryParam("value") String value,
			@ApiParam(value = "Dublin Core property name", required = true, allowMultiple = false, allowableValues = "title,description,date,creator") @PathParam("PROPERTY") String propertyName) {
		logger.info(" ro " + URI + " property " + propertyName + "value> "
				+ value);
		System.out.println("Updating the property "
				+ DublinCoreMetadataElementsSetHelper
						.getPropertyURI(propertyName) + " with value " + value);

		ResearchObject researchObject = (ResearchObject) core
				.getInformationHandler().get(URI,
						RDFHelper.RESEARCH_OBJECT_CLASS);
		String propertyURI = DublinCoreMetadataElementsSetHelper
				.getPropertyURI(propertyName);
		if (researchObject != null && propertyURI != null) {

			researchObject.getDcProperties().addPropertyValue(propertyURI,
					value);
			core.getInformationHandler().update(researchObject);
		} else {
			return Response.status(Responses.NOT_FOUND).build();
		}

		return Response.ok().build();

	}

	// -----------------------------------------------------------------------------------------

	@DELETE
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Removes a Researh Object", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The Research Object with such URI has been deleted"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA"),
			@ApiResponse(code = 404, message = "A Research Object with such URI could not be found") })
	public Response removeResearchObject(
			@ApiParam(value = "Research Object uri", required = true, allowMultiple = false) @QueryParam("uri") String URI) {
		logger.info("DELETE RO  > " + URI + " resource> " + URI);

		ResearchObject researchObject = (ResearchObject) core
				.getInformationHandler().get(URI,
						RDFHelper.RESEARCH_OBJECT_CLASS);
		if (researchObject != null) {
			this.core.getInformationHandler().remove(URI,
					RDFHelper.RESEARCH_OBJECT_CLASS);
			return Response.ok().build();
		} else {
			return Response.status(Responses.NOT_FOUND).build();
		}

	}

	// -----------------------------------------------------------------------------------------

	@DELETE
	@Path("/aggregation")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Removes an aggregated resource from a Researh Object", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The resource has been deleted from the Research Object "),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA"),
			@ApiResponse(code = 404, message = "Either a Research Object or the an aggregated resource with such URI could not be found") })
	public Response removeAggregatedResource(
			@ApiParam(value = "Research Object uri", required = true, allowMultiple = false) @QueryParam("uri") String URI,
			@ApiParam(value = "Aggregated resource to delete uri", required = true, allowMultiple = false) @QueryParam("resourceuri") String resourceURI) {
		ResearchObject researchObject = (ResearchObject) core
				.getInformationHandler().get(URI,
						RDFHelper.RESEARCH_OBJECT_CLASS);
		if (researchObject != null
				&& researchObject.getAggregatedResources()
						.contains(resourceURI)) {
			researchObject.getAggregatedResources().remove(resourceURI);
			this.core.getInformationHandler().update(researchObject);
			return Response.ok().build();
		} else {

			return Response.status(Responses.NOT_FOUND).build();
		}
	}
}
