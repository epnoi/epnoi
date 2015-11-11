package org.epnoi.uia.informationstore.dao.cassandra;

import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;
import org.epnoi.model.*;



public class ExternalResourceCassandraDAO extends CassandraDAO {

	public void remove(String URI) {
		super.deleteRow(URI, ExternalResourceCassandraHelper.COLUMN_FAMILY);
	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		ExternalResource externalResource = (ExternalResource)resource;
		super.createRow(externalResource.getUri(),
				ExternalResourceCassandraHelper.COLUMN_FAMILY);
		if (externalResource.getDescription() != null) {
			super.updateColumn(externalResource.getUri(),
					ExternalResourceCassandraHelper.DESCRIPTION,
					externalResource.getDescription(),
					ExternalResourceCassandraHelper.COLUMN_FAMILY);

		}

	}
	
	// --------------------------------------------------------------------------------

	public Resource read(Selector selector) {
		return new ExternalResource();
	}

	// --------------------------------------------------------------------------------

	public Resource read(String URI) {
	
		ColumnSliceIterator<String, String, String> columnsIterator = super
				.getAllCollumns(URI,
						ExternalResourceCassandraHelper.COLUMN_FAMILY);
		if (columnsIterator.hasNext()) {
			ExternalResource externalResource = new ExternalResource();
			externalResource.setUri(URI);
			while (columnsIterator.hasNext()) {
				HColumn<String, String> column = columnsIterator.next();
				System.out.println("-- "+column);
				if (ExternalResourceCassandraHelper.DESCRIPTION.equals(column
						.getName())) {
					externalResource.setDescription(column.getValue());

				}
			}

			return externalResource;
		}

		return null;
	}

	// --------------------------------------------------------------------------------

	public void update(ExternalResource externalResource) {
		super.updateColumn(externalResource.getUri(),
				ExternalResourceCassandraHelper.DESCRIPTION,
				externalResource.getDescription(),
				ExternalResourceCassandraHelper.COLUMN_FAMILY);
	}

	// --------------------------------------------------------------------------------

	public static void main(String[] args) {
		ExternalResourceCassandraDAO externalResourceCassandraDAO = new ExternalResourceCassandraDAO();
		externalResourceCassandraDAO.init();
		System.out.println("Starting test");

		ExternalResource externalResource = new ExternalResource();
		externalResource.setUri("http://uriproof");
		externalResource.setDescription("description proof");

		ExternalResource externalResource2 = new ExternalResource();
		externalResource2.setUri("http://uriproof2");
		externalResource2.setDescription("description proof2");

		Context context = new Context();
		
		externalResourceCassandraDAO.create(externalResource, context);

		externalResourceCassandraDAO.create(externalResource2, context);
		externalResourceCassandraDAO.remove("http://uriproof2");
		// externalResourceCassandraDAO.delete("http://uriproof");

		ExternalResource readedExternalResource = (ExternalResource)externalResourceCassandraDAO
				.read("http://uriproof");
		System.out
				.println("readedExternalResource.> " + readedExternalResource);

		externalResourceCassandraDAO.remove("http://uriproof");
		System.out.println("Exiting test");

	}
	
	// --------------------------------------------------------------------------------
	
		@Override
		public Content getContent(Selector selector) {
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
