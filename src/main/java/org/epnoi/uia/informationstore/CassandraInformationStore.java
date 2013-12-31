package org.epnoi.uia.informationstore;

import java.util.List;

import org.epnoi.uia.informationstore.dao.cassandra.CassandraDAO;
import org.epnoi.uia.informationstore.dao.cassandra.CassandraDAOFactory;
import org.epnoi.uia.parameterization.InformationStoreParameters;

import epnoi.model.Context;
import epnoi.model.Resource;
import epnoi.model.User;

public class CassandraInformationStore implements InformationStore {

	CassandraDAOFactory daoFactory;

	// ---------------------------------------------------------------------

	public void close() {
		// TODO Auto-generated method stub

	}

	// ------------------------------------------------------------------------

	public void init(InformationStoreParameters parameters) {
		// TODO Auto-generated method stub
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

	public List<String> query(String queryExpression) {
		// TODO Auto-generated method stub
		return null;
	}

	// ------------------------------------------------------------------------

	public void put(Resource resource) {
		// TODO Auto-generated method stub

	}

	// ------------------------------------------------------------------------

	public void put(Resource resource, Context context) {
		// TODO Auto-generated method stub

	}

	// ------------------------------------------------------------------------

	public InformationStoreParameters getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

}
