package org.epnoi.uia.informationstore;

import org.epnoi.uia.informationstore.dao.rdf.InformationSourceRDFDAO;
import org.epnoi.uia.informationstore.dao.rdf.RDFDAO;
import org.epnoi.uia.informationstore.dao.rdf.RDFDAOFactory;
import org.epnoi.uia.parameterization.InformationStoreParameters;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;

import epnoi.model.InformationSource;
import epnoi.model.Resource;

public class VirtuosoInformationStore implements InformationStore {
	VirtuosoInformationStoreParameters parameters;
	RDFDAOFactory datoFactory;

	public void close() {
		// TODO Auto-generated method stub

	}

	public void init(InformationStoreParameters parameters) {

		this.parameters = (VirtuosoInformationStoreParameters) parameters;
		this.datoFactory = new RDFDAOFactory(this.parameters);

	}

	public boolean test() {

		return RDFDAO.test(this.parameters);
	}

	public InformationStoreParameters getParameters() {
		return this.parameters;
	}

	public void put(Resource resource) {

		RDFDAO rdfDAO = this.datoFactory.build(resource);

		if (!rdfDAO.exists(resource.getURI())) {
			System.out.println("The information source doesn't exist");

			rdfDAO.create(resource);
		} else {
			System.out.println("The information source already exists!");
			rdfDAO.create(resource);
		}

	}

	public Resource get(String URI) {
		return new InformationSource();
	}
}
