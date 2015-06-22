package org.epnoi.uia.rest.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.epnoi.model.Domain;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.Resource;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.uia.informationstore.dao.rdf.UserRDFHelper;
import org.epnoi.uia.learner.knowledgebase.KnowledgeBaseParameters;
import org.epnoi.uia.learner.knowledgebase.wikidata.WikidataHandlerParameters;
import org.epnoi.uia.learner.knowledgebase.wikidata.WikidataHandlerParameters.DumpProcessingMode;
import org.epnoi.uia.learner.knowledgebase.wordnet.WordNetHandlerParameters;
import org.epnoi.uia.learner.relations.RelationsHandler;
import org.epnoi.uia.learner.relations.RelationsHandlerParameters;

import com.sun.jersey.api.Responses;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/uia/resources/relations")
@Api(value = "/uia/resources/relations", description = "Operations for handling relations")
public class RelationsResource extends UIAService {
	private static final String RELATIONS_HANDLER = "RELATIONS_HANDLER";

	@Context
	ServletContext context;
	RelationsHandler relationsHandler = null;
	private static Map<String, String> resourceTypesTable = new HashMap<String, String>();
	private static Set<String> validRelationTypes = new HashSet<String>();
	static {
		resourceTypesTable.put("hypernymy", RelationHelper.HYPERNYM);
		resourceTypesTable.put("mereology", UserRDFHelper.USER_CLASS);
		validRelationTypes.add("hypernymy");
		validRelationTypes.add("mereology");
	}

	// --------------------------------------------------------------------------------

	@PostConstruct
	public void init() {

		logger = Logger.getLogger(RelationsResource.class.getName());
		logger.info("Initializing " + getClass());
		this.core = this.getUIACore();

		this.relationsHandler = _buildRelationHandler();

	}

	// --------------------------------------------------------------------------------

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{RELATION_TYPE}")
	// @Consumes(MediaType.APPLICATION_JSON)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The resource has been retrieved"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA"),
			@ApiResponse(code = 404, message = "A resource with such URI could not be found") })
	@ApiOperation(value = "Returns the resource with the provided URI", notes = "", response = Resource.class)
	public Response getResource(
			@ApiParam(value = "Surface form of the source term of the relation", required = true, allowMultiple = false) @QueryParam("source") String source,
			@ApiParam(value = "Surface form of the target term of the relation", required = true, allowMultiple = false) @QueryParam("target") String target,
			@ApiParam(value = "Relation type", required = true, allowMultiple = false, allowableValues = "hypernymy,mereology") @PathParam("RELATION_TYPE") String type,
			@ApiParam(value = "Considered domain for the relation", required = true, allowMultiple = false) @QueryParam("domain") String domain) {

		logger.info("GET:> source=" + source + " target=" + target + " type="
				+ type + " domain" + domain);
		if ((source != null) && (target != null)
				&& validRelationTypes.contains(type)) {

			try {
				logger.info("As the parameters seemed ok we calculate the termhoood");

				Double relationhood = relationsHandler.areRelated(source,
						target, type, domain);
				
				System.out.println("------------------------> "+relationhood);

				return Response.ok().entity(relationhood).build();
			} catch (Exception exception) {
				exception.printStackTrace();
				logger.severe(exception.getMessage());
				return Response.serverError().build();
			}

		} else {
			return Response.status(400).build();
		}
	}

	// --------------------------------------------------------------------------------

	private synchronized RelationsHandler _buildRelationHandler() {

		RelationsHandler relationsHandler = (RelationsHandler) this.context
				.getAttribute(RELATIONS_HANDLER);
		if (relationsHandler == null) {
			relationsHandler = new RelationsHandler();
			KnowledgeBaseParameters knowledgeBaseParameters = new KnowledgeBaseParameters();
			WikidataHandlerParameters wikidataParameters = new WikidataHandlerParameters();

			WordNetHandlerParameters wordnetParameters = new WordNetHandlerParameters();
			wordnetParameters.setParameter(
					WordNetHandlerParameters.DICTIONARY_LOCATION,
					"/epnoi/epnoideployment/wordnet/dictWN3.1/");

			wikidataParameters.setParameter(
					WikidataHandlerParameters.WIKIDATA_VIEW_URI,
					"http://wikidataView");
			wikidataParameters.setParameter(
					WikidataHandlerParameters.STORE_WIKIDATA_VIEW, false);
			wikidataParameters.setParameter(
					WikidataHandlerParameters.RETRIEVE_WIKIDATA_VIEW, true);
			wikidataParameters.setParameter(
					WikidataHandlerParameters.RETRIEVE_WIKIDATA_VIEW, true);
			wikidataParameters.setParameter(
					WikidataHandlerParameters.OFFLINE_MODE, true);
			wikidataParameters.setParameter(
					WikidataHandlerParameters.DUMP_FILE_MODE,
					DumpProcessingMode.JSON);
			wikidataParameters.setParameter(WikidataHandlerParameters.TIMEOUT,
					10);
			wikidataParameters.setParameter(
					WikidataHandlerParameters.DUMP_PATH,
					"/Users/rafita/Documents/workspace/wikidataParsingTest");

			knowledgeBaseParameters.setParameter(
					KnowledgeBaseParameters.WORDNET_PARAMETERS,
					wordnetParameters);

			knowledgeBaseParameters.setParameter(
					KnowledgeBaseParameters.WIKIDATA_PARAMETERS,
					wikidataParameters);

			RelationsHandlerParameters relationsHandlerParameters = new RelationsHandlerParameters();

			relationsHandlerParameters.setParameter(
					RelationsHandlerParameters.KNOWLEDGE_BASE_PARAMETERS,
					knowledgeBaseParameters);

			relationsHandlerParameters.setParameter(
					RelationsHandlerParameters.CONSIDERED_DOMAINS,
					new ArrayList<Domain>());

			try {

				relationsHandler.init(core, relationsHandlerParameters);

			} catch (EpnoiInitializationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			this.context.setAttribute(RELATIONS_HANDLER, relationsHandler);
		}
		return relationsHandler;
	}
}
