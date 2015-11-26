package org.epnoi.api.rest.services;

import io.swagger.annotations.*;
import org.epnoi.model.Domain;
import org.epnoi.model.rdf.RDFHelper;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;
@Deprecated
@Path("/uia/domains/domain/relations")
@Api(value = "/uia/domains/domain/relations", description = "Operations for retrieving a domain relation")
public class DomainRelationsResource extends UIAService {

	@Context
	ServletContext context;

	// ----------------------------------------------------------------------------------------
	@PostConstruct
	public void init() {
		logger = Logger.getLogger(DomainRelationsResource.class.getName());
		logger.info("Initializing DomainResource");
		this.core = this.getUIACore();

	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("")
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
/*
		if (domain != null) {
			OntologyLearningTask ontologyLearningTask = new OntologyLearningTask();
			ontologyLearningTask.perform(core, domain);
			RelationsTable relationsTable = ontologyLearningTask.getRelationsTable();

		
			if (relationsTable != null) {
				List<Relation> relations =relationsTable.getMostProbable(10);
				return Response.ok(relations, MediaType.APPLICATION_JSON)
						.build();
			}
			
		}
		*/
		return Response.status(Response.Status.NOT_FOUND).build();

	}

}
