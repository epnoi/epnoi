package org.epnoi.uia.informationstore.dao.cassandra;

import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;
import org.epnoi.model.*;

public class ItemCassandraDAO extends CassandraDAO {

	public void remove(String URI) {
		super.deleteRow(URI, ItemCassandraHelper.COLUMN_FAMILY);
	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		Item item = (Item) resource;
		super.createRow(item.getUri(), ItemCassandraHelper.COLUMN_FAMILY);

		if (item.getDescription() != null) {

			super.updateColumn(item.getUri(), ItemCassandraHelper.DESCRIPTION,
					item.getDescription(), ItemCassandraHelper.COLUMN_FAMILY);

		}

		if (context != null) {
			String content = (String) context.getElements().get(item.getUri());

			super.updateColumn(item.getUri(), ItemCassandraHelper.CONTENT,
					content, ItemCassandraHelper.COLUMN_FAMILY);
		}

	}

	// --------------------------------------------------------------------------------

	public Resource read(Selector selector) {
		return new ExternalResource();
	}

	// --------------------------------------------------------------------------------

	public Resource read(String URI) {
		ColumnSliceIterator<String, String, String> columnsIterator = super
				.getAllCollumns(URI, ItemCassandraHelper.COLUMN_FAMILY);
		if (columnsIterator.hasNext()) {
			Item item = new Item();
			item.setUri(URI);
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
		super.updateColumn(search.getUri(), SearchCassandraHelper.DESCRIPTION,
				search.getDescription(), UserCassandraHelper.COLUMN_FAMILY);
	}

	// --------------------------------------------------------------------------------
	
	@Override
	public Content<String> getContent(Selector selector) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	// --------------------------------------------------------------------------------

	public static void main(String[] args) {
		FeedCassandraDAO dao = new FeedCassandraDAO();
		dao.init();
		Feed feed = (Feed) dao
				.read("http://www.epnoi.org/informationSources/slashdot");
		//System.out.println(">>> " + feed.getItems().get(0).toString());
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
