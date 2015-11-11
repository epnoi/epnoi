package org.epnoi.uia.informationstore;

import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.model.Selector;
import org.epnoi.model.modules.InformationStore;
import org.epnoi.model.parameterization.InformationStoreParameters;
import org.epnoi.model.parameterization.MapInformationStoreParameters;
import org.epnoi.model.search.SearchContext;
import org.epnoi.model.search.SearchSelectResult;
import org.epnoi.model.search.SelectExpression;
import org.epnoi.uia.informationstore.dao.map.MapDAO;
import org.epnoi.uia.informationstore.dao.map.MapDAOFactory;
import org.epnoi.uia.informationstore.dao.map.WikipediaPageMapDAO;

import java.util.List;

public class MapInformationStore implements InformationStore {
	private InformationStoreParameters parameters;

	private MapDAOFactory daoFactory;
	private WikipediaPageMapDAO testDAO;

	// -------------------------------------------------------------------------------------------------------------------

	@Override
	public void close() {

	}

	// -------------------------------------------------------------------------------------------------------------------

	@Override
	public void init(InformationStoreParameters parameters) {
		this.daoFactory = new MapDAOFactory(parameters);
		this.parameters=parameters;
		
		this.testDAO = new WikipediaPageMapDAO();
		this.testDAO.init((MapInformationStoreParameters) parameters);

	}

	// -------------------------------------------------------------------------------------------------------------------

	@Override
	public boolean test() {
		return true;
	}

	// -------------------------------------------------------------------------------------------------------------------

	@Override
	public Resource get(Selector selector) {
		throw (new RuntimeException(
				"The setContent method of the WikipediaPageCassandraDAO should not be invoked"));
	}

	// -------------------------------------------------------------------------------------------------------------------

	@Override
	public List<String> query(String queryExpression) {

		throw (new RuntimeException(
				"The setContent method of the WikipediaPageCassandraDAO should not be invoked"));
	}

	// -------------------------------------------------------------------------------------------------------------------

	@Override
	public SearchSelectResult query(SelectExpression selectionExpression,
			SearchContext searchContext) {

		throw (new RuntimeException(
				"The setContent method of the WikipediaPageCassandraDAO should not be invoked"));
	}

	// -------------------------------------------------------------------------------------------------------------------

	@Override
	public void put(Resource resource, Context context) {
		throw (new RuntimeException(
				"The setContent method of the WikipediaPageCassandraDAO should not be invoked"));

	}

	// -------------------------------------------------------------------------------------------------------------------

	@Override
	public void remove(Selector selector) {
		// TODO Auto-generated method stub

	}

	// -------------------------------------------------------------------------------------------------------------------

	@Override
	public boolean exists(Selector selector) {
		// TODO Auto-generated method stub
		return false;
	}

	// -------------------------------------------------------------------------------------------------------------------

	@Override
	public void update(Resource resource) {
		// TODO Auto-generated method stub

	}

	// -------------------------------------------------------------------------------------------------------------------

	@Override
	public InformationStoreParameters getParameters() {
		// TODO Auto-generated method stub
		return this.parameters;
	}

	// ------------------------------------------------------------------------

	public Content<Object> getAnnotatedContent(Selector selector) {
		MapDAO dao = this.daoFactory.build(selector);

		Content<Object> content = dao.getAnnotatedContent(selector);
		dao = null;
		return content;
	}

	// ------------------------------------------------------------------------

	public void setAnnotatedContent(Selector selector,
			Content<Object> annotatedContent) {

		MapDAO dao = this.daoFactory.build(selector);
		dao.setAnnotatedContent(selector, annotatedContent);
		dao = null;

	}

}
