package org.epnoi.uia.core;

import java.net.URL;
import java.util.logging.Logger;

import org.epnoi.uia.informationstore.dao.rdf.SearchRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.UserRDFHelper;
import org.epnoi.uia.parameterization.ParametersModel;
import org.epnoi.uia.parameterization.ParametersModelReader;

import epnoi.model.Search;
import epnoi.model.User;

public class CoreMainUser {
	// ---------------------------------------------------------------------------------
	private static final Logger logger = Logger.getLogger(CoreMainUser.class
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

			URL configFileURL = CoreMain.class.getResource("uiaCoreMain.xml");

			parametersModel = ParametersModelReader.read(configFileURL
					.getPath());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return parametersModel;
	}

	public static void main(String[] args) {

		Core core = getUIACore();

		User user = (User) core.getInformationAccess().get("http://userSara",
				UserRDFHelper.USER_CLASS);
		System.out.println("The readed user is " + user);

		User unknownUser = (User) core.getInformationAccess().get(
				"http://userSara2", UserRDFHelper.USER_CLASS);
		System.out.println("The readed user is " + unknownUser);

		Search readedSearch = (Search) core.getInformationAccess().get(
				"http://searchE", SearchRDFHelper.SEARCH_CLASS);
		
		System.out.println("The readed search is " + readedSearch);
	}
}
