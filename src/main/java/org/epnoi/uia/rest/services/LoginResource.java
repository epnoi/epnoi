package org.epnoi.uia.rest.services;



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

import epnoi.model.Login;
import epnoi.model.User;
import epnoi.model.dao.cassandra.UserCassandraDAO;

@Path("/loginService")
public class LoginResource {
	

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
		System.out.println("POST: matcher/matchingsSpace");
		System.out
				.println("loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooging!!!!!!: "
						+ login.getUser() + "|" + login.getPassword());

		UserCassandraDAO userCassandraDAO = new UserCassandraDAO();
		userCassandraDAO.init();

		if (userCassandraDAO.existsUserWithName(login.getUser())) {
			System.out.println("The user exists and it was " + login.getUser());
			User user = userCassandraDAO.getUserWithName(login.getUser());
			return Response.ok(user, MediaType.APPLICATION_JSON).build();
		} else
			System.out.println("User does not exist");
			return Response.status(404).build();
	}
}
