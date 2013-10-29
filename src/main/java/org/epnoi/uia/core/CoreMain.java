package org.epnoi.uia.core;

import org.epnoi.uia.parameterization.ParametersModel;
import org.epnoi.uia.parameterization.ParametersModelReader;

import epnoi.logging.EpnoiLogger;


public class CoreMain {

	public static void main(String[] args) {
		EpnoiLogger.setup("");
		System.out.println("Starting the EpnoiCoreMain");
		Core epnoiCore = new Core();

		ParametersModel parametersModel = ParametersModelReader
				.read("/parametersModelPath.xml");

		epnoiCore.init(parametersModel);

		

		epnoiCore.close();
	}

}
