package org.epnoi.rest.services;

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
import org.epnoi.model.Term;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.learner.OntologyLearningTask;
import org.epnoi.uia.learner.terms.TermsTable;

import com.sun.jersey.api.Responses;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/uia/domains/domain/terms")
@Api(value = "/uia/domains/domain/terms", description = "Operations for handling a domain terms")
public class DomainTerminologyResource extends UIAService {

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
			@ApiResponse(code = 200, message = "The terminology of the domain has been successfully retrieved"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA"),
			@ApiResponse(code = 404, message = "A domain with such URI,, or a terminology for such domain,, could not be found") })
	@ApiOperation(value = "Returns the domain with the provided URI", notes = "", response = Domain.class)
	public Response getDomainTerminology(
			@ApiParam(value = "Domain URI", required = true, allowMultiple = false) @QueryParam("uri") String uri) {

		logger.info("GET uri=" + uri);

		Domain domain = (Domain) core.getInformationHandler().get(uri,
				RDFHelper.DOMAIN_CLASS);

		if (domain != null) {
			OntologyLearningTask ontologyLearningTask = new OntologyLearningTask();
			ontologyLearningTask.perform(core, domain);
			TermsTable termsTable = ontologyLearningTask.getTermsTable();

			if (termsTable != null) {
				List<Term> terms =termsTable.getMostProbable(10);
				return Response.ok(terms, MediaType.APPLICATION_JSON)
						.build();
			}
		}
		return Response.status(Responses.NOT_FOUND).build();

	}

}
