package org.epnoi.uia.informationstore;

import java.util.ArrayList;
import java.util.List;

import org.epnoi.uia.informationstore.dao.solr.SOLRDAO;
import org.epnoi.uia.informationstore.dao.solr.SOLRDAOFactory;
import org.epnoi.uia.parameterization.InformationStoreParameters;
import org.epnoi.uia.parameterization.SOLRInformationStoreParameters;

import epnoi.model.Context;
import epnoi.model.InformationSource;
import epnoi.model.Resource;

public class SOLRInformationStore implements InformationStore {

	SOLRInformationStoreParameters parameters;
	SOLRDAOFactory datoFactory;

	// ------------------------------------------------------------------------

	public void close() {
		// TODO Auto-generated method stub

	}

	// ------------------------------------------------------------------------

	public void init(InformationStoreParameters parameters) {

		this.parameters = (SOLRInformationStoreParameters) parameters;
		this.datoFactory = new SOLRDAOFactory(this.parameters);

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

		if (!solrDAO.exists(resource.getURI())) {
			System.out.println("The information source doesn't exist");

			solrDAO.create(resource);
		} else {
			System.out.println("The information source already exists!");
			solrDAO.create(resource);
		}

	}

	public void put(Resource resource, Context context) {

	}

	// ------------------------------------------------------------------------

	public Resource get(String URI) {
		return new InformationSource();
	}

	// ------------------------------------------------------------------------

	public List<String> query(String queryExpression) {
		return new ArrayList<String>();
	}
}
