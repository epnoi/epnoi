package org.epnoi.uia.informationstore.dao.rdf;

import org.epnoi.model.*;
import org.epnoi.model.parameterization.InformationStoreParameters;
import org.epnoi.model.parameterization.VirtuosoInformationStoreParameters;
import org.epnoi.model.rdf.*;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.exception.DAONotFoundException;


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
		} else if (resource instanceof ResearchObject) {
			RDFDAO dao = new ResearchObjectRDFDAO();
			dao.init(this.parameters);
			return dao;
		} else if (resource instanceof WikipediaPage) {
			RDFDAO dao = new WikipediaPageRDFDAO();
			dao.init(this.parameters);
			return dao;
		} else if (resource instanceof Term) {
			RDFDAO dao = new TermRDFDAO();
			dao.init(this.parameters);
			return dao;
		} else if (resource instanceof RelationalSentencesCorpus) {
			RDFDAO dao = new RelationalSentencesCorpusRDFDAO();
			dao.init(this.parameters);
			return dao;
		} else if (resource instanceof Domain) {
			RDFDAO dao = new DomainRDFDAO();
			dao.init(this.parameters);
			return dao;
		} else if (resource instanceof WikidataView) {
			RDFDAO dao = new WikidataViewRDFDAO();
			dao.init(this.parameters);
			return dao;
		}

		else {
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

		} else if (typeSelector.equals(RDFHelper.RESEARCH_OBJECT_CLASS)) {

			ResearchObjectRDFDAO researchObjectDAO = new ResearchObjectRDFDAO();
			researchObjectDAO.init(this.parameters);
			return researchObjectDAO;

		} else if (typeSelector.equals(RDFHelper.WIKIPEDIA_PAGE_CLASS)) {

			WikipediaPageRDFDAO wikipediaPageDAO = new WikipediaPageRDFDAO();
			wikipediaPageDAO.init(this.parameters);
			return wikipediaPageDAO;

		} else if (typeSelector.equals(RDFHelper.TERM_CLASS)) {

			TermRDFDAO termDAO = new TermRDFDAO();
			termDAO.init(this.parameters);
			return termDAO;
		} else if (typeSelector
				.equals(RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS)) {
			RDFDAO dao = new RelationalSentencesCorpusRDFDAO();
			dao.init(this.parameters);
			return dao;

		} else if (typeSelector.equals(RDFHelper.DOMAIN_CLASS)) {
			RDFDAO dao = new DomainRDFDAO();
			dao.init(this.parameters);
			return dao;

		} else if (typeSelector.equals(RDFHelper.WIKIDATA_VIEW_CLASS)) {
			RDFDAO dao = new WikidataViewRDFDAO();
			dao.init(this.parameters);
			return dao;

		} else {
			throw new DAONotFoundException("Unknown type " + typeSelector);
		}
	}

	// ------------------------------------------------------------------------------

}
