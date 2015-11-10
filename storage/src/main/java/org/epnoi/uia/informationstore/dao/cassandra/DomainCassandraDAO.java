package org.epnoi.uia.informationstore.dao.cassandra;

import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;
import org.epnoi.model.*;
import org.epnoi.uia.informationstore.SelectorHelper;

public class DomainCassandraDAO extends CassandraDAO {

	// --------------------------------------------------------------------------------

	public void remove(String URI) {
		super.deleteRow(URI, DomainCassandraHelper.COLUMN_FAMILY);
	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
System.out.println("DOMAIN "+resource);
		Domain domain = (Domain) resource;

		super.createRow(domain.getUri(), DomainCassandraHelper.COLUMN_FAMILY);

		super.updateColumn(domain.getUri(), DomainCassandraHelper.LABEL,
				domain.getLabel(), DomainCassandraHelper.COLUMN_FAMILY);

		super.updateColumn(domain.getUri(), DomainCassandraHelper.EXPRESSION,
				domain.getExpression(), DomainCassandraHelper.COLUMN_FAMILY);

		super.updateColumn(domain.getUri(), DomainCassandraHelper.TYPE,
				domain.getType(), DomainCassandraHelper.COLUMN_FAMILY);
		
		super.updateColumn(domain.getUri(), DomainCassandraHelper.RESOURCES,
				domain.getResources(), DomainCassandraHelper.COLUMN_FAMILY);
	}

	// --------------------------------------------------------------------------------

	public Resource read(Selector selector) {

		return new ExternalResource();
	}

	// --------------------------------------------------------------------------------

	public Resource read(String URI) {

		ColumnSliceIterator<String, String, String> columnsIterator = super
				.getAllCollumns(URI, DomainCassandraHelper.COLUMN_FAMILY);

		if (columnsIterator.hasNext()) {
			Domain domain = new Domain();

			domain.setUri(URI);

			while (columnsIterator.hasNext()) {
				HColumn<String, String> column = columnsIterator.next();

				String columnName = column.getName();
				String columnValue = column.getValue();
				switch (columnName) {
				case DomainCassandraHelper.LABEL:
					domain.setLabel(columnValue);
					break;

				case DomainCassandraHelper.EXPRESSION:
					domain.setExpression(columnValue);
					break;

				case DomainCassandraHelper.TYPE:
					domain.setType(columnValue);
					break;

				case DomainCassandraHelper.RESOURCES:
					domain.setResources(columnValue);
					break;

				}
			}
			return domain;
		}

		return null;

	}

	// --------------------------------------------------------------------------------

	@Override
	public Content<String> getContent(Selector selector) {

		throw (new RuntimeException(
				"The getContent method of the TermCassandraDAO should not be invoked"));
	}

	// --------------------------------------------------------------------------------

	@Override
	public Content<String> getAnnotatedContent(Selector selector) {

		throw (new RuntimeException(
				"The getAnnotatedContent method of the DomainCassandraDAO should not be invoked"));
	}

	// --------------------------------------------------------------------------------

	@Override
	public void setContent(Selector selector, Content<String> content) {

		throw (new RuntimeException(
				"The setContent method of the DomainCassandraDAO should not be invoked"));
	}

	// --------------------------------------------------------------------------------

	@Override
	public void setAnnotatedContent(Selector selector,
			Content<String> annotatedContent) {

		throw (new RuntimeException(
				"The setAnnotatedContent method of the DomainCassandraDAO should not be invoked"));
	}

	// --------------------------------------------------------------------------------
/*
	public static void main(String[] args) {
		Core core = CoreUtility.getUIACore();
		Domain domain = new Domain();
		domain.setURI("lauri");
		domain.setExpression("sparqlexpression");
		domain.setLabel("name");
		domain.setType(RDFHelper.DOMAIN_CLASS);

		core.getInformationHandler().put(domain, Context.getEmptyContext());

		System.out.println("-------> "
				+ core.getInformationHandler().get("lauri"));
	}
*/
	@Override
	public boolean exists(Selector selector) {
		return (super.getAllCollumns(selector.getProperty(SelectorHelper.URI),
				DomainCassandraHelper.COLUMN_FAMILY).hasNext());
	}
}
