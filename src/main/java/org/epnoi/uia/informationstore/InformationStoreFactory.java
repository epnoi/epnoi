package org.epnoi.uia.informationstore;

import org.epnoi.uia.parameterization.InformationStoreParameters;
import org.epnoi.uia.parameterization.ParametersModel;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;

public class InformationStoreFactory {

	public static InformationStore buildInformationStore(
			InformationStoreParameters informationStoreParameters,
			ParametersModel parametersModel) {
		System.out.println(">>>>>> " + informationStoreParameters);
													
		if (informationStoreParameters instanceof VirtuosoInformationStoreParameters) {
			VirtuosoInformationStore newInformationStore= new VirtuosoInformationStore();
			newInformationStore.init(informationStoreParameters);
			return newInformationStore;
		}

		return null;
	}

}
