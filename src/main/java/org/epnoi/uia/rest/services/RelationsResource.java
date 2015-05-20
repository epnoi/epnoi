package org.epnoi.uia.rest.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.epnoi.model.RelationHelper;
import org.epnoi.model.Resource;
import org.epnoi.uia.informationstore.dao.rdf.UserRDFHelper;
import org.epnoi.uia.learner.relations.RelationsHandler;

import com.sun.jersey.api.Responses;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/uia/resources/relations")
@Api(value = "/uia/resources/relations", description = "Operations for handling relations")
public class RelationsResource extends UIAService {

	@Context
	ServletContext context;
	RelationsHandler relationsHandler = null;
	private static Map<String, String> resourceTypesTable = new HashMap<String, String>();
	private static Set<String> validRelationTypes = new HashSet<String>();
	static {
		resourceTypesTable.put("hypernymy", RelationHelper.HYPERNYM);
		resourceTypesTable.put("mereology", UserRDFHelper.USER_CLASS);
		validRelationTypes.add("hypernymy");
		validRelationTypes.add("mereology");
	}

	// --------------------------------------------------------------------------------

	@PostConstruct
	public void init() {

		logger = Logger.getLogger(RelationsResource.class.getName());
		logger.info("Initializing " + getClass());
		this.core = this.getUIACore();

	}

	// --------------------------------------------------------------------------------

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{RELATION_TYPE}")
	// @Consumes(MediaType.APPLICATION_JSON)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The resource has been retrieved"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA"),
			@ApiResponse(code = 404, message = "A resource with such URI could not be found") })
	@ApiOperation(value = "Returns the resource with the provided URI", notes = "", response = Resource.class)
	public Response getResource(
			@ApiParam(value = "Surface form of the source term of the relation", required = true, allowMultiple = false) @QueryParam("sourceTermSurfaceForm") String sourceTermSurfaceForm,
			@ApiParam(value = "Surface form of the target term of the relation", required = true, allowMultiple = false) @QueryParam("targetTermSurfaceForm") String targetTermSurfaceForm,
			@ApiParam(value = "Relation type", required = true, allowMultiple = false, allowableValues = "hypernymy,mereology") @PathParam("RELATION_TYPE") String relationType,
			@ApiParam(value = "Considered domain for the relation", required = true, allowMultiple = false) @QueryParam("domain") String domainURI) {
		if ((sourceTermSurfaceForm != null) && (targetTermSurfaceForm != null)) {

			if (validRelationTypes.contains(relationType)) {

				Double relationhood = relationsHandler.areRelated(
						sourceTermSurfaceForm, targetTermSurfaceForm,
						relationType, domainURI);
				return Response.ok().entity(relationhood).build();

			} else {

			}

		} else {
			return Response.status(Responses.NOT_FOUND).build();
		}
		return null;
	}

	// --------------------------------------------------------------------------------

	private RelationsHandler _buildRelationHandler() {
		RelationsHandler relationsHandler = new RelationsHandler();
		return relationsHandler;
	}
}
