package org.epnoi.uia.informationstore.dao.solr;

import org.epnoi.model.*;
import org.epnoi.model.parameterization.InformationStoreParameters;
import org.epnoi.model.parameterization.SOLRInformationStoreParameters;
import org.epnoi.model.rdf.FeedRDFHelper;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.exception.DAONotFoundException;

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
		} else if (resource instanceof ResearchObject) {
			SOLRDAO dao = new ResearchObjectSOLRDAO();
			dao.init(this.parameters);
			return dao;
		} else {
			throw new DAONotFoundException("For resource " + resource);
		}

	}
	
	//----------------------------------------------------------------------------------------
	

	public SOLRDAO build(Selector selector) throws DAONotFoundException {
		String typeSelector = selector.getProperty(SelectorHelper.TYPE);
		if (typeSelector == null) {
			throw new DAONotFoundException(
					"No type specified for building the SOLRDAO");

		} else if (typeSelector.equals(FeedRDFHelper.FEED_CLASS)) {

			FeedSOLRDAO feedDAO = new FeedSOLRDAO();
			feedDAO.init(this.parameters);
			return feedDAO;

		} else if (typeSelector.equals(RDFHelper.PAPER_CLASS)) {
			SOLRDAO dao = new PaperSOLRDAO();
			dao.init(this.parameters);
			return dao;

		} else if (typeSelector.equals(RDFHelper.RESEARCH_OBJECT_CLASS)) {
		
			ResearchObjectSOLRDAO researchObjectDAO = new ResearchObjectSOLRDAO();
			researchObjectDAO.init(this.parameters);
			return researchObjectDAO;

		} else {
			throw new DAONotFoundException("Unknown type " + typeSelector);
		}
	}

}
