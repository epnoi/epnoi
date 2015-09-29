package org.epnoi.rest.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.epnoi.model.RelationHelper;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.rest.services.response.WikidataViewSummary;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.knowledgebase.wikidata.WikidataHandlerParameters;
import org.epnoi.uia.knowledgebase.wikidata.WikidataHandlerParameters.DumpProcessingMode;
import org.epnoi.uia.knowledgebase.wikidata.WikidataView;
import org.epnoi.uia.knowledgebase.wikidata.WikidataViewCreator;

import com.sun.jersey.api.Responses;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/uia/knowledgebase/wikidataview")
@Api(value = "/uia/knowledgebase/wikidataview", description = "Operations for handling the wikidata view of the knowledge base")
public class WikidataViewResource extends UIAService {

	@Context
	ServletContext context;

	// ----------------------------------------------------------------------------------------
	@PostConstruct
	public void init() {
		logger = Logger.getLogger(WikidataViewResource.class.getName());
		logger.info("Initializing WikidataViewResource");
		this.core = this.getUIACore();

	}

	// -----------------------------------------------------------------------------------------

	@PUT
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Creates an new wikidataview", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "The wikidataview has been created"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA") })
	public Response createWikidataView(
			@ApiParam(value = "Wikidata view timeout", required = true, allowMultiple = false) @QueryParam("timeout") Integer timeout) {
		logger.info("PUT timout> " + timeout);

		URI wikidataViewURI = null;
		try {
			wikidataViewURI = new URI(WikidataHandlerParameters.DEFAULT_URI);
		} catch (URISyntaxException e) {
			throw new WebApplicationException();
		}

		WikidataHandlerParameters parameters = _createParameters(timeout);

		WikidataView wikidataView = _createWikidataView(parameters);

		_storeWikidataview(wikidataView);		
		
		return Response.created(wikidataViewURI).build();
	}

	// -----------------------------------------------------------------------------------------

	private void _storeWikidataview(WikidataView wikidataView) {
		this.core.getInformationHandler().remove(wikidataView.getURI(),
				RDFHelper.WIKIDATA_VIEW_CLASS);
		this.core.getInformationHandler().put(wikidataView,
				org.epnoi.model.Context.getEmptyContext());
	}

	// -----------------------------------------------------------------------------------------

	private WikidataView _createWikidataView(
			WikidataHandlerParameters parameters) {
		WikidataViewCreator wikidataViewCreator = new WikidataViewCreator();
		try {
			wikidataViewCreator.init(core, parameters);
		} catch (EpnoiInitializationException e) {

			e.printStackTrace();
		}
		return wikidataViewCreator.create();
	}

	// -----------------------------------------------------------------------------------------

	private WikidataHandlerParameters _createParameters(Integer timeout) {
		WikidataHandlerParameters parameters = new WikidataHandlerParameters();

		parameters.setParameter(WikidataHandlerParameters.WIKIDATA_VIEW_URI,
				WikidataHandlerParameters.DEFAULT_URI);
		parameters.setParameter(WikidataHandlerParameters.OFFLINE_MODE, true);
		parameters.setParameter(WikidataHandlerParameters.DUMP_FILE_MODE,
				DumpProcessingMode.JSON);
		parameters.setParameter(WikidataHandlerParameters.TIMEOUT, timeout);
		parameters.setParameter(WikidataHandlerParameters.DUMP_PATH,
				"/opt/epnoi/epnoideployment/wikidata");
		return parameters;
	}

	// --------------------------------------------------------------------------------------------

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The wikidata view has been retrieved"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA"),
			@ApiResponse(code = 404, message = "The UIA has not been initialized") })
	@ApiOperation(value = "Returns the wikidata view", notes = "", response = WikidataViewSummary.class)
	public Response getWikidataView() {
		logger.info("GET: ");

		long currentTime = System.currentTimeMillis();

		WikidataView wikidataView = (WikidataView) this.core
				.getInformationHandler().get(
						WikidataHandlerParameters.DEFAULT_URI,
						RDFHelper.WIKIDATA_VIEW_CLASS);

		System.out.println("It took "
				+ (System.currentTimeMillis() - currentTime)
				+ " to retrieve the wikidata view");

		currentTime = System.currentTimeMillis();
		wikidataView.count();
		System.out.println("It took "
				+ (System.currentTimeMillis() - currentTime)
				+ " to clean the wikidata view");

		if (wikidataView != null) {
			WikidataViewSummary wikidataViewSummary = _createWikidataViewSummary(wikidataView);
			return Response.ok(wikidataViewSummary).build();
		}
		return Response.status(Responses.NOT_FOUND).build();
	}

	// --------------------------------------------------------------------------------

	private WikidataViewSummary _createWikidataViewSummary(
			WikidataView wikidView) {
		WikidataViewSummary wikidataViewSummary = new WikidataViewSummary();
		wikidataViewSummary.setLabelsDictionarySize(new Long(wikidView
				.getLabelsDictionary().size()));
		wikidataViewSummary.setReverseLabelDictionarySize(new Long(wikidView
				.getLabelsReverseDictionary().size()));
		wikidataViewSummary.setNumberOfHypernymyRelations(_calculateHypernymsRelationsNumber(wikidView));

		return wikidataViewSummary;
	}
	
	// --------------------------------------------------------------------------------

	private long _calculateHypernymsRelationsNumber(WikidataView wikidView) {
		long number = 0;
		for (Entry<String, Set<String>> entry : wikidView.getRelations()
				.get(RelationHelper.HYPERNYM).entrySet()) {
			number += entry.getValue().size();
		}
		return number;
	}

	// --------------------------------------------------------------------------------

	@DELETE
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Removes a wikidata view", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The wikidata view with such URI has been deleted"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA"),
			@ApiResponse(code = 404, message = "A wikidata view with such URI could not be found") })
	public Response removeResearchObject(
			@ApiParam(value = "Wikidata view uri", required = true, allowMultiple = false) @QueryParam("uri") String URI) {
		logger.info("DELETE > " + URI);

		if (core.getInformationHandler().contains(URI,
				RDFHelper.WIKIDATA_VIEW_CLASS)) {
			this.core.getInformationHandler().remove(URI,
					RDFHelper.WIKIDATA_VIEW_CLASS);
			return Response.ok().build();
		} else {
			return Response.status(Responses.NOT_FOUND).build();
		}

	}

}