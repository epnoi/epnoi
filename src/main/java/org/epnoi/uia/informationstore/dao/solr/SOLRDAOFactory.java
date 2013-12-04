package org.epnoi.uia.informationstore.dao.solr;

import org.epnoi.uia.informationstore.dao.exception.DAONotFoundException;
import org.epnoi.uia.parameterization.InformationStoreParameters;
import org.epnoi.uia.parameterization.SOLRInformationStoreParameters;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;

import epnoi.model.Feed;
import epnoi.model.Resource;

public class SOLRDAOFactory {
	SOLRInformationStoreParameters parameters;

	// --------------------------------------------------------------------------------

	public SOLRDAOFactory(InformationStoreParameters parameters) {
		this.parameters = (SOLRInformationStoreParameters) parameters;
	}

	// --------------------------------------------------------------------------------


	public SOLRDAO build(Resource resource) throws DAONotFoundException {

		if (resource instanceof Feed) {
			SOLRDAO dao = new FeedSOLRDAO();
			dao.init(this.parameters);
			return dao;
		} else {
			throw new DAONotFoundException("For resource " + resource);
		}

	}

}
