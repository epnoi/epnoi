package org.epnoi.uia.informationstore.dao.solr;

import org.epnoi.uia.informationstore.dao.exception.DAONotFoundException;
import org.epnoi.uia.parameterization.InformationStoreParameters;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;

import epnoi.model.Feed;
import epnoi.model.InformationSource;
import epnoi.model.Resource;

public class SOLRDAOFactory {
	VirtuosoInformationStoreParameters parameters;

	public SOLRDAOFactory(InformationStoreParameters parameters) {
		this.parameters = (VirtuosoInformationStoreParameters) parameters;
	}

	public SOLRDAO build(Resource resource) throws DAONotFoundException {

		if (resource instanceof InformationSource) {
			RDFDAO dao = new InformationSourceRDFDAO();
			dao.init(this.parameters);

			return dao;
		} else if (resource instanceof Feed) {
			RDFDAO dao = new FeedRDFDAO();
			dao.init(this.parameters);
			return dao;
		} else {
			throw new DAONotFoundException("For resource " + resource);
		}

	}

}
