package org.epnoi.api.rest.services.uia;


import io.swagger.annotations.*;
import org.epnoi.model.Domain;
import org.epnoi.model.DublinCoreMetadataElementsSetHelper;
import org.epnoi.model.ResearchObject;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
@Path("/uia/domains/domain")
@Api(value = "/uia/domains/domain", description = "Operations for handling a domain")
public class DomainResource {
    private static final Logger logger = Logger.getLogger(DomainResource.class
            .getName());

    @Autowired
    private Core core;

    private static Map<String, String> typesURIsResolutionTable = new HashMap<String, String>();

    static {
        typesURIsResolutionTable.put("paper", RDFHelper.PAPER_CLASS);
    }

    private static final String LABEL_PROPERTY = "label";
    private static final String EXPRESSION_PROPERTY = "expression";
    private static final String TYPE_PROPERTY = "type";

    private static final String resourcesPathSubfix = "/resources";

    @Context
    ServletContext context;

    // ----------------------------------------------------------------------------------------
    @PostConstruct
    public void init() {
        logger.info("Starting the " + this.getClass());
    }

    // -----------------------------------------------------------------------------------------

    @PUT
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Creates an empty domain", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The domain has been created"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA")})
    public Response createDomain(
            @ApiParam(value = "Domain URI", required = true, allowMultiple = false) @QueryParam("uri") String newDomainURI,
            @ApiParam(value = "Domain type", required = true, allowMultiple = false, allowableValues = "paper") @QueryParam("type") String newDomainType) {
        logger.info("PUT domain > " + newDomainURI + " type> " + newDomainType);

        URI domainURI = null;
        try {
            domainURI = new URI(newDomainURI);
        } catch (URISyntaxException e) {
            throw new WebApplicationException();
        }

        // We create the new empty domain, just with its URI and a reference to
        // the research object
        Domain domain = new Domain();
        domain.setUri(newDomainURI);
        domain.setType(typesURIsResolutionTable.get(newDomainType));
        domain.setResources(newDomainURI + resourcesPathSubfix);

        System.out.println("DOMAIN> " + domain);

        // We create an empty research object
        ResearchObject resources = new ResearchObject();
        resources.setUri(newDomainURI + resourcesPathSubfix);

        this.core.getInformationHandler().put(resources,
                org.epnoi.model.Context.getEmptyContext());

        this.core.getInformationHandler().put(domain,
                org.epnoi.model.Context.getEmptyContext());

        return Response.created(domainURI).build();
    }

