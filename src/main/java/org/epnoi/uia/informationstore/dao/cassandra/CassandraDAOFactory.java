package org.epnoi.uia.informationstore.dao.cassandra;

import org.epnoi.model.Feed;
import org.epnoi.model.Paper;
import org.epnoi.model.Resource;
import org.epnoi.model.User;
import org.epnoi.model.WikipediaPage;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.exception.DAONotFoundException;
import org.epnoi.uia.informationstore.dao.rdf.FeedRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.SearchRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.UserRDFHelper;
import org.epnoi.uia.parameterization.CassandraInformationStoreParameters;
import org.epnoi.uia.parameterization.InformationStoreParameters;

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
		}
		throw new DAONotFoundException("Not implemented for the resource "
				+ resource);
	}

	// --------------------------------------------------------------------------------

	public CassandraDAO build(Selector selector) throws DAONotFoundException {
		String typeSelector = selector.getProperty(SelectorHelper.TYPE);
		if (typeSelector == null) {
			throw new DAONotFoundException("No column name specified");
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
		} else if (typeSelector.equals(RDFHelper.WIKIPEDIA_PAGE_CLASS)) {
			WikipediaPageCassandraDAO wikipediaPaperDAO = new WikipediaPageCassandraDAO();
			wikipediaPaperDAO.init();
			return wikipediaPaperDAO;

		}

		else {
			throw new DAONotFoundException("Unknown wrapper for the resource class " + typeSelector);
		}
	}
}
