package org.epnoi.uia.informationstore.dao.cassandra;

import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;

import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.ExternalResource;
import org.epnoi.model.Feed;
import org.epnoi.model.Item;
import org.epnoi.model.Resource;
import org.epnoi.model.Search;
import org.epnoi.uia.informationstore.Selector;

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

	
}
