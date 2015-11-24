package org.epnoi.learner.service.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.epnoi.learner.LearningParameters;
import org.epnoi.learner.OntologyLearningTask;
import org.epnoi.learner.modules.Learner;
import org.epnoi.model.Domain;
import org.epnoi.model.Relation;
import org.epnoi.model.RelationsTable;
import org.epnoi.model.rdf.RDFHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;


@Component
@Path("/")
public class LearnerResource {
    private static final Logger logger = Logger.getLogger(LearnerResource.class
            .getName());
    @Autowired
    private Learner learner;


    @PostConstruct
    public void init() {
        logger.info("Starting the "+this.getClass());
    }



        @GET
        @Produces({ MediaType.APPLICATION_JSON })
        @Path("terminology")
        @ApiResponses(value = {
                @ApiResponse(code = 200, message = "The relations  found in the domain has been successfully retrieved"),
                @ApiResponse(code = 500, message = "Something went wrong in the UIA"),
                @ApiResponse(code = 404, message = "A domain with such URI, or the relations for such domain,, could not be found") })
        @ApiOperation(value = "Returns the domain with the provided URI", notes = "", response = Domain.class)
        public Response getDomainTerminology(
                @ApiParam(value = "Domain URI", required = true, allowMultiple = false) @QueryParam("uri") String uri) {

            logger.info("GET uri=" + uri);


            return Response.status(Response.Status.NOT_FOUND).build();

        }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("relations")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The relations  found in the domain has been successfully retrieved"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA"),
            @ApiResponse(code = 404, message = "A domain with such URI, or the relations for such domain,, could not be found") })
    @ApiOperation(value = "Returns the domain with the provided URI", notes = "", response = Domain.class)
    public Response getDomainTerminology(
            @ApiParam(value = "Domain URI", required = true, allowMultiple = false) @QueryParam("uri") String uri) {

        logger.info("GET uri=" + uri);

        Domain domain = (Domain) core.getInformationHandler().get(uri,
                RDFHelper.DOMAIN_CLASS);

        if (domain != null) {
            learner.learn(domain);
            OntologyLearningTask ontologyLearningTask = new OntologyLearningTask();
            ontologyLearningTask.perform(core, domain);
            RelationsTable relationsTable = ontologyLearningTask.getRelationsTable();


            if (relationsTable != null) {
                List<Relation> relations =relationsTable.getMostProbable(10);
                return Response.ok(relations, MediaType.APPLICATION_JSON)
                        .build();
            }

        }
        return Response.status(Response.Status.NOT_FOUND).build();

    }


}