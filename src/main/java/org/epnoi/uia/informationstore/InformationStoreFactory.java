package org.epnoi.uia.informationstore;

import org.epnoi.uia.informationstore.exception.UnknownInformationStoreException;
import org.epnoi.uia.parameterization.CassandraInformationStoreParameters;
import org.epnoi.uia.parameterization.InformationStoreParameters;
import org.epnoi.uia.parameterization.MapInformationStoreParameters;
import org.epnoi.uia.parameterization.ParametersModel;
import org.epnoi.uia.parameterization.SOLRInformationStoreParameters;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;

public class InformationStoreFactory {

	public static InformationStore buildInformationStore(
			InformationStoreParameters informationStoreParameters,
			ParametersModel parametersModel)
			throws UnknownInformationStoreException {
		

		if (informationStoreParameters instanceof VirtuosoInformationStoreParameters) {
			VirtuosoInformationStore newInformationStore = new VirtuosoInformationStore();
			newInformationStore.init(informationStoreParameters);
			return newInformationStore;
		} else if (informationStoreParameters instanceof SOLRInformationStoreParameters) {
			SOLRInformationStore newInformationStore = new SOLRInformationStore();
			newInformationStore.init(informationStoreParameters);
			return newInformationStore;
		} else if (informationStoreParameters instanceof CassandraInformationStoreParameters) {
			CassandraInformationStore newInformationStore = new CassandraInformationStore();
			newInformationStore.init(informationStoreParameters);
			return newInformationStore;
		} else if (informationStoreParameters instanceof MapInformationStoreParameters) {
			MapInformationStore newInformationStore = new MapInformationStore();
			newInformationStore.init(informationStoreParameters);
			return newInformationStore;
		}else{
			throw new UnknownInformationStoreException(" "
					+ informationStoreParameters);
		}
	}

}
