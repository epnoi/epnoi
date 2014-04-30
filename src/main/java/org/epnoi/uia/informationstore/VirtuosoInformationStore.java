package org.epnoi.uia.informationstore;

import java.util.List;

import org.epnoi.uia.informationstore.dao.cassandra.CassandraDAO;
import org.epnoi.uia.informationstore.dao.rdf.RDFDAO;
import org.epnoi.uia.informationstore.dao.rdf.RDFDAOFactory;
import org.epnoi.uia.informationstore.dao.rdf.RDFDAOQueryResolver;
import org.epnoi.uia.parameterization.InformationStoreParameters;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;
import org.epnoi.uia.search.SearchContext;
import org.epnoi.uia.search.select.SearchSelectResult;
import org.epnoi.uia.search.select.SelectExpression;

import epnoi.model.Context;
import epnoi.model.InformationSource;
import epnoi.model.Resource;

public class VirtuosoInformationStore implements InformationStore {
	VirtuosoInformationStoreParameters parameters;
	RDFDAOFactory datoFactory;
	RDFDAOQueryResolver queryResolver;

	// ------------------------------------------------------------------------

	public void close() {
		// TODO Auto-generated method stub

	}

	// ------------------------------------------------------------------------

	public void init(InformationStoreParameters parameters) {

		this.parameters = (VirtuosoInformationStoreParameters) parameters;
		this.datoFactory = new RDFDAOFactory(this.parameters);
		this.queryResolver = new RDFDAOQueryResolver();
		this.queryResolver.init(this.parameters);
	}

	// ------------------------------------------------------------------------

	public boolean test() {
		return RDFDAO.test(this.parameters);
	}

	// ------------------------------------------------------------------------

	public InformationStoreParameters getParameters() {
		return this.parameters;
	}

	// ------------------------------------------------------------------------

	public void put(Resource resource, Context context) {
		RDFDAO rdfDAO = this.datoFactory.build(resource);
		rdfDAO.create(resource, context);

	}

	// ------------------------------------------------------------------------

	public Resource get(Selector selector) {
		RDFDAO dao = this.datoFactory.build(selector);

		Resource resource = dao.read(selector.getProperty(SelectorHelper.URI));
		return resource;
	}

	// ------------------------------------------------------------------------

	public void remove(Selector selector) {
		RDFDAO dao = this.datoFactory.build(selector);

		dao.remove(selector.getProperty(SelectorHelper.URI));

	}

	// ------------------------------------------------------------------------

	public List<String> query(String queryExpression) {

		return this.queryResolver.query(queryExpression);
	}

	// ------------------------------------------------------------------------

	@Override
	public SearchSelectResult query(SelectExpression selectionExpression,
			SearchContext searchContext) {
		// TODO Auto-generated method stub
		return null;
	}

	// ------------------------------------------------------------------------

	public void update(Resource resource) {
		RDFDAO rdfDAO = this.datoFactory.build(resource);

		rdfDAO.update(resource);
	}

}
