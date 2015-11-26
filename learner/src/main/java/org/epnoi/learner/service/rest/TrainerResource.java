package org.epnoi.learner.service.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.epnoi.learner.modules.Learner;
import org.epnoi.learner.relations.corpus.RelationalSentencesCorpusCreationParameters;
import org.epnoi.model.Domain;
import org.epnoi.model.ResearchObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Component
@Path("/trainer")
public class TrainerResource {
    private static final Logger logger = Logger.getLogger(TrainerResource.class
            .getName());
    @Autowired
    private Learner learner;


    @PostConstruct
    public void init() {
        logger.info("Starting the " + this.getClass());
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The relations  found in the domain has been successfully retrieved"),
            @ApiResponse(code = 500, message = "Something went wrong in the learner")})
    @ApiOperation(value = "Returns the training configuration of the learner", notes = "", response = Map.class)
    public Response getConfiguration() {
        Map<String, Object> trainerConfiguration = new HashMap<String, Object>();
        trainerConfiguration.put("relationalPatternsModelCreationParameters", learner.getTrainer().getRelationalPatternsModelCreationParameters());
        trainerConfiguration.put("relationalSentencesCorpusCreationParamaters", learner.getTrainer().getRelationalSentencesCorpusCreationParameters());
        return Response.status(Response.Status.OK).entity(trainerConfiguration).build();
    }

    @POST
    @Path("relationalSentencesCorpus")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Creates a relational sentences corpus", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The relational sentences corpus has been created"),
            @ApiResponse(code = 500, message = "Something went wrong in the trainer module of the learner")})
    public Response createRelationalSentenceCorpus() {
        learner.getTrainer().createRelationalSentencesCorpus();

        URI uri =
                UriBuilder.fromUri((String) learner.getTrainer().getRelationalSentencesCorpusCreationParameters().getParameterValue(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER)).build();
        return Response.created(uri).build();
    }

    // -----------------------------------------------------------------------------------------

    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Creates a relational patterns model", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The relational patterns model has been created"),
            @ApiResponse(code = 500, message = "Something went wrong in the trainer module of the learner")})
    public Response createRelationalPatternsModel() {
        learner.getTrainer().createRelationalPatternsModel();

        URI uri =
                UriBuilder.fromUri((String) learner.getTrainer().getRelationalSentencesCorpusCreationParameters().getParameterValue(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER)).build();
        return Response.created(uri).build();
    }
}