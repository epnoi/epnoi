package org.epnoi.uia.informationstore.dao.cassandra;

import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.dao.exception.DAONotFoundException;
import org.epnoi.uia.informationstore.dao.rdf.RDFDAO;
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
		String typeSelector = selector.getProperty(CassandraDAOHelper.COLUMN_FAMILLY);
		if (typeSelector != null) {
			throw new DAONotFoundException("No column name specified");
		} else if (typeSelector.equals(UserCassandraHelper.COLUMN_FAMILLY)){
			return new UserCassandraDAO();
			
		} else{
			throw new DAONotFoundException("N");
		}
	}
}
