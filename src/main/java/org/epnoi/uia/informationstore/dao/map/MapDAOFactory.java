package org.epnoi.uia.informationstore.dao.map;

import org.epnoi.model.Paper;
import org.epnoi.model.Resource;
import org.epnoi.model.WikipediaPage;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.exception.DAONotFoundException;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.parameterization.InformationStoreParameters;
import org.epnoi.uia.parameterization.MapInformationStoreParameters;

public class MapDAOFactory {

	private MapInformationStoreParameters parameters;

	// --------------------------------------------------------------------------------

	public MapDAOFactory(InformationStoreParameters parameters) {
		this.parameters = (MapInformationStoreParameters) parameters;
	}

	// --------------------------------------------------------------------------------

	public MapDAO build(Resource resource) throws DAONotFoundException {
		if (resource instanceof WikipediaPage) {
			//WikipediaPageMapDAO wikipediaPageDAO = new WikipediaPageMapDAO();
			
			TestMapDAO wikipediaPageDAO = new TestMapDAO();
			wikipediaPageDAO.init(parameters);
			return wikipediaPageDAO;
		}
		if (resource instanceof Paper) {
			TestMapDAO wikipediaPageDAO = new TestMapDAO();
			wikipediaPageDAO.init(parameters);
			return wikipediaPageDAO;
		}
		throw new DAONotFoundException("Not implemented for the resource "
				+ resource);
	}

	// --------------------------------------------------------------------------------

	public MapDAO build(Selector selector) throws DAONotFoundException {
		String typeSelector = selector.getProperty(SelectorHelper.TYPE);
		if (typeSelector == null) {
			throw new DAONotFoundException("No type specified");
		} else if (typeSelector.equals(RDFHelper.WIKIPEDIA_PAGE_CLASS)) {
			//WikipediaPageMapDAO wikipediaPaperDAO = new WikipediaPageMapDAO();
			TestMapDAO wikipediaPaperDAO = new TestMapDAO();
			wikipediaPaperDAO.init(parameters);
			return wikipediaPaperDAO;
		} else if (typeSelector.equals(RDFHelper.PAPER_CLASS)) {
			TestMapDAO wikipediaPaperDAO = new TestMapDAO();
			wikipediaPaperDAO.init(parameters);
			return wikipediaPaperDAO;
		} else {
			throw new DAONotFoundException(
					"Unknown dao for the resource class " + typeSelector);
		}
	}
}
