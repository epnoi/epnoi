package org.epnoi.uia.informationstore;

import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.model.Selector;
import org.epnoi.model.modules.InformationStore;
import org.epnoi.model.parameterization.InformationStoreParameters;
import org.epnoi.model.parameterization.SOLRInformationStoreParameters;
import org.epnoi.model.search.SearchContext;
import org.epnoi.model.search.SearchSelectResult;
import org.epnoi.model.search.SelectExpression;
import org.epnoi.uia.informationstore.dao.solr.SOLRDAO;
import org.epnoi.uia.informationstore.dao.solr.SOLRDAOFactory;
import org.epnoi.uia.informationstore.dao.solr.SOLRDAOQueryResolver;

import java.util.ArrayList;
import java.util.List;

public class SOLRInformationStore implements InformationStore {

	SOLRInformationStoreParameters parameters;
	SOLRDAOFactory datoFactory;
	SOLRDAOQueryResolver queryResolver;

	// ------------------------------------------------------------------------

	public void close() {
		// TODO Auto-generated method stub

	}

	// ------------------------------------------------------------------------

	public void init(InformationStoreParameters parameters) {
		this.parameters = (SOLRInformationStoreParameters) parameters;
		this.datoFactory = new SOLRDAOFactory(this.parameters);
		this.queryResolver = new SOLRDAOQueryResolver();
		this.queryResolver.init(this.parameters);
	}

	// ------------------------------------------------------------------------

	public boolean test() {
		return SOLRDAO.test(this.parameters);
	}

	// ------------------------------------------------------------------------

	public InformationStoreParameters getParameters() {
		return this.parameters;
	}

	// ------------------------------------------------------------------------

	public void put(Resource resource) {

		SOLRDAO solrDAO = this.datoFactory.build(resource);

		if (!solrDAO.exists(resource.getUri())) {
			System.out.println("The information source doesn't exist");

			solrDAO.create(resource);
		} else {
			System.out.println("The information source already exists!");
			solrDAO.create(resource);
		}

	}

	// ------------------------------------------------------------------------

	public void put(Resource resource, Context context) {
		SOLRDAO solrDAO = this.datoFactory.build(resource);

		if (!solrDAO.exists(resource.getUri())) {
			// System.out.println("The information source doesn't exist");

			solrDAO.create(resource, context);
		} else {
			// System.out.println("The information source already exists!");
			solrDAO.create(resource, context);
		}
	}

	// ------------------------------------------------------------------------

	public Resource get(Selector selector) {
		// TODO Auto-generated method stub
		return null;
	}

	// ------------------------------------------------------------------------

	public void remove(Selector selector) {
		//System.out.println("Entra en SOLRInformationStore->remove "+selector);
		SOLRDAO solrDAO = this.datoFactory.build(selector);

		solrDAO.remove(selector.getProperty(SelectorHelper.URI));
	}

	// ------------------------------------------------------------------------

	public List<String> query(String queryExpression) {
		return new ArrayList<String>();
	}

	// ------------------------------------------------------------------------

	public void update(Resource resource) {
		// TODO Auto-generated method stub
	}

	// ------------------------------------------------------------------------

	@Override
	public SearchSelectResult query(SelectExpression selectionExpression,
			SearchContext searchContext) {
		return this.queryResolver.query(selectionExpression, searchContext);
	}

	// ------------------------------------------------------------------------
	
	@Override
	public boolean exists(Selector selector) {
		return this.queryResolver.exists(selector);
	}
	
	// ------------------------------------------------------------------------

	
}
