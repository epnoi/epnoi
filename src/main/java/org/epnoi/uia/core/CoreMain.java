package org.epnoi.uia.core;

import org.epnoi.uia.parameterization.ParametersModel;
import org.epnoi.uia.parameterization.ParametersModelReader;
import org.epnoi.uia.rest.services.response.InformationStore;

import epnoi.logging.EpnoiLogger;


public class CoreMain {

	public static void main(String[] args) {
		/*
		EpnoiLogger.setup("");
		System.out.println("Starting the EpnoiCoreMain");
		Core epnoiCore = new Core();

		ParametersModel parametersModel = ParametersModelReader
				.read("/parametersModelPath.xml");

		epnoiCore.init(parametersModel);

		

		epnoiCore.close();
		*/
		InformationStore informationStore = new InformationStore();
		System.out.println("--------------------------> "+informationStore.getClass().getName());
	}

}
