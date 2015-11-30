package org.epnoi.api.rest.services.uia;


import io.swagger.annotations.*;
import org.epnoi.model.Annotation;
import org.epnoi.model.Resource;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.AnnotationRDFHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Logger;

@Service
@Path("/uia/annotations")
@Api(value = "/uia/annotations", description = "Operations about formally annotating resources")
public class AnnotationsResource {
    private static final Logger logger = Logger.getLogger(AnnotationsResource.class
            .getName());

    @Autowired
    private Core core;

    // ----------------------------------------------------------------------------------------
    @PostConstruct
    public void init() {
        logger.info("Starting the " + this.getClass());
    }

    // -----------------------------------------------------------------------------------------

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Something went wrong in the UIA"),
            @ApiResponse(code = 200, message = "The resource URI exists, its annotations URIs are returned"),
            @ApiResponse(code = 404, message = "A resource with such URI could not be found")})
    public Response getAnnotations(
            @ApiParam(value = "Resource URI", required = true, allowMultiple = false) @QueryParam("uri") String URI) {
        logger.info("GET Annotations> " + URI);
        Resource resource = this.core.getInformationHandler().get(URI);
        if (resource != null) {
            ArrayList<String> annotations = new ArrayList<String>(this.core
                    .getAnnotationHandler().getAnnotations(URI));

            return Response.status(Response.Status.OK).entity(annotations).build();
        } else {

            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }

    // -----------------------------------------------------------------------------------------

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/annotation")
    @ApiOperation(value = "Returns the label with the provided URI", notes = "", response = Annotation.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The annotation with such URI has been retrieved"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA"),
            @ApiResponse(code = 404, message = "An annotation with such URI could not be found")})
    public Response getAnnotation(
            @ApiParam(value = "URI of the label annotation ", required = true, allowMultiple = false) @QueryParam("uri") String uri) {

        System.out.println("GET Annotation> uri=" + uri);

        Annotation annotation = (Annotation) this.core.getInformationHandler()
                .get(uri, AnnotationRDFHelper.ANNOTATION_CLASS);

        if (annotation != null) {
            return Response.ok().entity(annotation).build();

        } else {

            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }

    // -----------------------------------------------------------------------------------------

    @POST
    @Path("/annotation")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Annotates the resource as being an instance of an ontology concept", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "An annotation has been created and used to annotate the resource"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA")})
    public Response annotate(
            @ApiParam(value = "URI of the resource to be labeled", required = true, allowMultiple = false) @QueryParam("uri") String URI,
            @ApiParam(value = "Ontology concept used to annotate the resource", required = true, allowMultiple = false) @QueryParam("annotationuri") String annotationURI) {
        logger.info(" ro " + URI + " annotationURI " + annotationURI);

        Annotation annotation = core.getAnnotationHandler().annotate(URI,
                annotationURI);

        URI annoURI = null;
        try {
            annoURI = new URI(annotation.getUri());
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return Response.created(annoURI).build();

    }

    // -----------------------------------------------------------------------------------------

    @DELETE
    @Path("/annotation")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Removes the annotation annotationuri from the resource uri", notes = "")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "OK")})
    public Response removeAnnotation(
            @ApiParam(value = "Resource uri", required = true, allowMultiple = false) @QueryParam("uri") String URI,
            @ApiParam(value = "Ontology concept URI that no longer annotates the resource", required = true, allowMultiple = false) @QueryParam("annotationuri") String annotationURI) {
        System.out.println("DELETE Annotation  > " + URI + " resource> " + URI);
        this.core.getAnnotationHandler().removeAnnotation(URI, annotationURI);
        return Response.ok().status(201).build();
    }

}
