package org.epnoi.uia.informationstore;

import org.epnoi.uia.informationstore.dao.rdf.RDFDAO;
import org.epnoi.uia.parameterization.InformationStoreParameters;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;

public class VirtuosoInformationStore implements InformationStore {
	VirtuosoInformationStoreParameters parameters;

	public void close() {
		// TODO Auto-generated method stub

	}

	public void init(InformationStoreParameters parameters) {
		this.parameters = (VirtuosoInformationStoreParameters) parameters;

	}

	public boolean test() {
		boolean testResult = true;
		try {
			System.out.println();
			RDFDAO dao = new RDFDAO();
			dao.init(this.parameters);
		} catch (Exception e) {
			e.printStackTrace();
			testResult = false;
		}
		return testResult;
	}

	public InformationStoreParameters getParameters() {
		return this.parameters;
	}

}
