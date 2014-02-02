package org.epnoi.uia.rest.services;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationstore.dao.rdf.InformationSourceSubscriptionRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.UserRDFHelper;

import epnoi.model.InformationSourceNotification;
import epnoi.model.InformationSourceSubscription;
import epnoi.model.User;

@Path("/users/{USER_ID}/subscriptions/informationSources")
public class UsersInformationSourcesResource extends UIAService {

	// ---------------------------------------------------------------------------------------
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("")
	// /notifications/informationSources
	// @Consumes(MediaType.APPLICATION_JSON)
	public Response getUserNotifications(
			@DefaultValue("none") @PathParam("USER_ID") String USER_ID) {
		System.out.println("GET: " + USER_ID);

		String URI = "http://www.epnoi.org/users/" + USER_ID;
		System.out.println("---> " + URI);

		if (URI == null) {
			return Response.status(404).build();
		}

		Core core = getUIACore();
		User user = (User) core.getInformationAccess().get(URI,
				UserRDFHelper.USER_CLASS);

		if (user == null) {
			return Response.status(404).build();
		}

		System.out.println(user + "--------------------------->"
				+ user.getInformationSourceSubscriptions());
		List<InformationSourceSubscription> informationSourceSubscriptions = new ArrayList<InformationSourceSubscription>();
		for (String informationSourceURI : user
				.getInformationSourceSubscriptions()) {

			InformationSourceSubscription informationSourceSubscription = (InformationSourceSubscription) this.core
					.getInformationAccess()
					.get(informationSourceURI,
							InformationSourceSubscriptionRDFHelper.INFORMATION_SOURCE_SUBSCRIPTION_CLASS);
			informationSourceSubscriptions.add(informationSourceSubscription);
		}
		return Response.ok(informationSourceSubscriptions,
				MediaType.APPLICATION_JSON).build();

	}

	// ---------------------------------------------------------------------------------------

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{INFORMATION_SOURCE_ID}")
	// /notifications/informationSources
	// @Consumes(MediaType.APPLICATION_JSON)
	public Response getUserNotificationsForInformationSource(
			@DefaultValue("none") @PathParam("USER_ID") String USER_ID,
			@DefaultValue("none") @PathParam("INFORMATION_SOURCE_ID") String INFORMATION_SOURCE_ID) {
		System.out.println("GET: " + USER_ID + INFORMATION_SOURCE_ID);

		String userURI = "http://www.epnoi.org/users/" + USER_ID;
		String informationSourceSubscriptionURI = "http://www.epnoi.org/users/"
				+ USER_ID + "/subscriptions/informationSources/"
				+ INFORMATION_SOURCE_ID;
		// "http://www.epnoi.org/users/testUser/subscriptions/informationSources/slashdot"
		System.out.println("---> " + userURI);

		if (userURI == null) {
			return Response.status(404).build();
		}

		Core core = getUIACore();
		User user = (User) core.getInformationAccess().get(userURI,
				UserRDFHelper.USER_CLASS);

		if (user == null) {
			return Response.status(404).build();
		}

		if (user.getInformationSourceSubscriptions().contains(
				informationSourceSubscriptionURI)) {

			InformationSourceSubscription informationSourceSubcription = (InformationSourceSubscription) core
					.getInformationAccess()
					.get(informationSourceSubscriptionURI,
							InformationSourceSubscriptionRDFHelper.INFORMATION_SOURCE_SUBSCRIPTION_CLASS);

			return Response.ok(informationSourceSubcription,
					MediaType.APPLICATION_JSON).build();

		}
		return Response.status(404).build();
	}

}
