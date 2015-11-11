package org.epnoi.uia.informationstore.dao.cassandra;

import org.epnoi.model.*;
import org.epnoi.uia.informationstore.SelectorHelper;

public class ContentCassandraDAO extends CassandraDAO {

	// --------------------------------------------------------------------------------

	public void remove(String URI) {
		super.deleteRow(URI, ContentCassandraHelper.COLUMN_FAMILY);
	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		throw (new RuntimeException(
				"The create method of the ContentCassandraDAO should not be invoked"));

	}

	// --------------------------------------------------------------------------------

	public Resource read(Selector selector) {
		throw (new RuntimeException(
				"The read method of the ContentCassandraDAO should not be invoked"));

	}

	// --------------------------------------------------------------------------------

	public Resource read(String URI) {
		throw (new RuntimeException(
				"The read method of the ContentCassandraDAO should not be invoked"));
	}

	// --------------------------------------------------------------------------------

	@Override
	public Content<String> getContent(Selector selector) {

		String value = super.readColumn(
				selector.getProperty(SelectorHelper.URI),
				ContentCassandraHelper.CONTENT,
				ContentCassandraHelper.COLUMN_FAMILY);

		return new Content<>(value, ContentHelper.CONTENT_TYPE_TEXT_PLAIN);
	}

	// --------------------------------------------------------------------------------

	@Override
	public Content<String> getAnnotatedContent(Selector selector) {
		throw (new RuntimeException(
				"The getAnnotatedContent method of the ContentCassandraDAO should not be invoked"));
	}

	// --------------------------------------------------------------------------------

	@Override
	public void setContent(Selector selector, Content<String> content) {

		super.updateColumn(selector.getProperty(SelectorHelper.URI),
				ContentCassandraHelper.CONTENT, content.getContent(),
				ContentCassandraHelper.COLUMN_FAMILY);
		
		
	}

	// --------------------------------------------------------------------------------

	@Override
	public void setAnnotatedContent(Selector selector,
			Content<String> annotatedContent) {
		throw (new RuntimeException(
				"The setAnnotatedContent method of the ContentCassandraDAO should not be invoked"));
	}

	@Override
	public boolean exists(Selector selector) {
		// TODO Auto-generated method stub
		return false;
	}

	// --------------------------------------------------------------------------------

}
