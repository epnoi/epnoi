package org.epnoi.uia.informationstore.dao.rdf;

import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.exception.DAONotFoundException;
import org.epnoi.uia.parameterization.InformationStoreParameters;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;

import epnoi.model.Feed;
import epnoi.model.InformationSource;
import epnoi.model.InformationSourceSubscription;
import epnoi.model.Item;
import epnoi.model.Resource;
import epnoi.model.User;

public class RDFDAOFactory {
	VirtuosoInformationStoreParameters parameters;

	// ------------------------------------------------------------------------------

	public RDFDAOFactory(InformationStoreParameters parameters) {
		this.parameters = (VirtuosoInformationStoreParameters) parameters;
	}

	// ------------------------------------------------------------------------------

	public RDFDAO build(Resource resource) throws DAONotFoundException {

		if (resource instanceof InformationSource) {
			RDFDAO dao = new InformationSourceRDFDAO();
			dao.init(this.parameters);

			return dao;
		} else if (resource instanceof InformationSourceSubscription) {
			RDFDAO dao = new InformationSourceSubscriptionRDFDAO();
			dao.init(this.parameters);

			return dao;
		} else if (resource instanceof Feed) {
			RDFDAO dao = new FeedRDFDAO();
			dao.init(this.parameters);
			return dao;
		} else if (resource instanceof Item) {
			RDFDAO dao = new ItemRDFDAO();
			dao.init(this.parameters);
			return dao;
		} else if (resource instanceof User) {
			RDFDAO dao = new UserRDFDAO();
			dao.init(this.parameters);
			return dao;
		} else {
			throw new DAONotFoundException("For resource " + resource);
		}

	}

	// ------------------------------------------------------------------------------

	public RDFDAO build(Selector selector) throws DAONotFoundException {
		String typeSelector = selector.getProperty(SelectorHelper.TYPE);
		if (typeSelector == null) {
			throw new DAONotFoundException("No column name specified");
		} else if (typeSelector
				.equals(InformationSourceRDFHelper.INFORMATION_SOURCE_CLASS)) {

			InformationSourceRDFDAO informationSourceDAO = new InformationSourceRDFDAO();
			informationSourceDAO.init(this.parameters);
			return informationSourceDAO;

		} else if (typeSelector
				.equals(InformationSourceSubscriptionRDFHelper.INFORMATION_SOURCE_SUBSCRIPTION_CLASS)) {

			InformationSourceSubscriptionRDFDAO informationSourceSubscriptionDAO = new InformationSourceSubscriptionRDFDAO();
			informationSourceSubscriptionDAO.init(this.parameters);
			return informationSourceSubscriptionDAO;

		} else if (typeSelector.equals(UserRDFHelper.USER_CLASS)) {

			UserRDFDAO informationSourceDAO = new UserRDFDAO();
			informationSourceDAO.init(this.parameters);
			return informationSourceDAO;
		} else if (typeSelector.equals(FeedRDFHelper.FEED_CLASS)) {

			FeedRDFDAO feedDAO = new FeedRDFDAO();
			feedDAO.init(this.parameters);
			return feedDAO;
		} else if (typeSelector.equals(FeedRDFHelper.ITEM_CLASS)) {

			ItemRDFDAO itemDAO = new ItemRDFDAO();
			itemDAO.init(this.parameters);
			return itemDAO;

		} else {
			throw new DAONotFoundException("Unknown type " + typeSelector);
		}
	}

	// ------------------------------------------------------------------------------

}
