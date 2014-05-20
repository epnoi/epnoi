package org.epnoi.uia.informationstore.dao.cassandra;

import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.exception.DAONotFoundException;
import org.epnoi.uia.informationstore.dao.rdf.FeedRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.SearchRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.UserRDFHelper;
import org.epnoi.uia.parameterization.CassandraInformationStoreParameters;
import org.epnoi.uia.parameterization.InformationStoreParameters;

import epnoi.model.Feed;
import epnoi.model.Resource;
import epnoi.model.User;

public class CassandraDAOFactory {

	CassandraInformationStoreParameters parameters;

	// --------------------------------------------------------------------------------

	public CassandraDAOFactory(InformationStoreParameters parameters) {
		this.parameters = (CassandraInformationStoreParameters) parameters;
	}

	// --------------------------------------------------------------------------------

	public CassandraDAO build(Resource resource) throws DAONotFoundException {
		if (resource instanceof User) {
			UserCassandraDAO userDAO = new UserCassandraDAO();
			userDAO.init();
			return userDAO;
			
		}
		
		if (resource instanceof Feed) {
			FeedCassandraDAO userDAO = new FeedCassandraDAO();
			userDAO.init();
			return userDAO;
			
		}
		throw new DAONotFoundException("Not implemented");
	}

	// --------------------------------------------------------------------------------

	public CassandraDAO build(Selector selector) throws DAONotFoundException {
		String typeSelector = selector.getProperty(SelectorHelper.TYPE);
		if (typeSelector == null) {
			throw new DAONotFoundException("No column name specified");
		} else if (typeSelector.equals(UserRDFHelper.USER_CLASS)) {

			UserCassandraDAO userDAO = new UserCassandraDAO();
			userDAO.init();
			return userDAO;
		} else if (typeSelector.equals(SearchRDFHelper.SEARCH_CLASS)) {

			SearchCassandraDAO searchDAO = new SearchCassandraDAO();
			searchDAO.init();
			return searchDAO;

		} else if (typeSelector.equals(FeedRDFHelper.ITEM_CLASS)) {

			ItemCassandraDAO searchDAO = new ItemCassandraDAO();
			searchDAO.init();
			return searchDAO;

		} else {
			throw new DAONotFoundException("Unknown type " + typeSelector);
		}
	}
}
