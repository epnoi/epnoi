package org.epnoi.learner.service.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.epnoi.learner.modules.Learner;
import org.epnoi.learner.service.rest.rest.responses.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Component
@Path("/configuration")
public class LearnerResource{
    @Autowired
    private Learner learner;


    @PostConstruct
    public void init(){
        System.out.println("--------------------------EEEEEEEEEEEEEEEEEEEEEEEENTRAAAAAA!!!");
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The relations  found in the domain has been successfully retrieved"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA"),
            @ApiResponse(code = 404, message = "A domain with such URI, or the relations for such domain,, could not be found") })
    @ApiOperation(value = "Returns the domain with the provided URI", notes = "", response = Configuration.class)
    public Response getConfiguration(){
        System.out.println("ENTRA!!!!!!");
        return Response.status(Response.Status.OK).entity(learner.getParameters()).build();
    }
}