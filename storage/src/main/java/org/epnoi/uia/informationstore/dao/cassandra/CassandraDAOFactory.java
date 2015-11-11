package org.epnoi.uia.informationstore.dao.cassandra;

import org.epnoi.model.*;
import org.epnoi.model.parameterization.CassandraInformationStoreParameters;
import org.epnoi.model.parameterization.InformationStoreParameters;
import org.epnoi.model.rdf.FeedRDFHelper;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.model.rdf.SearchRDFHelper;
import org.epnoi.model.rdf.UserRDFHelper;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.exception.DAONotFoundException;

public class CassandraDAOFactory {

	CassandraInformationStoreParameters parameters;

	// --------------------------------------------------------------------------------

	public CassandraDAOFactory(InformationStoreParameters parameters) {
		this.parameters = (CassandraInformationStoreParameters) parameters;
	}
	// --------------------------------------------------------------------------------

	public CassandraDAO build(Resource resource) throws DAONotFoundException {
		if (resource instanceof User) {
			UserCassandraDAO userDAO = new UserCassandraDAO();
			userDAO.init();
			return userDAO;
		} else if (resource instanceof Feed) {
			FeedCassandraDAO feedDAO = new FeedCassandraDAO();
			feedDAO.init();
			return feedDAO;
		} else if (resource instanceof Paper) {
			PaperCassandraDAO paperDAO = new PaperCassandraDAO();
			paperDAO.init();
			return paperDAO;
		} else if (resource instanceof WikipediaPage) {
			WikipediaPageCassandraDAO wikipediaPageDAO = new WikipediaPageCassandraDAO();
			wikipediaPageDAO.init();
			return wikipediaPageDAO;
		} else if (resource instanceof Term) {
			TermCassandraDAO termDAO = new TermCassandraDAO();
			termDAO.init();
			return termDAO;
		} else if (resource instanceof RelationalSentencesCorpus) {
			RelationalSentencesCorpusCassandraDAO relationalSentenceDAO = new RelationalSentencesCorpusCassandraDAO();
			relationalSentenceDAO.init();
			return relationalSentenceDAO;
		} else if (resource instanceof Domain) {
			DomainCassandraDAO domainDAO = new DomainCassandraDAO();
			domainDAO.init();
			return domainDAO;
		} else if (resource instanceof RelationsTable) {
			RelationsTableCassandraDAO relationsTableDAO = new RelationsTableCassandraDAO();
			relationsTableDAO.init();
			return relationsTableDAO;
		} else if (resource instanceof WikidataView) {
			WikidataViewCassandraDAO wikidataViewDAO = new WikidataViewCassandraDAO();
			wikidataViewDAO.init();
			return wikidataViewDAO;
		}
		throw new DAONotFoundException("Not implemented for the resource "
				+ resource);
	}

	// --------------------------------------------------------------------------------

	public CassandraDAO build(Selector selector) throws DAONotFoundException {
		String typeSelector = selector.getProperty(SelectorHelper.TYPE);
		if (typeSelector == null) {
			throw new DAONotFoundException("No column name specified");
		} else if (typeSelector.equals(RDFHelper.WIKIPEDIA_PAGE_CLASS)) {
			WikipediaPageCassandraDAO wikipediaPaperDAO = new WikipediaPageCassandraDAO();
			wikipediaPaperDAO.init();
			return wikipediaPaperDAO;
		} else if (typeSelector.equals(UserRDFHelper.USER_CLASS)) {

			UserCassandraDAO userDAO = new UserCassandraDAO();
			userDAO.init();
			return userDAO;
		} else if (typeSelector.equals(SearchRDFHelper.SEARCH_CLASS)) {

			SearchCassandraDAO searchDAO = new SearchCassandraDAO();
			searchDAO.init();
			return searchDAO;

		} else if (typeSelector.equals(FeedRDFHelper.ITEM_CLASS)) {

			ItemCassandraDAO itemDAO = new ItemCassandraDAO();
			itemDAO.init();
			return itemDAO;

		} else if (typeSelector.equals(RDFHelper.PAPER_CLASS)) {
			PaperCassandraDAO paperDAO = new PaperCassandraDAO();
			paperDAO.init();
			return paperDAO;

		} else if (typeSelector.equals(RDFHelper.TERM_CLASS)) {
			TermCassandraDAO termDAO = new TermCassandraDAO();
			termDAO.init();
			return termDAO;

		} else if (typeSelector
				.equals(RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS)) {
			RelationalSentencesCorpusCassandraDAO relationalSentenceDAO = new RelationalSentencesCorpusCassandraDAO();
			relationalSentenceDAO.init();
			return relationalSentenceDAO;
		} else if (typeSelector.equals(RDFHelper.DOMAIN_CLASS)) {
			DomainCassandraDAO domainDAO = new DomainCassandraDAO();
			domainDAO.init();
			return domainDAO;
		} else if (typeSelector.equals(RDFHelper.RELATIONS_TABLE_CLASS)) {

			RelationsTableCassandraDAO relationsTableDAO = new RelationsTableCassandraDAO();
			relationsTableDAO.init();
			return relationsTableDAO;
		} else if (typeSelector.equals(RDFHelper.WIKIDATA_VIEW_CLASS)) {

		WikidataViewCassandraDAO wikidataViewDAO = new WikidataViewCassandraDAO();
			wikidataViewDAO.init();
			return wikidataViewDAO;
		} else {
			throw new DAONotFoundException(
					"Unknown dao for the resource class " + typeSelector);
		}
	}
}
