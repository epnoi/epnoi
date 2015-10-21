package org.epnoi.uia.core;

import java.net.URL;
import java.util.logging.Logger;

import org.epnoi.model.Parameter;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.parameterization.ParametersModel;
import org.epnoi.model.parameterization.ParametersModelReader;

public class CoreUtility {
	// ---------------------------------------------------------------------------------
	private static final Logger logger = Logger.getLogger(CoreUtility.class
			.getName());

	public static Core getUIACore() {

	return getUIACore("CoreUtility.xml");

	}

	public static Core getUIACore(String configurationFile) {

		long time = System.currentTimeMillis();
		Core core = new CoreImpl();
		ParametersModel parametersModel = readParameters(configurationFile);
		logger.info("Reading the following paramaters for the UIA: "+parametersModel);
		try {
			core.init(parametersModel);
		} catch (EpnoiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}

		long afterTime = System.currentTimeMillis();
		logger.info("It took " + (Long) (afterTime - time) / 1000.0
				+ "to load the UIA core");

		return core;

	}

	public static ParametersModel readParameters(){
		return readParameters("CoreUtility.xml");
	}

	public static ParametersModel readParameters(String configurationFile) {
		ParametersModel parametersModel = null;

		try {

			URL configFileURL = CoreMain.class.getResource("CoreUtility.xml");

			parametersModel = ParametersModelReader.read(configFileURL
					.getPath());

		} catch (Exception e) {

			e.printStackTrace();
		}

		return parametersModel;
	}

	// ----------------------------------------------------------------------------------------

}