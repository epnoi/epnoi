package org.epnoi.uia.informationstore.dao.map;

import org.epnoi.model.Paper;
import org.epnoi.model.Resource;
import org.epnoi.model.Selector;
import org.epnoi.model.WikipediaPage;
import org.epnoi.model.parameterization.InformationStoreParameters;
import org.epnoi.model.parameterization.MapInformationStoreParameters;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.exception.DAONotFoundException;

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
			
			WikipediaPageMapDAO wikipediaPageDAO = new WikipediaPageMapDAO();
			wikipediaPageDAO.init(parameters);
			return wikipediaPageDAO;
		}
		if (resource instanceof Paper) {
			PaperMapDAO paperDAO = new PaperMapDAO();
			paperDAO.init(parameters);
			return paperDAO;
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
			WikipediaPageMapDAO wikipediaPaperDAO = new WikipediaPageMapDAO();
			wikipediaPaperDAO.init(parameters);
			return wikipediaPaperDAO;
		} else if (typeSelector.equals(RDFHelper.PAPER_CLASS)) {
			PaperMapDAO paperMapDAO = new PaperMapDAO();
			paperMapDAO.init(parameters);
			return paperMapDAO;
		} else {
			throw new DAONotFoundException(
					"Unknown dao for the resource class " + typeSelector);
		}
	}
}
