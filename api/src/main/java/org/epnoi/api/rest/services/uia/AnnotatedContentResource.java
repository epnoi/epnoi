package org.epnoi.api.rest.services.uia;


import gate.Document;
import io.swagger.annotations.*;
import org.epnoi.model.Content;
import org.epnoi.model.Selector;
import org.epnoi.model.modules.Core;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Service
@Path("/uia/annotatedcontent/")
@Api(value = "/uia/annotatedcontent/", description = "Operations for handling the annotated content stored in the UIA")
public class AnnotatedContentResource {

    private static final Logger logger = Logger.getLogger(AnnotatedContentResource.class
            .getName());
    @Autowired
    private Core core;

    // ----------------------------------------------------------------------------------------
    @PostConstruct
    public void init() {
        logger.info("Starting the " + this.getClass());
    }

    // -----------------------------------------------------------------------------------------

    // --------------------------------------------------------------------------------------------

    @GET
    @Produces({MediaType.APPLICATION_XML})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The annotated content has been retrieved"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA"),
            @ApiResponse(code = 404, message = "The UIA has not been initialized")})
    @ApiOperation(value = "Returns the annotated content associated with the URI", notes = "", response = Document.class)

    public Response getAnnotatedContent(
            @ApiParam(value = "Annotated content uri", required = true, allowMultiple = false) @QueryParam("uri") String URI,
            @ApiParam(value = "Annotated content type", required = true, allowMultiple = false) @QueryParam("type") String type) {
        logger.info("GET: " + URI);


        Document annotatedDocument = null;
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
        return Response.status(Response.Status.NOT_FOUND).build();
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
	 * removeWikidataView(
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
	 * removeWikidataView(
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