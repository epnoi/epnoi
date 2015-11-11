package org.epnoi.uia.informationstore.dao.cassandra;

import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.model.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;

public class AnnotatedContenCassandraDAO extends CassandraDAO {

	// --------------------------------------------------------------------------------

	public void remove(String URI) {
		super.deleteRow(URI, AnnotatedContentCassandraHelper.COLUMN_FAMILY);
	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		throw (new RuntimeException(
				"The create method of the AnnotatedContentCassandraDAO should not be invoked"));

	}

	// --------------------------------------------------------------------------------

	public Resource read(Selector selector) {
		throw (new RuntimeException(
				"The read method of the AnnotatedContentCassandraDAO should not be invoked"));

	}

	// --------------------------------------------------------------------------------

	public Resource read(String URI) {
		throw (new RuntimeException(
				"The read method of the AnnotatedContentCassandraDAO should not be invoked"));
	}

	// --------------------------------------------------------------------------------

	@Override
	public Content<String> getContent(Selector selector) {

		throw (new RuntimeException(
				"The getContent method of the AnnotatedContentCassandraDAO should not be invoked"));

	}

	// --------------------------------------------------------------------------------

	@Override
	public Content<String> getAnnotatedContent(Selector selector) {

		String value = super.readColumn(
				selector.getProperty(SelectorHelper.URI),
				AnnotatedContentCassandraHelper.CONTENT,
				AnnotatedContentCassandraHelper.COLUMN_FAMILY);

		String type = super.readColumn(
				selector.getProperty(SelectorHelper.URI),
				AnnotatedContentCassandraHelper.TYPE,
				AnnotatedContentCassandraHelper.COLUMN_FAMILY);

		return new Content<>(value, type);
	}

	// --------------------------------------------------------------------------------

	@Override
	public void setContent(Selector selector, Content<String> content) {

		throw (new RuntimeException(
				"The getContent method of the AnnotatedContentCassandraDAO should not be invoked"));

	}

	// --------------------------------------------------------------------------------

	@Override
	public void setAnnotatedContent(Selector selector,
			Content<String> annotatedContent) {
		System.out.println("selector> "+selector);

		super.updateColumn(selector.getProperty(SelectorHelper.URI),
				AnnotatedContentCassandraHelper.CONTENT,
				annotatedContent.getContent(),
				AnnotatedContentCassandraHelper.COLUMN_FAMILY);
		
		super.updateColumn(selector.getProperty(SelectorHelper.URI),
				AnnotatedContentCassandraHelper.TYPE,
				annotatedContent.getType(),
				AnnotatedContentCassandraHelper.COLUMN_FAMILY);
		
	}

	@Override
	public boolean exists(Selector selector) {
		// TODO Auto-generated method stub
		return false;
	}

	// --------------------------------------------------------------------------------

}
