package org.epnoi.uia.informationstore.dao.cassandra;

import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;

import org.epnoi.uia.informationstore.Selector;

import epnoi.model.Context;
import epnoi.model.ExternalResource;
import epnoi.model.Feed;
import epnoi.model.Item;
import epnoi.model.Resource;
import epnoi.model.Search;

public class ItemCassandraDAO extends CassandraDAO {

	public void remove(String URI) {
		super.deleteRow(URI, ItemCassandraHelper.COLUMN_FAMILLY);
	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		Item item = (Item) resource;
		super.createRow(item.getURI(), ItemCassandraHelper.COLUMN_FAMILLY);

		if (item.getDescription() != null) {

			super.updateColumn(item.getURI(), ItemCassandraHelper.DESCRIPTION,
					item.getDescription(), ItemCassandraHelper.COLUMN_FAMILLY);

		}

		if (context != null) {
			String content = (String) context.getElements().get(item.getURI());

			super.updateColumn(item.getURI(), ItemCassandraHelper.CONTENT,
					content, ItemCassandraHelper.COLUMN_FAMILLY);
		}

	}

	// --------------------------------------------------------------------------------

	public Resource read(Selector selector) {
		return new ExternalResource();
	}

	// --------------------------------------------------------------------------------

	public Resource read(String URI) {
		ColumnSliceIterator<String, String, String> columnsIterator = super
				.getAllCollumns(URI, ItemCassandraHelper.COLUMN_FAMILLY);
		if (columnsIterator.hasNext()) {
			Item item = new Item();
			item.setURI(URI);
			while (columnsIterator.hasNext()) {

				HColumn<String, String> column = columnsIterator.next();

				if (ItemCassandraHelper.DESCRIPTION.equals(column.getName())) {
					item.setDescription(column.getValue());

				} else if (ItemCassandraHelper.CONTENT.equals(column.getName())) {
					item.setContent(column.getValue());
				}

			}
			return item;
		}
		return null;
	}
	
	// --------------------------------------------------------------------------------

	public void update(Search search) {
		super.updateColumn(search.getURI(), SearchCassandraHelper.DESCRIPTION,
				search.getDescription(), UserCassandraHelper.COLUMN_FAMILLY);
	}

	// --------------------------------------------------------------------------------

	public static void main(String[] args) {
		FeedCassandraDAO dao = new FeedCassandraDAO();
		dao.init();
		Feed feed = (Feed) dao
				.read("http://www.epnoi.org/informationSources/slashdot");
		//System.out.println(">>> " + feed.getItems().get(0).toString());
	}

}
