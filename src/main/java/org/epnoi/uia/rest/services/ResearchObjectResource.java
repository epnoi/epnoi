package org.epnoi.uia.rest.services;

import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.epnoi.model.DublinCoreMetadataElementsSetHelper;
import org.epnoi.model.ResearchObject;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.rest.services.response.jsonld.JSONLDResearchObjectResponseBuilder;
import org.epnoi.uia.rest.services.response.jsonld.JSONLDResponse;

@Path("/uia/researchobjects/researchobject")
public class ResearchObjectResource extends UIAService {

	@Context
	ServletContext context;

	// ----------------------------------------------------------------------------------------
	@PostConstruct
	public void init() {
		logger = Logger.getLogger(ResearchObjectResource.class.getName());
		logger.info("Initializing ResearchObjectResource");
		this.core = this.getUIACore();

	}

	// -----------------------------------------------------------------------------------------

	@PUT
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createResearchObject(ResearchObject researchObject) {
		logger.info("PUT RO> " + researchObject);

		this.core.getInformationAccess().put(researchObject,
				org.epnoi.model.Context.emptyContext);

		String result = "Adding " + researchObject;
		return Response.status(201).entity(result).build();
	}

	// -----------------------------------------------------------------------------------------

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("")
	public Response getResearchObject(@QueryParam("uri") String uri,
			@QueryParam("format") String format) {

		System.out.println("GET RO> uri=" + uri + " format=" + format);

		ResearchObject researchObject = (ResearchObject) core
				.getInformationAccess().get(uri,
						RDFHelper.RESEARCH_OBJECT_CLASS);

		/*
		 * ResearchObject researchObject = new ResearchObject();
		 * researchObject.setURI("http://testResearchObject");
		 * researchObject.getAggregatedResources().add("http://resourceA");
		 * researchObject.getAggregatedResources().add("http://resourceB");
		 * researchObject.getDcProperties().addPropertyValue(
		 * DublinCoreRDFHelper.TITLE_PROPERTY,
		 * "First RO, loquetienesquebuscar");
		 * researchObject.getDcProperties().addPropertyValue(
		 * DublinCoreRDFHelper.DESCRIPTION_PROPERTY,
		 * "Description of the first RO");
		 * researchObject.getDcProperties().addPropertyValue(
		 * DublinCoreRDFHelper.DATE_PROPERTY, "2005-02-28T00:00:00Z");
		 */
		if (researchObject != null) {

			if ("jsonld".equals(format)) {
				JSONLDResponse researchObjectResponse = JSONLDResearchObjectResponseBuilder
						.build(researchObject);
				Map<String, Object> responseMap = researchObjectResponse
						.convertToMap();

				return Response.ok(responseMap, MediaType.APPLICATION_JSON)
						.build();
			} else {
				return Response.ok(researchObject, MediaType.APPLICATION_JSON)
						.build();
			}
		} else {

			return Response.status(404).build();
		}
	}

	// -----------------------------------------------------------------------------------------

	@POST
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateResearchObject(ResearchObject researchObject) {
		logger.info("POST RO> " + researchObject);

		ResearchObject researchObjectToBeUpdated = (ResearchObject) core
				.getInformationAccess().get(researchObject.getURI(),
						RDFHelper.RESEARCH_OBJECT_CLASS);

		if (researchObjectToBeUpdated != null) {
			core.getInformationAccess().update(researchObject);
			return Response.ok("{}", MediaType.APPLICATION_JSON).status(200)
					.build();
		} else {
			return Response.status(404).build();
		}

	}

	// -----------------------------------------------------------------------------------------

	@POST
	@Path("/aggregation")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addAggregatedResource(@QueryParam("uri") String URI,
			@QueryParam("resourceuri") String resourceURI) {
		System.out.println(" ro " + URI + " aggr " + resourceURI);

		ResearchObject researchObject = (ResearchObject) core
				.getInformationAccess().get(URI,
						RDFHelper.RESEARCH_OBJECT_CLASS);

		if (researchObject != null) {
			if (!researchObject.getAggregatedResources().contains(resourceURI)) {
				researchObject.getAggregatedResources().add(resourceURI);
				core.getInformationAccess().update(researchObject);
			}

		} else {
			return Response.status(404).build();
		}

		return Response.ok("{}", MediaType.APPLICATION_JSON).status(200)
				.build();

	}

	// -----------------------------------------------------------------------------------------

	@POST
	@Path("/dc/{PROPERTY}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateDCProperty(@QueryParam("uri") String URI,
			@QueryParam("value") String value,
			@PathParam("PROPERTY") String propertyName) {
		System.out.println(" ro " + URI + " property " + propertyName +"value> "+value);
		System.out.println("Updating the property "
				+ DublinCoreMetadataElementsSetHelper
						.getPropertyURI(propertyName) + " with value " + value);
		
		
		
		ResearchObject researchObject = (ResearchObject) core
				.getInformationAccess().get(URI,
						RDFHelper.RESEARCH_OBJECT_CLASS);
		String propertyURI = DublinCoreMetadataElementsSetHelper
				.getPropertyURI(propertyName);
		if (researchObject != null && propertyURI != null) {

			researchObject.getDcProperties().addPropertyValue(propertyURI,
					value);
			core.getInformationAccess().update(researchObject);
		} else {
			return Response.status(404).build();
		}

		return Response.ok("{}", MediaType.APPLICATION_JSON).status(200)
				.build();

	}

	// -----------------------------------------------------------------------------------------

	@DELETE
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeResearchObject(@QueryParam("uri") String URI) {
		System.out.println("DELETE RO  > " + URI + " resource> " + URI);

		ResearchObject researchObject = (ResearchObject) core
				.getInformationAccess().get(URI,
						RDFHelper.RESEARCH_OBJECT_CLASS);
		if (researchObject != null) {
			this.core.getInformationAccess().remove(URI,
					RDFHelper.RESEARCH_OBJECT_CLASS);

			return Response.ok().status(200).build();

		} else {

			return Response.status(404).build();
		}

	}

	// -----------------------------------------------------------------------------------------

	@DELETE
	@Path("/aggregation")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeAggregatedResource(@QueryParam("uri") String URI,
			@QueryParam("resourceuri") String resourceURI) {
		ResearchObject researchObject = (ResearchObject) core
				.getInformationAccess().get(URI,
						RDFHelper.RESEARCH_OBJECT_CLASS);
		if (researchObject != null
				&& researchObject.getAggregatedResources()
						.contains(resourceURI)) {

			researchObject.getAggregatedResources().remove(resourceURI);

			this.core.getInformationAccess().update(researchObject);

			return Response.ok().status(200).build();

		} else {

			return Response.status(404).build();
		}

	}

}
