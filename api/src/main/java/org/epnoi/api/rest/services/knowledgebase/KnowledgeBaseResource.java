package org.epnoi.api.rest.services.knowledgebase;


import io.swagger.annotations.*;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.Resource;
import org.epnoi.model.modules.Core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.logging.Logger;

@Service
@Path("/uia/knowledgebase")

@Api(value = "/uia/knowledgebase", description = "Knowledge base related operations")

public class KnowledgeBaseResource {

    @Autowired
    private Core core;

    private static final Logger logger = Logger.getLogger(KnowledgeBaseResource.class
            .getName());

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
        logger.info("Starting the " + getClass());
    }

    // --------------------------------------------------------------------------------

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("relations/{RELATION_TYPE}")

    @ApiResponses(value = {@ApiResponse(code = 200, message = "The resource has been retrieved"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA"),
            @ApiResponse(code = 404, message = "A resource with such URI could not be found")})
    @ApiOperation(value = "Returns the resource with the provided URI", notes = "", response = Resource.class)
    public Response getResource(
            @ApiParam(value = "Surface form of the source term of the relation", required = true, allowMultiple = false) @QueryParam("source") String source,
            @ApiParam(value = "Surface form of the target term of the relation", required = true, allowMultiple = false) @QueryParam("target") String target,
            @ApiParam(value = "Relation type", required = true, allowMultiple = false, allowableValues = "hypernymy,mereology") @PathParam("RELATION_TYPE") String type) {


        if ((source != null) && (target != null) && validRelationTypes.contains(type)) {

            type = resourceTypesTable.get(type);
            try {
                logger.info("As the parameters seemed ok we calculate the termhoood");
                boolean areRelated = core.getKnowledgeBaseHandler().getKnowledgeBase().areRelated(source, target, type);

                return Response.ok().entity(areRelated).build();
            } catch (Exception exception) {
                exception.printStackTrace();
                logger.severe(exception.getMessage());
                return Response.serverError().build();
            }

        } else {
            return Response.status(400).build();
        }
    }

    // --------------------------------------------------------------------------------

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("relations/{RELATION_TYPE}/targets")
    // @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "The resource has been retrieved"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA"),
            @ApiResponse(code = 404, message = "A resource with such URI could not be found")})
    @ApiOperation(value = "Returns the resource with the provided URI", notes = "", response = Resource.class)
    public Response getResource(
            @ApiParam(value = "Surface form of the source term of the relation", required = true, allowMultiple = true) @QueryParam("source") List<String> sources,
            @ApiParam(value = "Relation type", required = true, allowMultiple = false, allowableValues = "hypernymy,mereology") @PathParam("RELATION_TYPE") String type) {
        Map<String, List<String>> sourcesTargets = new HashMap<>();
        if (sources != null) {

            if (validRelationTypes.contains(type)) {

                type = resourceTypesTable.get(type);

                for (String source : sources) {
                    try {
                        List<String> targets = new ArrayList<>(core.getKnowledgeBaseHandler().getKnowledgeBase().getHypernyms(source));
                        sourcesTargets.put(source, targets);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        logger.severe(exception.getMessage());
                        sourcesTargets.put(source, new ArrayList<>());
                    }
                }
                return Response.ok().entity(sourcesTargets).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }
        return Response.ok().entity(sourcesTargets).build();
    }

    // --------------------------------------------------------------------------------

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("stem")

    @ApiResponses(value = {@ApiResponse(code = 200, message = "The resource has been retrieved"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA"),
            @ApiResponse(code = 404, message = "A resource with such URI could not be found")})
    @ApiOperation(value = "Returns the resource with the provided URI", notes = "", response = Resource.class)
    public Response getResource(
            @ApiParam(value = "Surface form of the term to be stemmed", required = true, allowMultiple = true) @QueryParam("term") List<String> terms) {
        Map<String, List<String>> termsStems = new HashMap<>();
        logger.info("stem GET:> source=" + terms);
        if (terms != null) {
            for (String term : terms) {
                try {
                    List<String> stemmedTerms = new ArrayList(core.getKnowledgeBaseHandler().getKnowledgeBase().stem(term));
                    termsStems.put(term, stemmedTerms);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    logger.severe(exception.getMessage());
                    termsStems.put(term, new ArrayList<>());
                }
            }
            return Response.ok().entity(termsStems).build();


        }
        return Response.ok().entity(termsStems).build();
    }

    // --------------------------------------------------------------------------------


}
