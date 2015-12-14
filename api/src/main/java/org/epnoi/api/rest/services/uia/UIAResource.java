package org.epnoi.api.rest.services.uia;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.epnoi.api.rest.services.response.KnowledgeBase;
import org.epnoi.api.rest.services.response.UIA;
import org.epnoi.model.modules.Core;
import org.epnoi.model.modules.InformationStore;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

//import org.epnoi.harvester.demo.DemoDataLoader;

@Path("/uia")
@Api(value = "/uia", description = "UIA status and management")
public class UIAResource {

    private static final Logger logger = Logger.getLogger(UIAResource.class
            .getName());

    @Autowired
    private Core core;

    // --------------------------------------------------------------------------------

    @PostConstruct
    public void init() {
        logger.info("Starting " + getClass());
    }

    // --------------------------------------------------------------------------------

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/status")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The UIA status has been retrieved"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA"),
            @ApiResponse(code = 404, message = "The UIA has not been initialized")})
    @ApiOperation(value = "Returns the UIA status", notes = "", response = UIA.class)
    public Response getUIA() {
        System.out.println("GET: UIA");

        UIA uia = new UIA();

        String timeStamp = Long.toString(System.currentTimeMillis());
        uia.setTimestamp(timeStamp);

        for (InformationStore informationStore : this.core.getInformationHandler()
                .getInformationStores()) {
            org.epnoi.api.rest.services.response.InformationStore informationStoreResponse = new org.epnoi.api.rest.services.response.InformationStore();

            informationStoreResponse
                    .setInformationStoreParameters(informationStore
                            .getParameters());
            informationStoreResponse.setStatus(informationStore.test());
            uia.addInformationStores(informationStoreResponse);

            KnowledgeBase knowledgeBase = _generateKnowledgeBaseStatus();
            uia.setKnowledgeBase(knowledgeBase);

        }

        if (uia != null) {
            return Response.ok(uia).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // --------------------------------------------------------------------------------

    private KnowledgeBase _generateKnowledgeBaseStatus() {
        KnowledgeBase knowledgeBase = new org.epnoi.api.rest.services.response.KnowledgeBase();
        knowledgeBase.setStatus(this.core.getKnowledgeBaseHandler()
                .isKnowledgeBaseInitialized());
/*
        for (Entry<String, Object> entry : this.core.g).getKnowledgeBaseParameters()
				.getParameters().entrySet()) {
			
			knowledgeBase.getParameters().put(entry.getKey(),
					entry.getValue().toString());
		}
	*/
        System.out.println("-------------------___> " + knowledgeBase);
        return knowledgeBase;
    }

    // --------------------------------------------------------------------------------

    @POST
    @Path("/init")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Initializes the UIA", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The UIA successfully initialized"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA initialization")})
    public Response label() {
        logger.info("POST");

        logger.info("Inserting demo data");
/*
        DemoDataLoader demoDataLoader = new DemoDataLoader();
        demoDataLoader.init(core);
        demoDataLoader.load();
*/

        logger.info("Retrieving the knowledge base");


        return Response.ok().build();

    }

}
