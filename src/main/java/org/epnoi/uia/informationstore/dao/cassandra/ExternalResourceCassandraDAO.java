package org.epnoi.uia.informationstore.dao.cassandra;

import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;

import org.epnoi.uia.informationstore.Selector;

import epnoi.model.Context;
import epnoi.model.ExternalResource;
import epnoi.model.Resource;

public class ExternalResourceCassandraDAO extends CassandraDAO {

	public void remove(String URI) {
		super.deleteRow(URI, ExternalResourceCassandraHelper.COLUMN_FAMILLY);
	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		ExternalResource externalResource = (ExternalResource)resource;
		super.createRow(externalResource.getURI(),
				ExternalResourceCassandraHelper.COLUMN_FAMILLY);
		if (externalResource.getDescription() != null) {
			super.updateColumn(externalResource.getURI(),
					ExternalResourceCassandraHelper.DESCRIPTION,
					externalResource.getDescription(),
					ExternalResourceCassandraHelper.COLUMN_FAMILLY);

		}

	}
	
	// --------------------------------------------------------------------------------

	public Resource read(Selector selector) {
		return new ExternalResource();
	}

	// --------------------------------------------------------------------------------

	public Resource read(String URI) {
		/*
		 * System.out.println(" --> " + URI); ColumnSliceIterator<String,
		 * String, String> columnsIteratorProof = super .getAllCollumns(URI,
		 * ExternalResourceCassandraHelper.COLUMN_FAMILLY);
		 * 
		 * while (columnsIteratorProof.hasNext()) { HColumn<String, String>
		 * column = columnsIteratorProof.next(); System.out.println("Column   "
		 * + column); }
		 */
		ColumnSliceIterator<String, String, String> columnsIterator = super
				.getAllCollumns(URI,
						ExternalResourceCassandraHelper.COLUMN_FAMILLY);
		if (columnsIterator.hasNext()) {
			ExternalResource externalResource = new ExternalResource();
			externalResource.setURI(URI);
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
		super.updateColumn(externalResource.getURI(),
				ExternalResourceCassandraHelper.DESCRIPTION,
				externalResource.getDescription(),
				ExternalResourceCassandraHelper.COLUMN_FAMILLY);
	}

	// --------------------------------------------------------------------------------

	public static void main(String[] args) {
		ExternalResourceCassandraDAO externalResourceCassandraDAO = new ExternalResourceCassandraDAO();
		externalResourceCassandraDAO.init();
		System.out.println("Starting test");

		ExternalResource externalResource = new ExternalResource();
		externalResource.setURI("http://uriproof");
		externalResource.setDescription("description proof");

		ExternalResource externalResource2 = new ExternalResource();
		externalResource2.setURI("http://uriproof2");
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
}
