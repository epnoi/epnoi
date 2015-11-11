package org.epnoi.uia.informationstore.dao.cassandra;

import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;
import org.epnoi.model.*;

public class FeedCassandraDAO extends CassandraDAO {

	public void remove(String URI) {
		super.deleteRow(URI, FeedCassandraHelper.COLUMN_FAMILY);
	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		Feed feed = (Feed) resource;
		super.createRow(feed.getUri(), FeedCassandraHelper.COLUMN_FAMILY);

		if (feed.getDescription() != null) {

			super.updateColumn(feed.getUri(), FeedCassandraHelper.DESCRIPTION,
					feed.getDescription(), FeedCassandraHelper.COLUMN_FAMILY);

		}

		for (Item item : feed.getItems()) {

			_createItem(feed.getUri(), item, context);
		}

	}

	// --------------------------------------------------------------------------------

	public Resource read(Selector selector) {
		return new ExternalResource();
	}

	// --------------------------------------------------------------------------------

	public Resource read(String URI) {
	
		ColumnSliceIterator<String, String, String> columnsIterator = super
				.getAllCollumns(URI, FeedCassandraHelper.COLUMN_FAMILY);
		if (columnsIterator.hasNext()) {
			Feed search = new Feed();
			search.setUri(URI);
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
		item.setUri(URI);

		ColumnSliceIterator<String, String, String> columnsIterator = super
				.getAllCollumns(URI, ItemCassandraHelper.COLUMN_FAMILY);
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
		super.updateColumn(search.getUri(), SearchCassandraHelper.DESCRIPTION,
				search.getDescription(), UserCassandraHelper.COLUMN_FAMILY);
	}

	// --------------------------------------------------------------------------------

	private void _createItem(String feedURI, Item item, Context context) {

		//super.createRow(item.getURI(), ItemCassandraHelper.COLUMN_FAMILY);

		if (item.getDescription() != null) {

			super.updateColumn(item.getUri(), ItemCassandraHelper.DESCRIPTION,
					item.getDescription(), ItemCassandraHelper.COLUMN_FAMILY);

		}

		if (context != null) {
			String content = (String) context.getElements().get(item.getUri());

			super.updateColumn(item.getUri(), ItemCassandraHelper.CONTENT,
					content, ItemCassandraHelper.COLUMN_FAMILY);
		}

		super.updateColumn(feedURI, item.getUri(), FeedCassandraHelper.ITEMS,
				FeedCassandraHelper.COLUMN_FAMILY);
		//_showFeed(feedURI);
	}
	
	// --------------------------------------------------------------------------------
	
		@Override
		public Content<String> getContent(Selector selector) {
			// TODO Auto-generated method stub
			return null;
		}

		
		

	private void _showFeed(String URI) {
		ColumnSliceIterator<String, String, String> columnsIterator = super
				.getAllCollumns(URI, FeedCassandraHelper.COLUMN_FAMILY);
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
