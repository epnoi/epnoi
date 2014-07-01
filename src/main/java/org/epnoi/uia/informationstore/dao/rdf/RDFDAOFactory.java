package org.epnoi.uia.informationstore.dao.rdf;

import org.epnoi.model.Annotation;
import org.epnoi.model.Feed;
import org.epnoi.model.InformationSource;
import org.epnoi.model.InformationSourceSubscription;
import org.epnoi.model.Item;
import org.epnoi.model.Paper;
import org.epnoi.model.Resource;
import org.epnoi.model.User;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.exception.DAONotFoundException;
import org.epnoi.uia.parameterization.InformationStoreParameters;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;

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
		} else if (resource instanceof Paper) {
			RDFDAO dao = new PaperRDFDAO();
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
		} else if (resource instanceof Paper) {
			RDFDAO dao = new PaperRDFDAO();
			dao.init(this.parameters);
			return dao;
		} else if (resource instanceof Annotation) {
			RDFDAO dao = new AnnotationRDFDAO();
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
		} else if (typeSelector.equals(RDFHelper.PAPER_CLASS)) {
			RDFDAO dao = new PaperRDFDAO();
			dao.init(this.parameters);
			return dao;
		} else if (typeSelector.equals(AnnotationRDFHelper.ANNOTATION_CLASS)) {

			AnnotationRDFDAO annotationDAO = new AnnotationRDFDAO();
			annotationDAO.init(this.parameters);
			return annotationDAO;

		} else {
			throw new DAONotFoundException("Unknown type " + typeSelector);
		}
	}

	// ------------------------------------------------------------------------------

}
