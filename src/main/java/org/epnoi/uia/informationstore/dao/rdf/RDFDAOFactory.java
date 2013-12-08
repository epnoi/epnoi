package org.epnoi.uia.informationstore.dao.rdf;

import org.epnoi.uia.informationstore.dao.exception.DAONotFoundException;
import org.epnoi.uia.parameterization.InformationStoreParameters;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;

import epnoi.model.Feed;
import epnoi.model.InformationSource;
import epnoi.model.Resource;

public class RDFDAOFactory {
	VirtuosoInformationStoreParameters parameters;

	public RDFDAOFactory(InformationStoreParameters parameters) {
		this.parameters = (VirtuosoInformationStoreParameters) parameters;
	}

	public RDFDAO build(Resource resource) throws DAONotFoundException {

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
