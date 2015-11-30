package org.epnoi.api.rest.services.uia;


import io.swagger.annotations.*;
import org.epnoi.model.Resource;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
@Path("/uia/resources/bytype")
@Api(value = "/uia/resources/bytype", description = "Operations for handling resources of different types")
public class ResourcesByTypeResource {
    private static final Logger logger = Logger.getLogger(ResourcesByTypeResource.class
            .getName());
    @Autowired
    private Core core;

    Map<String, Class<? extends Resource>> knownDeserializableClasses = new HashMap<>();

    private static Map<String, String> resourceTypesTable = new HashMap<String, String>();

    static {
        resourceTypesTable.put("papers", RDFHelper.PAPER_CLASS);
        resourceTypesTable.put("users", UserRDFHelper.USER_CLASS);
        resourceTypesTable.put("informationsources", InformationSourceRDFHelper.INFORMATION_SOURCE_CLASS);
        resourceTypesTable.put("informationsourcesubscriptions",
                InformationSourceSubscriptionRDFHelper.INFORMATION_SOURCE_SUBSCRIPTION_CLASS);
        resourceTypesTable.put("researchobjects", RDFHelper.RESEARCH_OBJECT_CLASS);
        resourceTypesTable.put("annotations", AnnotationRDFHelper.ANNOTATION_CLASS);
        resourceTypesTable.put("domains", RDFHelper.DOMAIN_CLASS);
        resourceTypesTable.put("wikipediapages", RDFHelper.WIKIPEDIA_PAGE_CLASS);

    }

    // --------------------------------------------------------------------------------

    @PostConstruct
    public void init() {
        logger.info("Starting the " + this.getClass());
    }

    // --------------------------------------------------------------------------------

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{RESOURCE_TYPE}/resource")
    @ApiOperation(value = "Update a resource of a given type", notes = "")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "The resource has been updated"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA")})
    public Response updateResource(Resource resource,
                                   @ApiParam(value = "Resource type", required = true, allowMultiple = false, allowableValues = "papers,domains,users,informationsources,informationsourcesubscriptions,researchobjects,annotations,wikipediapages") @PathParam("RESOURCE_TYPE") String resourceType) {
        logger.info("POST: UIA " + resource);

        core.getInformationHandler().update(resource);
        return Response.ok().build();
    }

    // --------------------------------------------------------------------------------

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{RESOURCE_TYPE}/resource")
    @ApiOperation(value = "Creates a resource of a given type", notes = "")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "The resource has been created"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA")})
    public Response createResource(Resource resource,
                                   @ApiParam(value = "Resource type", required = true, allowMultiple = false, allowableValues = "papers,domains,users,informationsources,informationsourcesubscriptions,researchobjects,annotations,wikipediapages") @PathParam("RESOURCE_TYPE") String resourceType) {
        logger.info("PUT: UIA " + resource);

        core.getInformationHandler().put(resource, org.epnoi.model.Context.getEmptyContext());
        return Response.ok().build();
    }

    // --------------------------------------------------------------------------------

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{RESOURCE_TYPE}/resource")
    // @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "The resource has been retrieved"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA"),
            @ApiResponse(code = 404, message = "A resource with such URI could not be found")})
    @ApiOperation(value = "Returns the resource with the provided URI", notes = "", response = Resource.class)
    public Response getResource(
            @ApiParam(value = "Resource URI", required = true, allowMultiple = false) @QueryParam("uri") String URI,
            @ApiParam(value = "Resource type", required = true, allowMultiple = false, allowableValues = "papers,domains,users,informationsources,informationsourcesubscriptions,researchobjects,annotations,wikipediapages") @PathParam("RESOURCE_TYPE") String resourceType) {
        logger.info("GET: UIA uri> " + URI + " reourceType > " + resourceType);

        String resourceClass = ResourcesByTypeResource.resourceTypesTable.get(resourceType);

        if ((URI != null) && (resourceClass != null)) {


            System.out.println("Getting the resource " + ResourcesByTypeResource.resourceTypesTable.get(resourceType));
            Resource resource = null;
            try {
                resource = this.core.getInformationHandler().get(URI, resourceClass);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (resource != null) {
                return Response.ok(resource, MediaType.APPLICATION_JSON).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // --------------------------------------------------------------------------------

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{RESOURCE_TYPE}")
    // @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "The resource has been retrieved"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA"),
            @ApiResponse(code = 404, message = "A resource with such URI could not be found")})
    @ApiOperation(value = "Returns the resource with the provided URI", notes = "List", response = String.class)
    public Response getAllResources(

            @ApiParam(value = "Resource type", required = true, allowMultiple = false, allowableValues = "papers,domains,users,informationsources,informationsourcesubscriptions,researchobjects,annotations,wikipediapages") @PathParam("RESOURCE_TYPE") String resourceType) {

        String resourceClass = ResourcesByTypeResource.resourceTypesTable.get(resourceType);
        if ((resourceClass != null)) {
            List<String> resource = this.core.getInformationHandler().getAll(resourceClass);

            if (resource != null) {
                return Response.ok(resource, MediaType.APPLICATION_JSON).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // --------------------------------------------------------------------------------

    @DELETE
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{RESOURCE_TYPE}/resource")
    @ApiOperation(value = "Removes a resource", notes = "")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "The resource with such URI has been deleted"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA"),
            @ApiResponse(code = 404, message = "A resource with such URI could not be found")})
    public Response deleteResource(
            @ApiParam(value = "Resource URI", required = true, allowMultiple = false) @QueryParam("uri") String URI,
            @ApiParam(value = "Resource type", required = true, allowMultiple = false, allowableValues = "papers,domains,users,informationsources,informationsourcesubscriptions,researchobjects,annotations,wikipediapages") @PathParam("RESOURCE_TYPE") String resourceType) {
        logger.info("DELETE: UIA uri> " + URI + " reourceType > " + resourceType);
        String resourceClass = ResourcesByTypeResource.resourceTypesTable.get(resourceType);
        if ((URI != null) && (resourceClass != null)) {
            this.core.getInformationHandler().remove(URI, resourceClass);
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
