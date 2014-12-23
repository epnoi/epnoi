package org.epnoi.uia.rest.services;

import javax.servlet.ServletContext;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.epnoi.model.User;
import org.epnoi.uia.informationstore.dao.rdf.UserRDFHelper;

@Path("/informationSources")
public class InformationSourceResource extends UIAService {

	@Context
	ServletContext context;

	// ----------------------------------------------------------------------------------------
	// ----------------------------------------------------------

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("")
	// @Consumes(MediaType.APPLICATION_JSON)
	public Response getSearchInJSON(
			@DefaultValue("none") @QueryParam("URI") String URI) {
		System.out.println("GET: " + URI);

		if (URI == null) {
			return Response.status(404).build();
		}

		this.core = getUIACore();
		User user = (User) this.core.getInformationHandler().get(URI,
				UserRDFHelper.USER_CLASS);
		if (user == null) {
			return Response.status(404).build();
		}
		System.out.println("--------------------------->"
				+ user.getInformationSourceSubscriptions());

		return Response.ok(user.getInformationSourceSubscriptions(),
				MediaType.APPLICATION_JSON).build();

	}
}
