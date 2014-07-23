package org.epnoi.uia.core;

import java.net.URL;
import java.util.logging.Logger;

import org.epnoi.uia.parameterization.ParametersModel;
import org.epnoi.uia.parameterization.ParametersModelReader;

public class CoreMain {
	private static final Logger logger = Logger.getLogger(CoreMain.class
			.getName());

	public static void main(String[] args) {

		String feedTitle ="arXiv";
		Core core = getUIACore();
/*
		SelectExpression selectExpression = new SelectExpression();

		String queryExpression = "SELECT ?uri FROM <{GRAPH}> " + "{"
				+ "?uri a <{ITEM_CLASS}> . " + "?uriFeed a <{FEED_CLASS}> . "
				+ "?uriFeed <{AGGREGATES_PROPERTY}> ?uri . "
				+ "?uriFeed <{TITLE_PROPERTY}> \""+feedTitle+"\" . "
				+ " }";

		queryExpression = queryExpression.replace("{GRAPH}", "http://feedTest")
				.replace("{AGGREGATES_PROPERTY}", RDFOAIOREHelper.AGGREGATES_PROPERTY)
				.replace("{TITLE_PROPERTY}", RDFHelper.TITLE_PROPERTY)
				.replace("{ITEM_CLASS}", FeedRDFHelper.ITEM_CLASS)
				.replace("{FEED_CLASS}", FeedRDFHelper.FEED_CLASS);

		selectExpression.setSparqlExpression(queryExpression);
		core.getSearchHandler().search(selectExpression, new SearchContext());
*/
	}

	// ---------------------------------------------------------------------------------

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

			URL configFileURL = CoreMain.class.getResource("uiaCoreMain.xml");

			parametersModel = ParametersModelReader.read(configFileURL
					.getPath());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return parametersModel;
	}
}
