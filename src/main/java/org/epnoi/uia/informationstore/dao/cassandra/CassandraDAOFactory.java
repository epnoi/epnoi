package org.epnoi.uia.informationstore.dao.cassandra;

import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.exception.DAONotFoundException;
import org.epnoi.uia.informationstore.dao.rdf.RDFDAO;
import org.epnoi.uia.informationstore.dao.rdf.UserRDFHelper;
import org.epnoi.uia.parameterization.CassandraInformationStoreParameters;
import org.epnoi.uia.parameterization.InformationStoreParameters;

import epnoi.model.Resource;

public class CassandraDAOFactory {

	CassandraInformationStoreParameters parameters;

	// --------------------------------------------------------------------------------

	public CassandraDAOFactory(InformationStoreParameters parameters) {
		this.parameters = (CassandraInformationStoreParameters) parameters;
	}

	// --------------------------------------------------------------------------------

	public CassandraDAO build(Resource resource) throws DAONotFoundException {
		throw new DAONotFoundException("Not implemented");
	}

	// --------------------------------------------------------------------------------

	public CassandraDAO build(Selector selector) throws DAONotFoundException {
		String typeSelector = selector.getProperty(SelectorHelper.TYPE);
		if (typeSelector == null) {
			throw new DAONotFoundException("No column name specified");
		} else if (typeSelector.equals(UserRDFHelper.USER_CLASS)){
			
			UserCassandraDAO userDAO= new UserCassandraDAO();
			userDAO.init();
			return userDAO;
			
		} else{
			throw new DAONotFoundException("Unknown type "+ typeSelector);
		}
	}
}
