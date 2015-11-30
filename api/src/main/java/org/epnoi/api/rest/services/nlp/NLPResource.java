package org.epnoi.api.rest.services.nlp;

import gate.Document;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
@Service
@Path("/uia/nlp")
@Api(value = "/uia/nlp", description = "Natural Language Processing service")
public class NLPResource {

	private static final Logger logger = Logger.getLogger(NLPResource.class
			.getName());

	@Autowired
	private Core core;

	private static Map<String, String> resourceTypesTable = new HashMap<String, String>();
	private static Set<String> validRelationTypes = new HashSet<String>();

	// --------------------------------------------------------------------------------

	@PostConstruct
	public void init() {
		logger.info("Initializing " + getClass());
	}

	// --------------------------------------------------------------------------------

	@GET
	@Produces({ MediaType.APPLICATION_XML })
	@Path("/process")
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
