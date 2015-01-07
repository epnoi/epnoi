package org.epnoi.uia.rest.services;

import java.util.HashMap;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.epnoi.model.Resource;
import org.epnoi.uia.informationstore.dao.rdf.AnnotationRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.InformationSourceRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.InformationSourceSubscriptionRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.UserRDFHelper;

import com.sun.jersey.api.Responses;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/uia/resources")
@Api(value = "/uia/resources", description = "Operations for handling resources")
public class ResourcesResource extends UIAService {

	@Context
	ServletContext context;

	Map<String, Class<? extends Resource>> knownDeserializableClasses = new HashMap<>();

	private static Map<String, String> resourceTypesTable = new HashMap<String, String>();
	static {
		resourceTypesTable.put("papers", RDFHelper.PAPER_CLASS);
		resourceTypesTable.put("users", UserRDFHelper.USER_CLASS);
		resourceTypesTable.put("informationsources",
				InformationSourceRDFHelper.INFORMATION_SOURCE_CLASS);
		resourceTypesTable
				.put("informationsourcesubscriptions",
						InformationSourceSubscriptionRDFHelper.INFORMATION_SOURCE_SUBSCRIPTION_CLASS);
		resourceTypesTable.put("researchobjects",
				RDFHelper.RESEARCH_OBJECT_CLASS);
		resourceTypesTable.put("annotations",
				AnnotationRDFHelper.ANNOTATION_CLASS);
	}

	// --------------------------------------------------------------------------------

	@PostConstruct
	public void init() {

		logger = Logger.getLogger(ResourcesResource.class.getName());
		logger.info("Initializing " + getClass());
		this.core = this.getUIACore();

	}

	// --------------------------------------------------------------------------------

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/{RESOURCE_TYPE}")
	@ApiOperation(value = "Update a resource of a given type", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "The resource has been updated"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA") })
	public Response updateResource(
			Resource resource,
			@ApiParam(value = "Resource type", required = true, allowMultiple = false, allowableValues = "papers,users,informationsources,informationsourcesubscriptions,researchobjects,annotations") @PathParam("RESOURCE_TYPE") String resourceType) {
		logger.info("POST: UIA " + resource);

		this.getUIACore().getInformationHandler().update(resource);
		return Response.ok().build();
	}

	// --------------------------------------------------------------------------------

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/{RESOURCE_TYPE}")
	@ApiOperation(value = "Creates a resource of a given type", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "The resource has been created"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA") })
	public Response createResource(
			Resource resource,
			@ApiParam(value = "Resource type", required = true, allowMultiple = false, allowableValues = "papers,users,informationsources,informationsourcesubscriptions,researchobjects,annotations") @PathParam("RESOURCE_TYPE") String resourceType) {
		logger.info("PUT: UIA " + resource);

		this.getUIACore().getInformationHandler()
				.put(resource, org.epnoi.model.Context.getEmptyContext());
		return Response.ok().build();
	}

	// --------------------------------------------------------------------------------

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{RESOURCE_TYPE}")
	// @Consumes(MediaType.APPLICATION_JSON)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The resource has been retrieved"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA"),
			@ApiResponse(code = 404, message = "A resource with such URI could not be found") })
	@ApiOperation(value = "Returns the resource with the provided URI", notes = "", response = Resource.class)
	public Response getResource(
			@ApiParam(value = "Resource URI", required = true, allowMultiple = false) @QueryParam("uri") String URI,
			@ApiParam(value = "Resource type", required = true, allowMultiple = false, allowableValues = "papers,users,informationsources,informationsourcesubscriptions,researchobjects,annotations") @PathParam("RESOURCE_TYPE") String resourceType) {
		logger.info("GET: UIA uri> " + URI + " reourceType > " + resourceType);

		String resourceClass = ResourcesResource.resourceTypesTable
				.get(resourceType);
		if ((URI != null) && (resourceClass != null)) {

			this.core = this.getUIACore();
			System.out.println("Getting the resource "
					+ ResourcesResource.resourceTypesTable.get(resourceType));
			Resource resource = this.core.getInformationHandler().get(URI,
					resourceClass);

			if (resource != null) {
				return Response.ok(resource, MediaType.APPLICATION_JSON)
						.build();
			}
		}
		return Response.status(Responses.NOT_FOUND).build();
	}

	// --------------------------------------------------------------------------------

	@DELETE
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/{RESOURCE_TYPE}")
	@ApiOperation(value = "Removes a resource", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The resource with such URI has been deleted"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA"),
			@ApiResponse(code = 404, message = "A resource with such URI could not be found") })
	public Response deleteResource(
			@ApiParam(value = "Resource URI", required = true, allowMultiple = false) @QueryParam("uri") String URI,
			@ApiParam(value = "Resource type", required = true, allowMultiple = false, allowableValues = "papers,users,informationsources,informationsourcesubscriptions,researchobjects,annotations") @PathParam("RESOURCE_TYPE") String resourceType) {
		logger.info("DELETE: UIA uri> " + URI + " reourceType > "
				+ resourceType);
		String resourceClass = ResourcesResource.resourceTypesTable
				.get(resourceType);
		if ((URI != null) && (resourceClass != null)) {
			this.core.getInformationHandler().remove(URI, resourceType);
			return Response.ok().build();
		}
		return Response.status(Responses.NOT_FOUND).build();
	}

	// --------------------------------------------------------------------------------
	/*
	 * @GET
	 * 
	 * @Produces({ MediaType.APPLICATION_JSON })
	 * 
	 * @Path("/notifications")
	 * 
	 * public Response getResource(
	 * 
	 * @DefaultValue("none") @QueryParam("URI") String URI) {
	 * System.out.println("GET: UIA");
	 * 
	 * List<InformationSourceNotification> notifications = new
	 * ArrayList<InformationSourceNotification>();
	 * 
	 * for (InformationSourceNotification notification : core
	 * .getInformationSourcesHandler().retrieveNotifications(URI)) {
	 * 
	 * notifications.add(notification); }
	 * 
	 * InformationSourceNotificationsSet notificationsSet = new
	 * InformationSourceNotificationsSet();
	 * 
	 * notificationsSet.setNotifications(notifications);
	 * notificationsSet.setURI(URI);
	 * 
	 * return Response.ok(notificationsSet, MediaType.APPLICATION_JSON)
	 * .build(); }
	 */
}
