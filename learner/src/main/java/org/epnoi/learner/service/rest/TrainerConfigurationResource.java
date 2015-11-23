package org.epnoi.learner.service.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.epnoi.learner.modules.Learner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Component
@Path("/trainer/configuration")
public class TrainerConfigurationResource {
    private static final Logger logger = Logger.getLogger(TrainerConfigurationResource.class
            .getName());
    @Autowired
    private Learner learner;


    @PostConstruct
    public void init() {
        logger.info("Starting the TrainerResources");
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The relations  found in the domain has been successfully retrieved"),
            @ApiResponse(code = 500, message = "Something went wrong in the learner")})
    @ApiOperation(value = "Returns the training configuration of the trainer module of the learner", notes = "", response = Map.class)
    public Response getConfiguration() {
        Map<String, Object> trainerConfiguration = new HashMap<String, Object>();
        trainerConfiguration.put("relationalPatternsModelCreationParameters", learner.getTrainer().getRelationalPatternsModelCreationParameters());
        trainerConfiguration.put("relationalSentencesCorpusCreationParamaters", learner.getTrainer().getRelationalSentencesCorpusCreationParamaters());
        return Response.status(Response.Status.OK).entity(trainerConfiguration).build();
    }
}