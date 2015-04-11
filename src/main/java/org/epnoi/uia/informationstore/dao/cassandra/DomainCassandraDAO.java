package org.epnoi.uia.informationstore.dao.cassandra;

import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;

import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.Domain;
import org.epnoi.model.ExternalResource;
import org.epnoi.model.Resource;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;

public class DomainCassandraDAO extends CassandraDAO {

	// --------------------------------------------------------------------------------

	public void remove(String URI) {
		super.deleteRow(URI, DomainCassandraHelper.COLUMN_FAMILLY);
	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {

		Domain domain = (Domain) resource;

		super.createRow(domain.getURI(), DomainCassandraHelper.COLUMN_FAMILLY);

		super.updateColumn(domain.getURI(), DomainCassandraHelper.NAME,
				domain.getName(), DomainCassandraHelper.COLUMN_FAMILLY);

		super.updateColumn(domain.getURI(), DomainCassandraHelper.EXPRESSION,
				domain.getExpression(), DomainCassandraHelper.COLUMN_FAMILLY);
		
		super.updateColumn(domain.getURI(), DomainCassandraHelper.CONSIDERED_RESOURCES,
				domain.getExpression(), DomainCassandraHelper.COLUMN_FAMILLY);
	}

	// --------------------------------------------------------------------------------

	public Resource read(Selector selector) {

		return new ExternalResource();
	}

	// --------------------------------------------------------------------------------

	public Resource read(String URI) {

		ColumnSliceIterator<String, String, String> columnsIterator = super
				.getAllCollumns(URI, DomainCassandraHelper.COLUMN_FAMILLY);

		if (columnsIterator.hasNext()) {
			Domain term = new Domain();

			term.setURI(URI);

			while (columnsIterator.hasNext()) {
				HColumn<String, String> column = columnsIterator.next();

				String columnName = column.getName();
				String columnValue = column.getValue();
				switch (columnName) {
				case DomainCassandraHelper.NAME:
					term.setName(columnValue);
					break;

				case DomainCassandraHelper.EXPRESSION:
					term.setExpression(columnValue);
					break;

				case DomainCassandraHelper.CONSIDERED_RESOURCES:
					term.setConsideredResource(columnValue);
					break;
				}

			}
			return term;
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

	public static void main(String[] args) {
		Core core = CoreUtility.getUIACore();
		Domain domain = new Domain();
		domain.setURI("lauri");
		domain.setExpression("sparqlexpression");
		domain.setName("name");
		domain.setConsideredResource(RDFHelper.DOMAIN_CLASS);

		core.getInformationHandler().put(domain, Context.getEmptyContext());

		System.out.println("-------> "
				+ core.getInformationHandler().get("lauri"));
	}

	@Override
	public boolean exists(Selector selector) {
		// TODO Auto-generated method stub
		return false;
	}
}
