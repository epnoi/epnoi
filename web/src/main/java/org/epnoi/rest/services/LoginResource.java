package org.epnoi.rest.services;



import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.epnoi.model.Login;
import org.epnoi.model.User;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationstore.dao.cassandra.UserCassandraDAO;
import org.epnoi.uia.informationstore.dao.rdf.UserRDFHelper;

@Path("/loginService")
public class LoginResource extends UIAService{
	

	@Context
	ServletContext context;

	// --------------------------------------------------------------------------------
	
	@GET
	@Path("/login/{ID}")
	// @Consumes(MediaType.APPLICATION_JSON)
	public Response createMatchingSpace(
			@DefaultValue("none") @PathParam("ID") String ID) {
		System.out.println("PUT: login/" + ID);
		System.out.println("Entrar entra!");
		return Response.ok().build();
	}

	// --------------------------------------------------------------------------------

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateMatchingSpaceInJSON(Login login) {
		System.out.println("POST: login");
		System.out
				.println("loging: "
						+ login.getUser() + "|" + login.getPassword());

		UserCassandraDAO userCassandraDAO = new UserCassandraDAO();
		userCassandraDAO.init();

		if (userCassandraDAO.existsUserWithName(login.getUser())) {
			System.out.println("The user exists and it was " + login.getUser());
		
			User cassandraUser = userCassandraDAO.getUserWithName(login.getUser());
			System.out.println("The user exists and it was uried " + cassandraUser.getURI());
		
			Core core = getUIACore();
			User user = (User) core.getInformationHandler().get(cassandraUser.getURI(),
					UserRDFHelper.USER_CLASS);
			if (user == null) {
				return Response.status(404).build();
			}
			
			return Response.ok(user,
					MediaType.APPLICATION_JSON).build();
			
			
		} else
			System.out.println("User does not exist");
			return Response.status(404).build();
	}
}
