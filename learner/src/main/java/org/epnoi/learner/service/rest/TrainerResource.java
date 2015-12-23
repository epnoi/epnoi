package org.epnoi.learner.service.rest;

import io.swagger.annotations.*;
import org.epnoi.learner.modules.Learner;
import org.epnoi.learner.relations.corpus.RelationalSentencesCorpusCreationParameters;
import org.epnoi.learner.relations.patterns.RelationalPatternsModelCreationParameters;
import org.epnoi.model.commons.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Component
@Path("/trainer")
@Api(value = "/trainer", description = "Operations for handling the learner trainer")

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
    @Path("/relationalSentencesCorpus")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Creates a relational sentences corpus", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The relational sentences corpus has been created"),
            @ApiResponse(code = 500, message = "Something went wrong in the trainer module of the learner")})
    public Response createRelationalSentenceCorpus(
            @ApiParam(value = "Maximum number of items in the textual corpus", required = false, allowMultiple = false) @QueryParam("textCorpusMaxSize") int textCorpusMaxSize,
            @ApiParam(value = "URI of the generated relational corpus", required = false, allowMultiple = false) @QueryParam("uri") String uri)

    {



        Parameters<Object> runtimeParameters = new Parameters<Object>();

        runtimeParameters.setParameter(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI, uri);
        runtimeParameters.setParameter(RelationalSentencesCorpusCreationParameters.MAX_TEXT_CORPUS_SIZE, textCorpusMaxSize);


        learner.getTrainer().createRelationalSentencesCorpus(runtimeParameters);
        URI createdResourceUri = null;
        if (runtimeParameters.getParameterValue(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI) != null) {

            createdResourceUri =
                    UriBuilder.fromUri((String) learner.getTrainer().getRuntimeParameters()
                            .getParameterValue(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI)).build();
        } else {
            createdResourceUri =
                    UriBuilder.fromUri((String) learner.getTrainer().getRelationalSentencesCorpusCreationParameters()
                            .getParameterValue(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI)).build();

        }
        return Response.created(createdResourceUri).build();
    }



    // -----------------------------------------------------------------------------------------

    @POST
    @Path("/patterns/lexical")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Creates a relational patterns model", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The relational patterns model has been created"),
            @ApiResponse(code = 500, message = "Something went wrong in the trainer module of the learner")})
    public Response createRelationalPatternsModel(@ApiParam(value = "", required = false, allowMultiple = false) @QueryParam("modelpath") String modelpath) {
        Parameters<Object> runtimeParameters = new Parameters<Object>();
        runtimeParameters.setParameter(RelationalPatternsModelCreationParameters.MODEL_PATH, modelpath);
        learner.getTrainer().createRelationalPatternsModel(runtimeParameters);

        URI uri =
                UriBuilder.fromUri((String) learner.getTrainer().getRelationalSentencesCorpusCreationParameters().getParameterValue(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI)).build();
        return Response.created(uri).build();
    }

    @POST
    @Path("/demo")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The relational sentences corpus has been created"),
            @ApiResponse(code = 500, message = "Something went wrong in the trainer module of the learner")})
    public Response createDemoData() {

/*

        Parameters<Object> runtimeParameters = new Parameters<Object>();

        runtimeParameters.setParameter(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI, uri);
        runtimeParameters.setParameter(RelationalSentencesCorpusCreationParameters.MAX_TEXT_CORPUS_SIZE, textCorpusMaxSize);


        learner.getTrainer().createRelationalSentencesCorpus(runtimeParameters);
        URI createdResourceUri = null;
        if (runtimeParameters.getParameterValue(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI) != null) {

            createdResourceUri =
                    UriBuilder.fromUri((String) learner.getTrainer().getRuntimeParameters()
                            .getParameterValue(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI)).build();
        } else {
            createdResourceUri =
                    UriBuilder.fromUri((String) learner.getTrainer().getRelationalSentencesCorpusCreationParameters()
                            .getParameterValue(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI)).build();

        }
        return Response.created(createdResourceUri).build();
    */
      return  Response.ok().build();
    }
}