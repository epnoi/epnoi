package org.epnoi.uia.informationstore;

import org.epnoi.uia.informationstore.dao.solr.SOLRDAO;
import org.epnoi.uia.informationstore.dao.solr.SOLRDAOFactory;
import org.epnoi.uia.parameterization.InformationStoreParameters;
import org.epnoi.uia.parameterization.SOLRInformationStoreParameters;

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

		SOLRDAO rdfDAO = this.datoFactory.build(resource);

		if (!rdfDAO.exists(resource.getURI())) {
			System.out.println("The information source doesn't exist");

			rdfDAO.create(resource);
		} else {
			System.out.println("The information source already exists!");
			rdfDAO.create(resource);
		}

	}
	
	// ------------------------------------------------------------------------

	public Resource get(String URI) {
		return new InformationSource();
	}
}
