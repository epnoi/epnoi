package org.epnoi.learner.service.rest;

import io.swagger.annotations.*;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

@Component
@Path("/uia/resources/relations")
@Api(value = "/uia/resources/relations", description = "Operations for handling relations")
public class RelationsResource {
    private static final String RELATIONS_HANDLER = "RELATIONS_HANDLER";

    private static final Logger logger = Logger.getLogger(LearnerResource.class
            .getName());

    //	RelationsHandler relationsHandler = null;
    private static Map<String, String> resourceTypesTable = new HashMap<String, String>();
    private static Set<String> validRelationTypes = new HashSet<String>();

    static {
        resourceTypesTable.put("hypernymy", RelationHelper.HYPERNYMY);
        resourceTypesTable.put("mereology", RelationHelper.MEREOLOGY);
        validRelationTypes.add("hypernymy");
        validRelationTypes.add("mereology");
    }

    // --------------------------------------------------------------------------------

    @PostConstruct
    public void init() {
        logger.info("===========================================================>Initializing " + getClass());


        //this.relationsHandler = _buildRelationHandler();

    }

    // --------------------------------------------------------------------------------

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{RELATION_TYPE}")
    // @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The resource has been retrieved"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA"),
            @ApiResponse(code = 404, message = "A resource with such URI could not be found")})
    @ApiOperation(value = "Returns the resource with the provided URI", notes = "", response = Resource.class)
    public Response getResource(
            @ApiParam(value = "Surface form of the source term of the relation", required = true, allowMultiple = false) @QueryParam("source") String source,
            @ApiParam(value = "Surface form of the target term of the relation", required = true, allowMultiple = false) @QueryParam("target") String target,
            @ApiParam(value = "Relation type", required = true, allowMultiple = false, allowableValues = "hypernymy,mereology") @PathParam("RELATION_TYPE") String type,
            @ApiParam(value = "Considered domain for the relation", required = false, allowMultiple = false) @QueryParam("domain") String domain) {

        logger.info("GET:> source=" + source + " target=" + target + " type="
                + type + " domain" + domain);
        if ((source != null) && (target != null)
                && validRelationTypes.contains(type)) {

	/*
            type = resourceTypesTable.get(type);
			try {
				logger.info("As the parameters seemed ok we calculate the termhoood");

				Double relationhood = relationsHandler.areRelated(source,
						target, type, domain);

				System.out.println("------------------------> " + relationhood);

				return Response.ok().entity(relationhood).build();
			} catch (Exception exception) {
				exception.printStackTrace();
				logger.severe(exception.getMessage());
				return Response.serverError().build();
			}

*/
            //remove!!!!!!!!! just for compiling purpose
            return Response.status(400).build();
        } else {
            return Response.status(400).build();
        }
    }

    // --------------------------------------------------------------------------------
/*
	private synchronized RelationsHandler _buildRelationHandler() {

		RelationsHandler relationsHandler = (RelationsHandler) this.context
				.getAttribute(RELATIONS_HANDLER);
		if (relationsHandler == null) {
			relationsHandler = new RelationsHandler();

			RelationsHandlerParameters relationsHandlerParameters = new RelationsHandlerParameters();

			relationsHandlerParameters.setParameter(
					RelationsHandlerParameters.CONSIDERED_DOMAINS,
					new ArrayList<Domain>());

			try {

				relationsHandler.init(core, relationsHandlerParameters);

			} catch (EpnoiInitializationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			this.context.setAttribute(RELATIONS_HANDLER, relationsHandler);
		}
		return relationsHandler;
	}
*/
}
