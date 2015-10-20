package org.epnoi.rest.services;

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

import org.epnoi.model.search.SearchContext;
import org.epnoi.model.search.SearchResult;
import org.epnoi.model.search.SelectExpression;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/uia/searchs")
@Api(value = "/uia/searchs", description = "Operations about search")
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
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The Research Object has been retrieved"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA"),
			@ApiResponse(code = 404, message = "A Research Object with such URI could not be found") })
	@ApiOperation(value = "Returns the search result", notes = "", response = SearchResult.class)
	public Response getSearch(
			@ApiParam(value = "Search query expression", required = true, allowMultiple = false) @QueryParam("query") String query,
			@ApiParam(value = "Considered facet", required = false, allowMultiple = true) @QueryParam("facet") List<String> facet,
			@ApiParam(value = "Filters defined about the considered facets ", required = false, allowMultiple = true) @QueryParam("filter") List<String> filter) {
		System.out.println("GET: query: " + query + " facets: " + facet
				+ " filter: " + filter);

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
		if (facet != null) {
			for (String facetParameter : facet) {
				searchContext.getFacets().add(facetParameter);
			}
		}

		if (facet != null && filter != null) {
			for (String filterParameter : filter) {
				searchContext.getFilterQueries().add(filterParameter);

			}
		}
		SearchResult searchResult = this.core.getSearchHandler().search(
				selectExpression, searchContext);
		System.out.println("Results:");
		System.out.println("#results " + searchResult.getResources().size());
		System.out.println("#facets " + searchResult.getFacets().size());

		if (searchResult != null) {
			return Response.ok(searchResult, MediaType.APPLICATION_JSON)
					.build();
		}

		return Response.status(404).build();
	}

}
