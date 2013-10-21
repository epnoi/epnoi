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

import epnoi.model.Search;

@Path("/searchsService")
public class SearchsResource {
	public static final String MATCHER_ATTRIBUTE = "SEMANTIC_MATCHER";

	@Context
	ServletContext context;

	// --------------------------------------------------------------------------------
	// ----------------------------------------------------------

	@GET
	@Produces( { MediaType.APPLICATION_JSON })
	@Path("")
	// @Consumes(MediaType.APPLICATION_JSON)
	public Response getSearchInJSON(
			@DefaultValue("none") @QueryParam("URI") String URI) {
		System.out.println("GET: " + URI);

		/*
		SearchCassandraDAO searchCassandraDAO = new SearchCassandraDAO();
		searchCassandraDAO.init();
		
		Search search = searchCassandraDAO.read(URI);
		for (Search s:searchCassandraDAO.getSearchs()){
			System.out.println(s.getTitle()+ "existe!");
			
		}
		*/
		Search search = new Search();
		search.setURI(URI);
		search.setDescription("Whatever at "+ System.currentTimeMillis());
		if (search != null) {
			return Response.ok(search, MediaType.APPLICATION_JSON).build();
		}
		return Response.status(404).build();
	}

	
}