    // -----------------------------------------------------------------------------------------

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The domain has been successfully retrieved"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA"),
            @ApiResponse(code = 404, message = "A domain with such URI could not be found")})
    @ApiOperation(value = "Returns the domain with the provided URI", notes = "", response = Domain.class)
    public Response getResearchObject(
            @ApiParam(value = "Domain URI", required = true, allowMultiple = false) @QueryParam("uri") String uri) {

        logger.info("GET uri=" + uri);

        Domain domain = (Domain) core.getInformationHandler().get(uri,
                RDFHelper.DOMAIN_CLASS);

        if (domain != null) {

            return Response.ok(domain, MediaType.APPLICATION_JSON).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();

    }

    // -----------------------------------------------------------------------------------------
/*MOVED OUT FROM THE API
    @GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/terms")
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
				List<Term> terms = termsTable.getMostProbable(10);

				return Response.ok(terms, MediaType.APPLICATION_JSON).build();
			}

		}
		return Response.status(Response.Status.NOT_FOUND).build();

	}
*/
    // -----------------------------------------------------------------------------------------

    @POST
    @Path("/resources")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Adds a resource to the domain", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The resource has been add to the domain"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA"),
            @ApiResponse(code = 404, message = "A domain with such URI could not be found")})
    public Response addAggregatedResource(
            @ApiParam(value = "Research Object uri", required = true, allowMultiple = false) @QueryParam("uri") String URI,
            @ApiParam(value = "Resource to be aggregated to the Research Object", required = true, allowMultiple = false) @QueryParam("resourceuri") String resourceURI) {
        logger.info("POST /resources uri" + URI + " resourceuri " + resourceURI);

        Domain domain = (Domain) core.getInformationHandler().get(URI,
                RDFHelper.DOMAIN_CLASS);
        System.out.println();
        System.out.println();
        System.out.println("DOMAIN >     " + domain);
        System.out.println();
        System.out.println();

        if (domain != null) {

            if (!core.getInformationHandler().contains(resourceURI,
                    domain.getType())) {
                core.getHarvestersHandler().harvestURL(resourceURI, domain);
            }

            ResearchObject researchObject = (ResearchObject) core
                    .getInformationHandler().get(URI + resourcesPathSubfix,
                            RDFHelper.RESEARCH_OBJECT_CLASS);

            if (researchObject != null) {
                if (!researchObject.getAggregatedResources().contains(
                        resourceURI)) {
                    researchObject.getAggregatedResources().add(resourceURI);
                    core.getInformationHandler().update(researchObject);
                }
            }
            domain = (Domain) core.getInformationHandler().get(URI,
                    RDFHelper.DOMAIN_CLASS);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }

    // -----------------------------------------------------------------------------------------

    @POST
    @Path("/properties/{PROPERTY}")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Sets a domain property", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The domain property has been updated"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA"),
            @ApiResponse(code = 404, message = "A domain with such URI, or a property with such could not be found")})
    public Response updateDCProperty(
            @ApiParam(value = "Domain URI", required = true, allowMultiple = false) @QueryParam("uri") String URI,
            @ApiParam(value = "Domain property name", required = true, allowMultiple = false, allowableValues = "label,type,expression,title,description,date,creator") @PathParam("PROPERTY") String propertyName,
            @ApiParam(value = "Domain property value", required = true, allowMultiple = false) @QueryParam("value") String value) {
        logger.info("POST /properties/" + propertyName + " domain " + URI
                + "value " + value);
        System.out.println("Updating the property "
                + DublinCoreMetadataElementsSetHelper
                .getPropertyURI(propertyName) + " with value " + value);

        String propertyURI = DublinCoreMetadataElementsSetHelper
                .getPropertyURI(propertyName);
        // If the propertyName is among considered dublin core properties, the
        // returned URI should not be null
        if (propertyURI != null) {
            // We are updating a dublin core property of the research object
            // that represents the domain resources
            _updateResourcesProperty(URI, propertyURI, value);

        } else {
            // We must be updating a property of the domain
            _updateDomainProperty(URI, propertyName, value);
        }

        return Response.ok().build();

    }

    // -----------------------------------------------------------------------------------------

    private void _updateResourcesProperty(String URI, String propertyURI,
                                          String value) {

        ResearchObject researchObject = (ResearchObject) core
                .getInformationHandler().get(URI + resourcesPathSubfix,
                        RDFHelper.RESEARCH_OBJECT_CLASS);
        if (researchObject != null) {
            researchObject.getDcProperties().addPropertyValue(propertyURI,
                    value);
            this.core.getInformationHandler().update(researchObject);
        }
    }

    // -----------------------------------------------------------------------------------------

    private void _updateDomainProperty(String URI, String propertyName,
                                       String value) {
        Domain domain = (Domain) core.getInformationHandler().get(URI,
                RDFHelper.DOMAIN_CLASS);
        if (domain != null) {

            switch (propertyName) {
                case DomainResource.EXPRESSION_PROPERTY:
                    domain.setExpression(value);
                    break;
                case DomainResource.LABEL_PROPERTY:
                    domain.setLabel(value);
                    break;
                case DomainResource.TYPE_PROPERTY:
                    domain.setType(value);
                    break;
                default:
            }
            this.core.getInformationHandler().update(domain);
        }
    }

    // -----------------------------------------------------------------------------------------

    @DELETE
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Removes Domain", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The domain with such URI has been deleted"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA"),
            @ApiResponse(code = 404, message = "A domain with such URI could not be found")})
    public Response removeResearchObject(
            @ApiParam(value = "Domain uri", required = true, allowMultiple = false) @QueryParam("uri") String URI) {
        logger.info("DELETE > " + URI);

        Domain domain = (Domain) core.getInformationHandler().get(URI,
                RDFHelper.DOMAIN_CLASS);
        if (domain != null) {
            this.core.getInformationHandler().remove(URI,
                    RDFHelper.DOMAIN_CLASS);
            // When we delete the domain object we also must remove the
            // associated research object
            this.core.getInformationHandler().remove(URI + resourcesPathSubfix,
                    RDFHelper.RESEARCH_OBJECT_CLASS);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }

    // -----------------------------------------------------------------------------------------

    @DELETE
    @Path("/resources")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Removes an aggregated resource from a Researh Object", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The resource has been deleted from the Domain"),
            @ApiResponse(code = 500, message = "Something went wrong in the UIA"),
            @ApiResponse(code = 404, message = "Either a Domain or the an aggregated resource with such URI could not be found")})
    public Response removeAggregatedResource(
            @ApiParam(value = "Domain URI", required = true, allowMultiple = false) @QueryParam("uri") String URI,
            @ApiParam(value = "Aggregated resource URI to be deleted", required = true, allowMultiple = false) @QueryParam("resourceuri") String resourceURI) {

        if (core.getInformationHandler().contains(URI, RDFHelper.DOMAIN_CLASS)) {

            ResearchObject researchObject = (ResearchObject) core
                    .getInformationHandler().get(URI + resourcesPathSubfix,
                            RDFHelper.RESEARCH_OBJECT_CLASS);
            if (researchObject != null
                    && researchObject.getAggregatedResources().contains(
                    resourceURI)) {
                researchObject.getAggregatedResources().remove(resourceURI);
                this.core.getInformationHandler().update(researchObject);
                return Response.ok().build();
            } else {

                return Response.status(Response.Status
                        .NOT_FOUND).build();
            }
        } else {

            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
