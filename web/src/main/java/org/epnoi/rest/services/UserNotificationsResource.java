package org.epnoi.rest.services;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.epnoi.model.InformationSourceNotification;
import org.epnoi.model.InformationSourceNotificationsSet;
import org.epnoi.model.User;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.UserRDFHelper;

@Path("/users/{USER_ID}/notifications/informationSources")
public class UserNotificationsResource extends UIAService {
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("")
	// /notifications/informationSources
	// @Consumes(MediaType.APPLICATION_JSON)
	public Response getUserNotifications(
			@DefaultValue("none") @PathParam("USER_ID") String USER_ID) {
		System.out.println("GET: " + USER_ID);

		String URI = "http://www.epnoi.org/users/" + USER_ID;
		
		String informationSourceSubscriptionURI = "http://www.epnoi.org/users/"
				+ USER_ID + "/subscriptions/informationSources";
		
		System.out.println("---> " + URI);

		if (URI == null) {
			return Response.status(404).build();
		}

		Core core = getUIACore();
		User user = (User) core.getInformationHandler().get(URI,
				UserRDFHelper.USER_CLASS);

		if (user == null) {
			return Response.status(404).build();
		}

		System.out.println(user + "--------------------------->"
				+ user.getInformationSourceSubscriptions());
		List<InformationSourceNotification> notifications = new ArrayList<InformationSourceNotification>();
		for (String informationSourceURI : user
				.getInformationSourceSubscriptions()) {

			for (InformationSourceNotification notification : core
					.getInformationSourcesHandler().retrieveNotifications(
							informationSourceURI)) {

				notifications.add(notification);
			}
		}
		
		InformationSourceNotificationsSet notificationsSet = new InformationSourceNotificationsSet();
		
		notificationsSet.setNotifications(notifications);
		notificationsSet.setURI(informationSourceSubscriptionURI);
		
		
		return Response.ok(notificationsSet, MediaType.APPLICATION_JSON).build();

	}

	//---------------------------------------------------------------------------------------
	
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
		User user = (User) core.getInformationHandler().get(userURI,
				UserRDFHelper.USER_CLASS);

		if (user == null) {
			return Response.status(404).build();
		}

		if (user.getInformationSourceSubscriptions().contains(
				informationSourceSubscriptionURI)) {

			List<InformationSourceNotification> notifications = core
					.getInformationSourcesHandler().retrieveNotifications(
							informationSourceSubscriptionURI);

			InformationSourceNotificationsSet notificationsSet = new InformationSourceNotificationsSet();
			notificationsSet.setNotifications(notifications);
			notificationsSet.setURI(informationSourceSubscriptionURI);
			
			
			return Response.ok(notificationsSet, MediaType.APPLICATION_JSON)
					.build();

		}
		return Response.status(404).build();
	}

}
