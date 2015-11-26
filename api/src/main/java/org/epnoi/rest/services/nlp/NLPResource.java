package org.epnoi.rest.services.nlp;

import gate.Document;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

@Path("/uia/nlp")
@Api(value = "/uia/nlp", description = "Natural Language Processing service")
public class NLPResource extends UIAService {

	@Context
	ServletContext context;

	private static Map<String, String> resourceTypesTable = new HashMap<String, String>();
	private static Set<String> validRelationTypes = new HashSet<String>();

	// --------------------------------------------------------------------------------

	@PostConstruct
	public void init() {

		logger = Logger.getLogger(NLPResource.class.getName());
		logger.info("Initializing " + getClass());
		this.core = this.getUIACore();
	}

	// --------------------------------------------------------------------------------

	@GET
	@Produces({ MediaType.APPLICATION_XML })
	@Path("/process")
	// @Consumes(MediaType.APPLICATION_JSON)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The annotated resource has been created"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA") })
	@ApiOperation(value = "Returns the document with the annotated resource", notes = "", response = Resource.class)
	public Response annotate(@QueryParam("content") String content) {

		logger.info("GET "+content);

		try {
			
			Document document = this.core.getNLPHandler().process(content);

			return Response.ok().entity(document.toXml()).build();
		} catch (Exception exception) {
			exception.printStackTrace();
			logger.severe(exception.getMessage());
			return Response.serverError().build();
		}
	}

}
