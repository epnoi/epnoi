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

public class CoreMainSearch {
	// ---------------------------------------------------------------------------------
	private static final Logger logger = Logger.getLogger(CoreMainSearch.class
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

			URL configFileURL = CoreMain.class.getResource("CoreMainSearch.xml");

			parametersModel = ParametersModelReader.read(configFileURL
					.getPath());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return parametersModel;
	}

	// ----------------------------------------------------------------------------------------

	public static void main(String[] args) {

		Core core = getUIACore();

		SelectExpression selectExpression = new SelectExpression();

		selectExpression.setSolrExpression("content:scalability");

		SearchContext searchContext = new SearchContext();
		searchContext.getFacets().add("date");
		searchContext.getFilterQueries().add("date:\"2013-12-06T17:54:21Z\"");
		//searchContext.getFilterQueries().add("date:\"2014-03-04T17:56:05Z\"");

		SearchResult searchResult = core.getSearchHandler().search(
				selectExpression, searchContext);
		System.out.println("Facets ---> " + searchResult.getFacets().size());
		
		
		
		Resource resource =core.getInformationAccess().get("http://rss.slashdot.org/~r/Slashdot/slashdot/~3/-FraYC4r__w/story01.htm", FeedRDFHelper.ITEM_CLASS);
		System.out.println("---> "+resource);
	}
}