package org.epnoi.api.rest.services.uia;


import io.swagger.annotations.*;
import org.epnoi.model.Resource;
import org.epnoi.model.modules.Core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@Service
@Path("/uia/resources/bylabel")
@Api(value = "/uia/resources/bylabel", description = "Operations for retrieving resources by label")
public class ResourcesByLabelResource {
    private static final Logger logger = Logger.getLogger(ResourcesByLabelResource.class
            .getName());

    @Autowired
    private Core core;


    // --------------------------------------------------------------------------------

    @PostConstruct
    public void init() {
        logger.info("Starting the " + getClass());
    }

    // --------------------------------------------------------------------------------

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/")
    // @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The list of resource has been retrieved"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA"),
            @ApiResponse(code = 404, message = "Resource with such label could not be found")})
    @ApiOperation(value = "Returns the resources annotated using the provided label", notes = "", responseContainer = "List", response = Resource.class)
    public Response getResource(
            @ApiParam(value = "Human readeable label", required = true, allowMultiple = false) @QueryParam("label") String label) {
        logger.info("GET: label" + label);

        if ((label != null)) {

            List<String> resources = this.core.getAnnotationHandler()
                    .getLabeledAs(label);
            if (resources.size() > 0) {
                return Response.ok(resources, MediaType.APPLICATION_JSON)
                        .build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
