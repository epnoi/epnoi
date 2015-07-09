package org.epnoi.uia.rest.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.epnoi.model.Annotation;
import org.epnoi.uia.demo.DemoDataLoader;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.knowledgebase.KnowledgeBase;
import org.epnoi.uia.rest.services.response.UIA;

import com.sun.jersey.api.Responses;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

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

		for (InformationStore informationStore : this.core
				.getInformationStores()) {
			org.epnoi.uia.rest.services.response.InformationStore informationStoreResponse = new org.epnoi.uia.rest.services.response.InformationStore();

			informationStoreResponse
					.setInformationStoreParameters(informationStore
							.getParameters());
			informationStoreResponse.setStatus(informationStore.test());
			uia.addInformationStores(informationStoreResponse);

		}

		DemoDataLoader demoDataLoader = new DemoDataLoader();
		demoDataLoader.init(core);
		demoDataLoader.load();

		if (uia != null) {
			return Response.ok(uia).build();
		}
		return Response.status(Responses.NOT_FOUND).build();
	}

	// --------------------------------------------------------------------------------

	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Initializes the UIA", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "The UIA successfully initialized"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA initialization") })
	public Response label() {
		logger.info("POST");

		logger.info("Inserting demo data");

		DemoDataLoader demoDataLoader = new DemoDataLoader();
		demoDataLoader.init(core);
		demoDataLoader.load();

		// We retrieve the knowledge base, since it is lazy
		logger.info("Retrieving the knowledge base");
		KnowledgeBase knowledgeBase = this.core.getKnowledgeBaseHandler()
				.getKnowledgeBase();

		return Response.ok().build();

	}

}
