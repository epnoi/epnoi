package org.epnoi.uia.nlp.gate;

import gate.Gate;
import gate.util.GateException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.epnoi.uia.nlp.NLPHandler;
import org.epnoi.uia.parameterization.ParametersModel;

public class GATEInitializer {


	private ParametersModel parameters;

	private static final Logger logger = Logger.getLogger(NLPHandler.class
			.getName());

		// ----------------------------------------------------------------------------------------------------------

	/**
	 * Initializtion of the Gate natural language processing framework and the
	 * needed Gate plugins
	 */

	public void init(ParametersModel parameters) {
		this.parameters=parameters;
		logger.info("Initializing Gate");
		String gateHomePath = this.parameters.getNlp().getGatePath();
		String pluginsPath = gateHomePath + "/plugins";
		// String grammarsPath = gateHomePath + "/grammars/nounphrases";

		logger.info("The gateHomePath is set to " + gateHomePath
				+ ", the pluginsPath is set to " + pluginsPath);

		File gateHomeDirectory = new File(gateHomePath);
		File pluginsDirectory = new File(pluginsPath);

		Gate.setPluginsHome(pluginsDirectory);

		Gate.setGateHome(gateHomeDirectory);
		Gate.setUserConfigFile(new File(gateHomeDirectory, "user-gate.xml"));

		try {
			Gate.init(); // to prepare the GATE library

			_initGATEPlugins(pluginsDirectory);

		} catch (MalformedURLException | GateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	//-----------------------------------------------------------------------------------------------------
	
	private void _initGATEPlugins(File pluginsDirectory)
			throws MalformedURLException, GateException {
		
		//ANNIE Plugin----------------------------------------------------------------------------------
		URL anniePlugin = new File(pluginsDirectory, "ANNIE").toURI().toURL();

		Gate.getCreoleRegister().registerDirectories(anniePlugin);

		/*Desactivated 
		URL stanfordCoreNLPPlugin = new File(pluginsDirectory,
				"Parser_Stanford").toURI().toURL();
		Gate.getCreoleRegister().registerDirectories(stanfordCoreNLPPlugin);
		*/
	}

	// ----------------------------------------------------------------------------------------------------------
}
