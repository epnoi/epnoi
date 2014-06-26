package org.epnoi.uia.client;

import javax.ws.rs.core.MultivaluedMap;

import org.epnoi.model.Search;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class InformationSourceTestClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String baseURL = "http://localhost:8081/epnoiUIA/rest";
		// http://localhost:8081/epnoiUIA/rest/searchsService?URI=algo

		ClientConfig config = new DefaultClientConfig();
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
				Boolean.TRUE);
		Client client = Client.create(config);

		WebResource service = client.resource(baseURL);

		ClientResponse response = null;
		// Create one todo

		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("URI", "Whatever!!! ");

		String URI = "URI:" + System.currentTimeMillis();
		System.out
				.println("Result(toString) "
						+ service
								.path("/searchsService")
								.queryParam("URI", "whatever")
								.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE)
								.get(ClientResponse.class)
								.getEntity(Search.class).getURI());

		/*
		 * String ID = "whatever";
		 * System.out.println("Creating the matchings space  " + ID);
		 * 
		 * System.out.println(service.path("/matcher/matchingsSpace/"+ID).type(javax
		 * .ws.rs.core.MediaType.APPLICATION_JSON) .put(ClientResponse.class));
		 * 
		 * 
		 * System.out.println("First update: " + update);
		 * service.path("/matcher/matchingsSpace/"+ID)
		 * .type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
		 * .post(ClientResponse.class, update); System.out.println("--");
		 * 
		 * System.out.println("Second update: " + secondUpdate);
		 * service.path("/matcher/matchingsSpace/"+ID)
		 * .type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
		 * .post(ClientResponse.class, secondUpdate);
		 * 
		 * 
		 * System.out.println("Result(toString) " +
		 * service.path("/matcher/matchingsSpace/"+ID)
		 * .accept(javax.ws.rs.core.MediaType.APPLICATION_JSON)
		 * .get(ClientResponse.class) .getEntity(MatchingsSpace.class));
		 * 
		 * secondUpdate.setType(Update.TYPE_REMOVAL);
		 * System.out.println("Removing the second update: " + secondUpdate);
		 * service.path("/matcher/matchingsSpace/"+ID)
		 * .type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
		 * .post(ClientResponse.class, secondUpdate);
		 * 
		 * secondUpdate.setType(Update.TYPE_ADDITION);
		 * System.out.println("Removing the second update: " + secondUpdate);
		 * service.path("/matcher/matchingsSpace/"+ID)
		 * .type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
		 * .post(ClientResponse.class, secondUpdate);
		 * 
		 * System.out.println("The matchings space ");
		 * System.out.println("Result(toString) " +
		 * service.path("/matcher/matchingsSpace/"+ID)
		 * .accept(javax.ws.rs.core.MediaType.APPLICATION_JSON)
		 * .get(ClientResponse.class) .getEntity(MatchingsSpace.class));
		 * 
		 * 
		 * 
		 * 
		 * service.path("/matcher/matchingsSpace/"+ID)
		 * .type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
		 * .delete(ClientResponse.class);
		 * 
		 * System.out.println("Trying to get matchingsSpace that has been deleted"
		 * ); System.out.println( service.path("/matcher/matchingsSpace/"+ID)
		 * .accept(javax.ws.rs.core.MediaType.APPLICATION_JSON)
		 * .get(ClientResponse.class) .getEntity(MatchingsSpace.class));
		 */

		System.out
				.println("exit-------------------------------------------------------");

		System.exit(0);

	}

}