package org.epnoi.uia.rest.services;



import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.epnoi.model.Domain;
import org.epnoi.model.Relation;
import org.epnoi.model.RelationsTable;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.learner.OntologyLearningTask;

import com.sun.jersey.api.Responses;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/uia/domains/domain/relations")
@Api(value = "/uia/domains/domain/relations", description = "Operations for retrieving a domain relation")
public class DomainRelationsResource extends UIAService {

	@Context
	ServletContext context;

	// ----------------------------------------------------------------------------------------
	@PostConstruct
	public void init() {
		logger = Logger.getLogger(DomainResource.class.getName());
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
		return Response.status(Responses.NOT_FOUND).build();

	}

}
