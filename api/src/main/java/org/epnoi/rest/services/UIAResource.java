package org.epnoi.rest.services;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.epnoi.harvester.demo.DemoDataLoader;
import org.epnoi.model.modules.InformationStore;
import org.epnoi.rest.services.response.UIA;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("/uia")
@Api(value = "/uia", description = "UIA status and management")
public class UIAResource extends UIAService {

	@Context
	ServletContext context;

	// --------------------------------------------------------------------------------

	@PostConstruct
	public void init() {

		logger = Logger.getLogger(UIAResource.class.getName());
		logger.info("Initializing " + getClass());
		this.core = this.getUIACore();

	}

	// --------------------------------------------------------------------------------

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/status")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The UIA status has been retrieved"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA"),
			@ApiResponse(code = 404, message = "The UIA has not been initialized") })
	@ApiOperation(value = "Returns the UIA status", notes = "", response = UIA.class)
	public Response getUIA() {
		System.out.println("GET: UIA");

		UIA uia = new UIA();

		String timeStamp = Long.toString(System.currentTimeMillis());
		uia.setTimestamp(timeStamp);

		for (InformationStore informationStore : this.core.getInformationHandler()
				.getInformationStores()) {
			org.epnoi.rest.services.response.InformationStore informationStoreResponse = new org.epnoi.rest.services.response.InformationStore();

			informationStoreResponse
					.setInformationStoreParameters(informationStore
							.getParameters());
			informationStoreResponse.setStatus(informationStore.test());
			uia.addInformationStores(informationStoreResponse);

			org.epnoi.rest.services.response.KnowledgeBase knowledgeBase = _generateKnowledgeBaseStatus();
			uia.setKnowledgeBase(knowledgeBase);

		}

		if (uia != null) {
			return Response.ok(uia).build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	// --------------------------------------------------------------------------------
	
	private org.epnoi.rest.services.response.KnowledgeBase _generateKnowledgeBaseStatus() {
		org.epnoi.rest.services.response.KnowledgeBase knowledgeBase = new org.epnoi.rest.services.response.KnowledgeBase();
		knowledgeBase.setStatus(this.core.getKnowledgeBaseHandler()
				.isKnowledgeBaseInitialized());
/*
		for (Entry<String, Object> entry : this.core.g).getKnowledgeBaseParameters()
				.getParameters().entrySet()) {
			
			knowledgeBase.getParameters().put(entry.getKey(),
					entry.getValue().toString());
		}
	*/
		System.out.println("-------------------___> "+knowledgeBase);
		return knowledgeBase;
	}

	// --------------------------------------------------------------------------------

	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Initializes the UIA", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The UIA successfully initialized"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA initialization") })
	public Response label() {
		logger.info("POST");

		logger.info("Inserting demo data");

		DemoDataLoader demoDataLoader = new DemoDataLoader();
		demoDataLoader.init(core);
		demoDataLoader.load();


	
		
		logger.info("Retrieving the knowledge base");
	
		
		return Response.ok().build();

	}

}
