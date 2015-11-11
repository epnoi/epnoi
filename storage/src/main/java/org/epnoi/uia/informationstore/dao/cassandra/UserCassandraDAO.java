package org.epnoi.uia.informationstore.dao.cassandra;

import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.Row;
import org.epnoi.model.*;

import java.util.ArrayList;
import java.util.List;

public class UserCassandraDAO extends CassandraDAO {

	public void remove(String URI) {
		super.deleteRow(URI, UserCassandraHelper.COLUMN_FAMILY);
	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		
		User user = (User) resource;
		super.createRow(user.getUri(), UserCassandraHelper.COLUMN_FAMILY);

		if (user.getName() != null) {

			super.updateColumn(user.getUri(), UserCassandraHelper.NAME,
					user.getName(), UserCassandraHelper.COLUMN_FAMILY);

		}

		if (user.getPassword() != null) {
			super.updateColumn(user.getUri(), UserCassandraHelper.PASSWORD,
					user.getPassword(), UserCassandraHelper.COLUMN_FAMILY);

		}

		for (String searchURI : user.getSearchs()) {
			super.updateColumn(user.getUri(), searchURI,
					UserCassandraHelper.SEARCHS,
					UserCassandraHelper.COLUMN_FAMILY);
		}

	}

	// --------------------------------------------------------------------------------

	public Resource read(Selector selector) {
		
		return new ExternalResource();
	}

	// --------------------------------------------------------------------------------

	public Resource read(String URI) {
		
		
	
		ColumnSliceIterator<String, String, String> columnsIterator = super
				.getAllCollumns(URI, UserCassandraHelper.COLUMN_FAMILY);
		if (columnsIterator.hasNext()) {
			User user = new User();
			user.setUri(URI);
			while (columnsIterator.hasNext()) {

				HColumn<String, String> column = columnsIterator.next();
		
				if (UserCassandraHelper.NAME.equals(column.getName())) {
					user.setName(column.getValue());

				} else {
					if (UserCassandraHelper.PASSWORD.equals(column.getName())) {
						user.setPassword(column.getValue());
					} else {
						if (UserCassandraHelper.SEARCHS.equals(column
								.getValue())) {
							user.addSearch(column.getName());
						}
					}

				}
			}

			return user;
		}

		return null;
	}

	// --------------------------------------------------------------------------------

	public void update(User externalResource) {
		super.updateColumn(externalResource.getUri(), UserCassandraHelper.NAME,
				externalResource.getDescription(),
				UserCassandraHelper.COLUMN_FAMILY);
	}

	// --------------------------------------------------------------------------------
	public boolean existsUserWithName(String name) {
		List result = (CassandraQueryResolver
				.query("select * from User where NAME='" + name + "'"));
		return ((result != null) && (result.size() > 0));
	}

	// --------------------------------------------------------------------------------
	public User getUserWithName(String name) {
		List result = (CassandraQueryResolver
				.query("select * from User where NAME='" + name + "'"));
		if ((result != null) && (result.size() > 0)) {
			Row row = (Row) result.get(0);
			User user = (User) this.read((String) row.getKey());
			return user;
		}
		return null;
	}

	// --------------------------------------------------------------------------------

	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();
		List<Row<String, String, String>> result = (CassandraQueryResolver
				.query("select * from " + UserCassandraHelper.COLUMN_FAMILY));

		if (result != null) {
			for (Row<String, String, String> row : result) {

				User user = (User) this.read((String) row.getKey());
				users.add(user);

			}
		}
		return users;
	}

	// --------------------------------------------------------------------------------

	public static void main(String[] args) {
		System.out.println("Starting test");
		System.out
				.println("Initialization --------------------------------------------");
		UserCassandraDAO userCassandraDAO = new UserCassandraDAO();
		ExternalResourceCassandraDAO externalResourceCassandraDAO = new ExternalResourceCassandraDAO();
		userCassandraDAO.init();
		externalResourceCassandraDAO.init();

		System.out.println(" --------------------------------------------");

		ExternalResource externalResource = new ExternalResource();
		externalResource.setUri("http://externalresourceuri");
		externalResource.setDescription("description of external resource");
		if (userCassandraDAO.existsUserWithName("Rafita")) {
			System.out.println("Rafita existe!");
			User userToDelete = userCassandraDAO.getUserWithName("Rafita");
			userCassandraDAO.remove(userToDelete.getUri());

		}

		if (userCassandraDAO.existsUserWithName("RafitaELOtro")) {
			System.out.println("RafitaElOtro existe!");
			User userToDelete = userCassandraDAO
					.getUserWithName("RafitaELOtro");
			userCassandraDAO.remove(userToDelete.getUri());
		}

		User readUser = (User) userCassandraDAO.read("http://userSara");
		System.out.println("readed user> " + readUser);

		// userCassandraDAO.delete("http://useruri");
		System.out.println("Exiting test");

	}
	
	// --------------------------------------------------------------------------------
	
		@Override
		public Content<String> getContent(Selector selector) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Content<String> getAnnotatedContent(Selector selector) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setContent(Selector selector, Content<String> content) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setAnnotatedContent(Selector selector,
				Content<String> annotatedContent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean exists(Selector selector) {
			// TODO Auto-generated method stub
			return false;
		}
		
		

}
