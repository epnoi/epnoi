package org.epnoi.rest.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.epnoi.model.InformationSourceNotification;
import org.epnoi.model.InformationSourceNotificationsSet;
import org.epnoi.model.InformationSourceSubscription;
import org.epnoi.model.Resource;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.rest.services.response.UIA;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.dao.rdf.AnnotationRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.InformationSourceRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.InformationSourceSubscriptionRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.UserRDFHelper;

import flexjson.JSONDeserializer;

@Path("/uiaflexjon")
public class UIAResourceFlexjon extends UIAService {

	@Context
	ServletContext context;

	Map<String, Class<? extends Resource>> knownDeserializableClasses = new HashMap<>();

	private static Map<String, String> resourceTypesTable = new HashMap<String, String>();
	static {
		resourceTypesTable.put("papers", RDFHelper.PAPER_CLASS);
		resourceTypesTable.put("user", UserRDFHelper.USER_CLASS);
		resourceTypesTable.put("informationsources",
				InformationSourceRDFHelper.INFORMATION_SOURCE_CLASS);
		resourceTypesTable
				.put("informationsourcesubscriptions",
						InformationSourceSubscriptionRDFHelper.INFORMATION_SOURCE_SUBSCRIPTION_CLASS);
		resourceTypesTable.put("researchobjects",
				RDFHelper.RESEARCH_OBJECT_CLASS);
		resourceTypesTable.put("annotations",
				AnnotationRDFHelper.ANNOTATION_CLASS);
	}

	// --------------------------------------------------------------------------------

	@PostConstruct
	public void init() {

		System.out.println("Initialicing the UIA service");

		knownDeserializableClasses.put("informationSourceSubscriptions",
				InformationSourceSubscription.class);

	}

	// --------------------------------------------------------------------------------

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/status")
	// @Consumes(MediaType.APPLICATION_JSON)
	public Response getUIA() {
		System.out.println("GET: UIA");

		UIA uia = new UIA();
		this.core = this.getUIACore();
		String timeStamp = Long.toString(System.currentTimeMillis());
		uia.setTimestamp(timeStamp);

		System.out.println("--->>>-->> " + this.core.getInformationStores());
		for (InformationStore informationStore : this.core
				.getInformationStores()) {
			org.epnoi.rest.services.response.InformationStore informationStoreResponse = new org.epnoi.rest.services.response.InformationStore();
			System.out.println("----> " + informationStore);
			informationStoreResponse
					.setInformationStoreParameters(informationStore
							.getParameters());
			informationStoreResponse.setStatus(informationStore.test());
			uia.addInformationStores(informationStoreResponse);

		}

		if (uia != null) {
			return Response.ok(uia, MediaType.APPLICATION_JSON).build();
		}
		return Response.status(404).build();
	}

	// --------------------------------------------------------------------------------

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/resources/{RESOURCE_TYPE}")
	public Response putResource(
			String jsonExpression,
			@DefaultValue("none") @PathParam("RESOURCE_TYPE") String resourceType) {
		System.out.println("POST: UIA");

		this.core = this.getUIACore();

		System.out
				.println("======================================================================================");
		System.out.println("This is the resourceType " + resourceType);
		System.out.println("This is the resource " + jsonExpression);

		Resource resource = new JSONDeserializer<Resource>().deserialize(
				jsonExpression, InformationSourceSubscription.class);
		System.out.println("What we have deserialized " + resource);

		System.out
				.println("----------------------------------------------------");
		Resource storedResource = this
				.getUIACore()
				.getInformationHandler()
				.get(resource.getURI(),
						InformationSourceSubscriptionRDFHelper.INFORMATION_SOURCE_SUBSCRIPTION_CLASS);

		System.out.println("lo que habia en el UIA " + storedResource);

		this.getUIACore().getInformationHandler().update(resource);

		storedResource = this
				.getUIACore()
				.getInformationHandler()
				.get(resource.getURI(),
						InformationSourceSubscriptionRDFHelper.INFORMATION_SOURCE_SUBSCRIPTION_CLASS);

		System.out.println("lo que ha quedaddo despues en el UIA "
				+ storedResource);
		if (jsonExpression != null) {
			return Response.ok(jsonExpression, MediaType.APPLICATION_JSON)
					.build();
		}
		return Response.status(404).build();
	}

	// --------------------------------------------------------------------------------

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/resources/{RESOURCE_TYPE}")
	// @Consumes(MediaType.APPLICATION_JSON)
	public Response getResource(@QueryParam("uri") String URI,
			@PathParam("RESOURCE_TYPE") String resourceType) {
		System.out.println("GET: UIA uri> " + URI + " reourceType > "
				+ resourceType);
/*
		String resourceClass = UIAResource.resourceTypesTable.get(resourceType);
		if ((URI != null) && (resourceClass != null)) {

			this.core = this.getUIACore();
			System.out.println("Getting the resource "
					+ UIAResource.resourceTypesTable.get(resourceType));
			Resource resource = this.core.getInformationAccess().get(URI,
					resourceClass);

			if (resource != null) {
				return Response.ok(resource, MediaType.APPLICATION_JSON)
						.build();
			}
		}
		*/
		return Response.status(404).build();
	}

	// --------------------------------------------------------------------------------

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/notifications")
	// @Consumes(MediaType.APPLICATION_JSON)
	public Response getResource(
			@DefaultValue("none") @QueryParam("URI") String URI) {
		System.out.println("GET: UIA");
		System.out.println("....entra");

		this.core = this.getUIACore();

		List<InformationSourceNotification> notifications = new ArrayList<InformationSourceNotification>();

		for (InformationSourceNotification notification : core
				.getInformationSourcesHandler().retrieveNotifications(URI)) {

			notifications.add(notification);
		}

		InformationSourceNotificationsSet notificationsSet = new InformationSourceNotificationsSet();

		notificationsSet.setNotifications(notifications);
		notificationsSet.setURI(URI);

		return Response.ok(notificationsSet, MediaType.APPLICATION_JSON)
				.build();
	}

	// --------------------------------------------------------------------------------
	/*
	 * String _translateResourceType(String resourceType) {
	 * 
	 * if (resourceType.equals("informationSources")) { return
	 * InformationSourceRDFHelper.INFORMATION_SOURCE_CLASS; } if
	 * (resourceType.equals("informationSourceSubscriptions")) { return
	 * InformationSourceSubscriptionRDFHelper
	 * .INFORMATION_SOURCE_SUBSCRIPTION_CLASS; } else if
	 * (resourceType.equals("users")) { return UserRDFHelper.USER_CLASS; }
	 * 
	 * return null; }
	 * 
	 * //
	 * ------------------------------------------------------------------------
	 * --------
	 * 
	 * Resource deserializeResource(String resourceExpression, String
	 * resourceType) { Resource resource = new
	 * JSONDeserializer<Resource>().deserialize( resourceExpression,
	 * this.knownDeserializableClasses.get(resourceType)); return resource; }
	 */
}
