package org.epnoi.uia.rest.services;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.epnoi.uia.search.SearchContext;
import org.epnoi.uia.search.SearchResult;
import org.epnoi.uia.search.select.SelectExpression;

@Path("/UIA/searchs")
public class SearchsResource extends UIAService {

	@Context
	ServletContext context;

	// ----------------------------------------------------------------------------------------
	@PostConstruct
	public void init() {
		this.core = this.getUIACore();
	}

	// ----------------------------------------------------------

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("")
	public Response getSearchInJSON(@QueryParam("query") String query,
			@QueryParam("facet") List<String> facet,
			@QueryParam("filter") List<String> filter) {
		System.out.println("GET: query: "+query +" facets: " + facet + " filter: "
				+ filter);

		/*
		 * 
		 * SearchCassandraDAO searchCassandraDAO = new SearchCassandraDAO();
		 * searchCassandraDAO.init();
		 * 
		 * Search search = (Search)searchCassandraDAO.read(URI); for (Search
		 * s:searchCassandraDAO.getSearchs()){ System.out.println(s.getTitle()+
		 * "existe!");
		 * 
		 * }
		 * 
		 * //_initUIACore();
		 * 
		 * if (search != null) { return Response.ok(search,
		 * MediaType.APPLICATION_JSON).build(); }
		 */
		SelectExpression selectExpression = new SelectExpression();
		selectExpression.setSolrExpression(query);

		SearchContext searchContext = new SearchContext();
		for (String facetParameter : facet) {
			searchContext.getFacets().add(facetParameter);

		}
		
		for (String filterParameter : filter) {
			searchContext.getFilterQueries().add(filterParameter);

		}

		SearchResult searchResult = this.core.getSearchHandler().search(
				selectExpression, searchContext);
		System.out.println("Results:");
		System.out.println("#results " +searchResult.getResources().size());
		System.out.println("#facets " +searchResult.getFacets().size());
		
		if (searchResult != null) {
			return Response.ok(searchResult, MediaType.APPLICATION_JSON)
					.build();
		}

		return Response.status(404).build();
	}

}
