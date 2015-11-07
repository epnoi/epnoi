package org.epnoi.rest.services;

import java.util.*;
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

import com.sun.jersey.spi.resource.Singleton;
import org.epnoi.learner.relations.RelationsHandler;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.Resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@Path("/uia/knowledgebase")

@Api(value = "/uia/knowledgebase", description = "Knowledge base related operations")
@Singleton
public class KnowledgeBaseResource extends UIAService {
    private static final String RELATIONS_HANDLER = "RELATIONS_HANDLER";

    @Context
    ServletContext context;
    RelationsHandler relationsHandler = null;
    private static Map<String, String> resourceTypesTable = new HashMap<String, String>();
    private static Set<String> validRelationTypes = new HashSet<String>();

    static {
        resourceTypesTable.put("hypernymy", RelationHelper.HYPERNYM);
        resourceTypesTable.put("mereology", RelationHelper.MEREOLOGY);
        validRelationTypes.add("hypernymy");
        validRelationTypes.add("mereology");
    }

    // --------------------------------------------------------------------------------

    @PostConstruct
    public void init() {

        logger = Logger.getLogger(KnowledgeBaseResource.class.getName());
        logger.info("Initializing " + getClass());
        this.core = this.getUIACore();


    }

    // --------------------------------------------------------------------------------

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("relations/{RELATION_TYPE}")
    // @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "The resource has been retrieved"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA"),
            @ApiResponse(code = 404, message = "A resource with such URI could not be found")})
    @ApiOperation(value = "Returns the resource with the provided URI", notes = "", response = Resource.class)
    public Response getResource(
            @ApiParam(value = "Surface form of the source term of the relation", required = true, allowMultiple = false) @QueryParam("source") String source,
            @ApiParam(value = "Surface form of the target term of the relation", required = true, allowMultiple = false) @QueryParam("target") String target,
            @ApiParam(value = "Relation type", required = true, allowMultiple = false, allowableValues = "hypernymy,mereology") @PathParam("RELATION_TYPE") String type) {

        logger.info("GET:> source=" + source + " target=" + target);
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
        logger.info("hypernyms GET:> source=" + sources);

        if ((sources != null) && validRelationTypes.contains(type)) {

            type = resourceTypesTable.get(type);
            try {
                for (String source : sources) {
                    List<String> targets = new ArrayList<>(core.getKnowledgeBaseHandler().getKnowledgeBase().getHypernyms(source));
                    sourcesTargets.put(source, targets);
                }
                return Response.ok().entity(sourcesTargets).build();
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
    @Path("stem")

    @ApiResponses(value = {@ApiResponse(code = 200, message = "The resource has been retrieved"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA"),
            @ApiResponse(code = 404, message = "A resource with such URI could not be found")})
    @ApiOperation(value = "Returns the resource with the provided URI", notes = "", response = Resource.class)
    public Response getResource(
            @ApiParam(value = "Surface form of the term to be stemmed", required = true, allowMultiple =true) @QueryParam("term") List<String> terms) {

        logger.info("stem GET:> source=" + terms);
        if (terms != null) {
            Map<String, List<String>> termsStems = new HashMap<>();

            try {
                logger.info("As the parameters seemed ok");

                for(String term:terms) {
                    List<String> stemmedTerms = new ArrayList(core.getKnowledgeBaseHandler().getKnowledgeBase().stem(term));
                    termsStems.put(term, stemmedTerms);
                }
                return Response.ok().entity(termsStems).build();
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


}
