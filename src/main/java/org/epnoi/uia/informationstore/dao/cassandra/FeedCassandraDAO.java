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

public class FeedCassandraDAO extends CassandraDAO {

	public void remove(String URI) {
		super.deleteRow(URI, FeedCassandraHelper.COLUMN_FAMILLY);
	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		Feed feed = (Feed) resource;
		super.createRow(feed.getURI(), FeedCassandraHelper.COLUMN_FAMILLY);

		if (feed.getDescription() != null) {

			super.updateColumn(feed.getURI(), FeedCassandraHelper.DESCRIPTION,
					feed.getDescription(), FeedCassandraHelper.COLUMN_FAMILLY);

		}

		for (Item item : feed.getItems()) {

			_createItem(feed.getURI(), item, context);
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
				.getAllCollumns(URI, FeedCassandraHelper.COLUMN_FAMILLY);
		if (columnsIterator.hasNext()) {
			Feed search = new Feed();
			search.setURI(URI);
			while (columnsIterator.hasNext()) {

				HColumn<String, String> column = columnsIterator.next();
				//System.out.println("feed column.getName------------------>  "
				//		+ column.getName());
				if (FeedCassandraHelper.DESCRIPTION.equals(column.getName())) {
					search.setDescription(column.getValue());

				} else if (FeedCassandraHelper.ITEMS.equals(column.getValue())) {
					Item item = _readItem(column.getName());
					search.addItem(item);
				}
			}

			return search;
		}

		return null;
	}

	// --------------------------------------------------------------------------------

	private Item _readItem(String URI) {
		Item item = new Item();
		item.setURI(URI);

		ColumnSliceIterator<String, String, String> columnsIterator = super
				.getAllCollumns(URI, ItemCassandraHelper.COLUMN_FAMILLY);
		System.out.println("item URI-------------------------------> " + URI);
		while (columnsIterator.hasNext()) {

			HColumn<String, String> column = columnsIterator.next();
			System.out
					.println("item column.getName-------------------------------> "
							+ column.getName());
			if (ItemCassandraHelper.DESCRIPTION.equals(column.getName())) {
				item.setDescription(column.getValue());

			} else if (ItemCassandraHelper.CONTENT.equals(column.getName())) {
				item.setContent(column.getValue());
			}
		}
		return item;

	}

	// --------------------------------------------------------------------------------

	public void update(Search search) {
		super.updateColumn(search.getURI(), SearchCassandraHelper.DESCRIPTION,
				search.getDescription(), UserCassandraHelper.COLUMN_FAMILLY);
	}

	// --------------------------------------------------------------------------------

	private void _createItem(String feedURI, Item item, Context context) {

		//super.createRow(item.getURI(), ItemCassandraHelper.COLUMN_FAMILLY);

		if (item.getDescription() != null) {

			super.updateColumn(item.getURI(), ItemCassandraHelper.DESCRIPTION,
					item.getDescription(), ItemCassandraHelper.COLUMN_FAMILLY);

		}

		if (context != null) {
			String content = (String) context.getElements().get(item.getURI());

			super.updateColumn(item.getURI(), ItemCassandraHelper.CONTENT,
					content, ItemCassandraHelper.COLUMN_FAMILLY);
		}

		super.updateColumn(feedURI, item.getURI(), FeedCassandraHelper.ITEMS,
				FeedCassandraHelper.COLUMN_FAMILLY);
		//_showFeed(feedURI);
	}

	private void _showFeed(String URI) {
		ColumnSliceIterator<String, String, String> columnsIterator = super
				.getAllCollumns(URI, FeedCassandraHelper.COLUMN_FAMILLY);
		if (columnsIterator.hasNext()) {

			while (columnsIterator.hasNext()) {

				HColumn<String, String> column = columnsIterator.next();

			}
		}
	}

	public static void main(String[] args) {
		FeedCassandraDAO dao = new FeedCassandraDAO();
		dao.init();
		Feed feed = (Feed) dao
				.read("http://www.epnoi.org/informationSources/slashdot");
		System.out.println(">>> " + feed.getItems().get(0).toString());
	}

}
