package org.epnoi.rest.services;

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

import org.epnoi.model.Content;
import org.epnoi.model.Selector;
import org.epnoi.rest.services.response.WikidataViewSummary;
import org.epnoi.uia.informationstore.SelectorHelper;

import com.sun.jersey.api.Responses;

import gate.Document;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/uia/annotatedcontent/")
@Api(value = "/uia/annotatedcontent/", description = "Operations for handling the annotated content stored in the UIA")
public class AnnotatedContentResource extends UIAService {

	@Context
	ServletContext context;

	// ----------------------------------------------------------------------------------------
	@PostConstruct
	public void init() {
		logger = Logger.getLogger(AnnotatedContentResource.class.getName());
		logger.info("Initializing the AnnotatedContentResource!!");
		this.core = this.getUIACore();

	}

	// -----------------------------------------------------------------------------------------

	// --------------------------------------------------------------------------------------------

	@GET
	@Produces({ MediaType.APPLICATION_XML })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The annotated content has been retrieved"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA"),
			@ApiResponse(code = 404, message = "The UIA has not been initialized") })
	@ApiOperation(value = "Returns the annotated content associated with the URI", notes = "", response = Document.class)
	
	public Response getAnnotatedContent(
			@ApiParam(value = "Annotated content uri", required = true, allowMultiple = false) @QueryParam("uri") String URI,
			@ApiParam(value = "Annotated content type", required = true, allowMultiple = false) @QueryParam("type") String type) {
		logger.info("GET: "+URI);


		Document annotatedDocument=null;
		try {
			Selector selector = new Selector();
			selector.setProperty(SelectorHelper.ANNOTATED_CONTENT_URI, URI);
			selector.setProperty(SelectorHelper.TYPE, type);
			Content<Object> content = this.core.getInformationHandler().getAnnotatedContent(selector);
			annotatedDocument = (Document) content.getContent();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
		if (annotatedDocument != null) {
			
			return Response.ok(annotatedDocument.toXml()).build();
		}
		return Response.status(Responses.NOT_FOUND).build();
	}
	// --------------------------------------------------------------------------------
	/*
	 * @DELETE
	 * 
	 * @Path("")
	 * 
	 * @Consumes(MediaType.APPLICATION_JSON)
	 * 
	 * @ApiOperation(value = "Removes a wikidata view", notes = "")
	 * 
	 * @ApiResponses(value = {
	 * 
	 * @ApiResponse(code = 200, message =
	 * "The wikidata view with such URI has been deleted"),
	 * 
	 * @ApiResponse(code = 500, message = "Something went wrong in the UIA"),
	 * 
	 * @ApiResponse(code = 404, message =
	 * "A wikidata view with such URI could not be found") }) public Response
	 * removeResearchObject(
	 * 
	 * @ApiParam(value = "Wikidata view uri", required = true, allowMultiple =
	 * false) @QueryParam("uri") String URI) { logger.info("DELETE > " + URI);
	 * 
	 * if (core.getInformationHandler().contains(URI,
	 * RDFHelper.WIKIDATA_VIEW_CLASS)) {
	 * this.core.getInformationHandler().remove(URI,
	 * RDFHelper.WIKIDATA_VIEW_CLASS); return Response.ok().build(); } else {
	 * return Response.status(Responses.NOT_FOUND).build(); }
	 * 
	 * }
	 */

	// --------------------------------------------------------------------------------
	/*
	 * @DELETE
	 * 
	 * @Path("")
	 * 
	 * @Consumes(MediaType.APPLICATION_JSON)
	 * 
	 * @ApiOperation(value = "Removes a wikidata view", notes = "")
	 * 
	 * @ApiResponses(value = {
	 * 
	 * @ApiResponse(code = 200, message =
	 * "The wikidata view with such URI has been deleted"),
	 * 
	 * @ApiResponse(code = 500, message = "Something went wrong in the UIA"),
	 * 
	 * @ApiResponse(code = 404, message =
	 * "A wikidata view with such URI could not be found") }) public Response
	 * removeResearchObject(
	 * 
	 * @ApiParam(value = "Wikidata view uri", required = true, allowMultiple =
	 * false) @QueryParam("uri") String URI) { logger.info("DELETE > " + URI);
	 * 
	 * if (core.getInformationHandler().contains(URI,
	 * RDFHelper.WIKIDATA_VIEW_CLASS)) {
	 * this.core.getInformationHandler().remove(URI,
	 * RDFHelper.WIKIDATA_VIEW_CLASS); return Response.ok().build(); } else {
	 * return Response.status(Responses.NOT_FOUND).build(); }
	 * 
	 * }
	 */

}