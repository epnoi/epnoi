package org.epnoi.uia.informationstore.dao.solr;

import org.epnoi.model.Feed;
import org.epnoi.model.Paper;
import org.epnoi.model.Resource;
import org.epnoi.uia.informationstore.dao.exception.DAONotFoundException;
import org.epnoi.uia.parameterization.InformationStoreParameters;
import org.epnoi.uia.parameterization.SOLRInformationStoreParameters;

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
		} else if (resource instanceof Paper) {
			SOLRDAO dao = new PaperSOLRDAO();
			dao.init(this.parameters);
			return dao;
		} else {
			throw new DAONotFoundException("For resource " + resource);
		}

	}

}
