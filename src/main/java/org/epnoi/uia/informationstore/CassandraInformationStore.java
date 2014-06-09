package org.epnoi.uia.informationstore;

import java.util.List;

import org.epnoi.uia.informationstore.dao.cassandra.CassandraDAO;
import org.epnoi.uia.informationstore.dao.cassandra.CassandraDAOFactory;
import org.epnoi.uia.parameterization.InformationStoreParameters;
import org.epnoi.uia.search.SearchContext;
import org.epnoi.uia.search.select.SearchSelectResult;
import org.epnoi.uia.search.select.SelectExpression;

import epnoi.model.Context;
import epnoi.model.Resource;
import epnoi.model.User;

public class CassandraInformationStore implements InformationStore {
InformationStoreParameters parameters;
	CassandraDAOFactory daoFactory;

	// ---------------------------------------------------------------------

	public void close() {
		// TODO Auto-generated method stub

	}

	// ------------------------------------------------------------------------

	public void init(InformationStoreParameters parameters) {
		this.parameters=parameters;
		this.daoFactory = new CassandraDAOFactory(parameters);
	}

	// ------------------------------------------------------------------------

	public boolean test() {
		// TODO Auto-generated method stub
		return false;
	}

	// ------------------------------------------------------------------------

	public Resource get(String URI) {
		// TODO Auto-generated method stub
		return null;
	}

	// ------------------------------------------------------------------------

	public Resource get(Selector selector) {

		CassandraDAO dao = this.daoFactory.build(selector);

		Resource resource = dao.read(selector.getProperty(SelectorHelper.URI));
		return resource;
	}

	// ------------------------------------------------------------------------

	public void remove(Selector selector) {

		CassandraDAO dao = this.daoFactory.build(selector);

		dao.remove(selector.getProperty(SelectorHelper.URI));

	}

	// ------------------------------------------------------------------------

	public List<String> query(String queryExpression) {
		// TODO Auto-generated method stub
		return null;
	}

	// ------------------------------------------------------------------------

	@Override
	public SearchSelectResult query(SelectExpression selectionExpression,
			SearchContext searchContext) {
		// TODO Auto-generated method stub
		return null;
	}

	// ------------------------------------------------------------------------

	public void put(Resource resource, Context context) {
		CassandraDAO dao = this.daoFactory.build(resource);

		dao.create(resource, context);

	}

	// ------------------------------------------------------------------------

	public InformationStoreParameters getParameters() {
		
		return this.parameters;
	}

	// ------------------------------------------------------------------------

	public void update(Resource resource) {
		// TODO Auto-generated method stub
	}

}
