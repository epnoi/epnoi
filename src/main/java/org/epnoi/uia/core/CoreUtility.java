package org.epnoi.uia.core;

import java.net.URL;
import java.util.logging.Logger;

import org.epnoi.uia.informationstore.dao.rdf.FeedRDFHelper;
import org.epnoi.uia.parameterization.ParametersModel;
import org.epnoi.uia.parameterization.ParametersModelReader;
import org.epnoi.uia.search.SearchContext;
import org.epnoi.uia.search.SearchResult;
import org.epnoi.uia.search.select.SelectExpression;

import epnoi.model.Resource;

public class CoreUtility {
	// ---------------------------------------------------------------------------------
	private static final Logger logger = Logger.getLogger(CoreUtility.class
			.getName());

	public static Core getUIACore() {

		long time = System.currentTimeMillis();
		Core core = new Core();
		ParametersModel parametersModel = _readParameters();
		core.init(parametersModel);

		long afterTime = System.currentTimeMillis();
		logger.info("It took " + (Long) (afterTime - time) / 1000.0
				+ "to load the UIA core");

		return core;

	}

	// ----------------------------------------------------------------------------------------

	public static ParametersModel _readParameters() {
		ParametersModel parametersModel = null;

		try {

			URL configFileURL = CoreMain.class.getResource("CoreUtility.xml");

			parametersModel = ParametersModelReader.read(configFileURL
					.getPath());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return parametersModel;
	}

}