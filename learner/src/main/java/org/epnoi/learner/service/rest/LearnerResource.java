package org.epnoi.learner.service.rest;

import io.swagger.annotations.*;
import org.epnoi.learner.filesystem.FilesystemHarvester;
import org.epnoi.learner.modules.Learner;
import org.epnoi.model.Domain;
import org.epnoi.model.Relation;
import org.epnoi.model.Term;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


@Component
@Path("/learner")
@Api(value = "/learner", description = "Operations for retrieving the learned relations from a domain")
public class LearnerResource {
    private static final Logger logger = Logger.getLogger(LearnerResource.class
            .getName());
    @Autowired
    private Learner learner;

    @Autowired
    private Core core;

    @Autowired
    private FilesystemHarvester harvester;


    @PostConstruct
    public void init() {
        logger.info("Starting the " + this.getClass());
    }


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/relations")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The relations  found in the domain has been successfully retrieved"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA"),
            @ApiResponse(code = 404, message = "A domain with such URI, or the relations for such domain,, could not be found")})
    @ApiOperation(value = "Returns the domain with the provided URI", notes = "", response = Domain.class)
    public Response getDomainRelations(
            @ApiParam(value = "Domain URI", required = true, allowMultiple = false) @QueryParam("uri") String uri) {


        //if (this.core.getInformationHandler().contains(uri, RDFHelper.DOMAIN_CLASS)) {
        //List<Relation> relations = new ArrayList<>(learner.retrieveRelations(uri).getRelations());
        if (true) {
            Relation relation = new Relation();
            relation.setUri("http://whateverrelation");
            relation.setSource("source");
            relation.setTarget("target");
            relation.addProvenanceSentence("esta es la frase", 0.2);

            List<Relation> relations = Arrays.asList(relation);
            GenericEntity<List<Relation>> entity = new GenericEntity<List<Relation>>(relations) {
            };
            return Response.status(Response.Status.OK).entity(entity).build();
        }


        return Response.status(Response.Status.NOT_FOUND).build();

    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/terms")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The relations  found in the domain has been successfully retrieved"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA"),
            @ApiResponse(code = 404, message = "A domain with such URI, or the relations for such domain,, could not be found")})
    @ApiOperation(value = "Returns the domain with the provided URI", notes = "", response = Domain.class)
    public Response getDomainTerminology(
            @ApiParam(value = "Domain URI", required = true, allowMultiple = false) @QueryParam("uri") String uri) {

        if (this.core.getInformationHandler().contains(uri, RDFHelper.DOMAIN_CLASS)) {
            List<Term> terms = new ArrayList<>(learner.retrieveTerminology(uri).getTerms());

            return Response.status(Response.Status.OK).entity(terms).build();
        }


        return Response.status(Response.Status.NOT_FOUND).build();

    }

    @POST
    @Path("/demo")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The relational sentences corpus has been created"),
            @ApiResponse(code = 500, message = "Something went wrong in the trainer module of the learner")})
    public Response createDemoData() {
//harvester.harvest("")
/*

        Parameters<Object> runtimeParameters = new Parameters<Object>();

        runtimeParameters.setParameter(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI, uri);
        runtimeParameters.setParameter(RelationalSentencesCorpusCreationParameters.MAX_TEXT_CORPUS_SIZE, textCorpusMaxSize);


        learner.getTrainer().createRelationalSentencesCorpus(runtimeParameters);
        URI createdResourceUri = null;
        if (runtimeParameters.getParameterValue(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI) != null) {

            createdResourceUri =
                    UriBuilder.fromUri((String) learner.getTrainer().getRuntimeParameters()
                            .getParameterValue(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI)).build();
        } else {
            createdResourceUri =
                    UriBuilder.fromUri((String) learner.getTrainer().getRelationalSentencesCorpusCreationParameters()
                            .getParameterValue(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI)).build();

        }
        return Response.created(createdResourceUri).build();
    */
        return Response.ok().build();
    }

}