package org.epnoi.uia.nlp;

import gate.Gate;
import gate.util.GateException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.epnoi.uia.core.Core;
import org.epnoi.uia.parameterization.ParametersModel;

public class NLPHandler {
	private Core core;
	private ParametersModel parameters;
	
	private static final Logger logger = Logger.getLogger(NLPHandler.class.getName());
	
	// ----------------------------------------------------------------------------------------------------------

	private void init(Core core, ParametersModel parameters){
		this.core=core;
		this.parameters=parameters;
	}
	
	/**
		 * Initializtion of the Gate natural language processing framework and the
		 * needed Gate plugins
		 */

		private void _initGATE() {
			logger.info("Initializing Gate");
			String gateHomePath = this.parameters.getGatePath();
			String pluginsPath = gateHomePath + "/plugins";
			String grammarsPath = gateHomePath + "/grammars/nounphrases";

			logger.info("The gateHomePath is set to " + gateHomePath
					+ ", the pluginsPath is set to " + pluginsPath
					+ " and finally the grammarsPath is set to " + grammarsPath);

			File gateHomeDirectory = new File(gateHomePath);
			File pluginsDirectory = new File(pluginsPath);

			Gate.setPluginsHome(pluginsDirectory);

			Gate.setGateHome(gateHomeDirectory);
			Gate.setUserConfigFile(new File(gateHomeDirectory, "user-gate.xml"));

			try {
				Gate.init(); // to prepare the GATE library

				URL anniePlugin = new File(pluginsDirectory, "ANNIE").toURI()
						.toURL();

				Gate.getCreoleRegister().registerDirectories(anniePlugin);

				URL stanfordCoreNLPPlugin = new File(pluginsDirectory,
						"Parser_Stanford").toURI().toURL();
				Gate.getCreoleRegister().registerDirectories(stanfordCoreNLPPlugin);

			} catch (MalformedURLException | GateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		// ----------------------------------------------------------------------------------------------------------
}
