package org.epnoi.learner.service.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.epnoi.learner.LearningParameters;
import org.epnoi.learner.modules.Learner;
import org.epnoi.model.commons.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


@Component
@Path("/configuration")
@Api(value = "/configuration", description = "Operation for handling the learner configuration")
public class LearnerConfigurationResource {
    private static final Logger logger = Logger.getLogger(LearnerConfigurationResource.class
            .getName());
    @Autowired
    private Learner learner;


    @PostConstruct
    public void init() {
        logger.info("Starting the "+this.getClass());
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The relations  found in the domain has been successfully retrieved"),
            @ApiResponse(code = 500, message = "Something went wrong in the learner")})
    @ApiOperation(value = "Returns the learner configuration", notes = "", response = LearningParameters.class)
    public Response getConfiguration() {/*
        Map<String, Map<String,String>> trainerConfiguration = new HashMap<>();
        trainerConfiguration.put("learningParameters", learner.getTrainer().getRelationalPatternsModelCreationParameters().getParametersMap());
        GenericEntity<Map<String, Map<String,String>>> entity = new GenericEntity<Map<String,Map<String,String>>>(trainerConfiguration) {
        };
*/
        Map<String, String> trainerConfiguration = new HashMap<>();

        GenericEntity<Map<String,String>> entity = new GenericEntity<Map<String,String>>(this.learner.getParameters().getParametersMap()) {
        };
        return Response.status(Response.Status.OK).entity(entity).build();
    }
}