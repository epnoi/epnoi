package org.epnoi.rest.services.uia;


import io.swagger.annotations.*;
import org.epnoi.model.Resource;
import org.epnoi.rest.services.UIAService;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Path("/uia/resources/bylabel")
@Api(value = "/uia/resources/bylabel", description = "Operations for retrieving resources by label")
public class ResourcesByLabelResource extends UIAService {

	@Context
	ServletContext context;

	Map<String, Class<? extends Resource>> knownDeserializableClasses = new HashMap<>();

	// --------------------------------------------------------------------------------

	@PostConstruct
	public void init() {

		logger = Logger.getLogger(ResourcesByLabelResource.class.getName());
		logger.info("Initializing " + getClass());
		this.core = this.getUIACore();

	}

	// --------------------------------------------------------------------------------

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/")
	// @Consumes(MediaType.APPLICATION_JSON)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The list of resource has been retrieved"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA"),
			@ApiResponse(code = 404, message = "Resource with such label could not be found") })
	@ApiOperation(value = "Returns the resources annotated using the provided label", notes = "", responseContainer = "List", response = Resource.class)
	public Response getResource(
			@ApiParam(value = "Human readeable label", required = true, allowMultiple = false) @QueryParam("label") String label) {
		logger.info("GET: label" + label);

		if ((label != null)) {

			this.core = this.getUIACore();

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
