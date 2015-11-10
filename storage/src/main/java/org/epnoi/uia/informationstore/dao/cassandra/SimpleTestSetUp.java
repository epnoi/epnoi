package org.epnoi.uia.informationstore.dao.cassandra;

import org.epnoi.model.Context;
import org.epnoi.model.Search;
import org.epnoi.model.User;

import java.util.Arrays;


public class SimpleTestSetUp {
	public static void main(String[] args) {

		System.out.println("Starting Test SetUp");
		System.out
				.println("Initialization --------------------------------------------");
		UserCassandraDAO userCassandraDAO = new UserCassandraDAO();
		SearchCassandraDAO searchCassandraDAO = new SearchCassandraDAO();
		userCassandraDAO.init();

		System.out.println(" --------------------------------------------");
		System.out.println("-- " + searchCassandraDAO.getSearchs());
		for (User user : userCassandraDAO.getUsers()) {
			System.out.println(user.getName() + " existe!");
			userCassandraDAO.remove(user.getUri());
		}
		Context context = new Context();
		

		// Let's create the users
		User user = new User();
		user.setUri("http://userRafa");
		user.setName("Rafa");
		user.setPassword("PasswordDeRafa");
		user.addSearch("http://searchA");
		user.addSearch("http://searchB");
		userCassandraDAO.create(user, context);

		User userElOtro = new User();
		userElOtro.setUri("http://userSara");
		userElOtro.setName("Sara");
		userElOtro.setPassword("PasswordDeSara");
		userElOtro.addSearch("http://searchC");
		userElOtro.addSearch("http://searchD");
		userCassandraDAO.create(userElOtro, context);

		User pique = new User();
		pique.setUri("http://userPique");
		pique.setName("Pique");
		pique.setPassword("PasswordDePique");
		pique.addSearch("http://searchE");
		pique.addSearch("http://searchF");
		userCassandraDAO.create(pique, context);

		User readUser = (User)userCassandraDAO.read("http://userRafa");
		System.out.println("readed user> " + readUser);

		User otherReadUser =(User) userCassandraDAO.read("http://userSara");
		System.out.println("readed user> " + otherReadUser);
		System.out.println("Exiting test");
		// Let's create the searchs

		for (Search search : searchCassandraDAO.getSearchs()) {
			System.out.println(search.getTitle() + "existe!");
			searchCassandraDAO.remove(search.getUri());
		}

		for (String label : Arrays.asList("A", "B", "C", "D", "E", "F")) {
			String searchURI = "http://search" + label;
			Search search = new Search();
			search.setUri(searchURI);
			search.setDescription("Search" + label + " Description");
			search.setTitle("Search" + label);
			/*
			 * for (String expressionLabel : Arrays.asList("A", "B", "C",
			 * "D","E","F")) { search.addExpression("expression" +
			 * expressionLabel); }
			 */

			if (search.getUri().equals("http://searchE")) {
				search.addExpression("galaxy");
			} else {

				for (String expressionLabel : Arrays.asList("A", "B", "C", "D",
						"E", "F")) {
					search.addExpression("expression" + expressionLabel);
				}

			}
			searchCassandraDAO.create(search, context);
		}

		Search piqueSearch = (Search)searchCassandraDAO.read("http://searchE");
		piqueSearch.addExpression("galaxy");
	}
}
