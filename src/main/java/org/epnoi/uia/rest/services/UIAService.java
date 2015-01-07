package org.epnoi.uia.rest.services;

import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.epnoi.uia.core.Core;
import org.epnoi.uia.parameterization.ParametersModel;
import org.epnoi.uia.parameterization.ParametersModelReader;

import com.sun.jersey.api.Responses;

public abstract class UIAService {

	protected Logger logger = null;

	private String UIA_CORE_ATTRIBUTE = "UIA_CORE";

	protected ParametersModel parametersModel;
	@Context
	protected ServletContext context;
	@Context
	protected UriInfo uriInfo;

	protected Core core = null;

	// ----------------------------------------------------------------------------------------

	protected Core getUIACore() {

		this.core = (Core) this.context.getAttribute(UIA_CORE_ATTRIBUTE);
		if (this.core == null) {
			System.out.println("Loading the model!");
			long time = System.currentTimeMillis();
			this.core = new Core();
			parametersModel = this._readParameters();
			this.core.init(parametersModel);
			this.context.setAttribute(UIA_CORE_ATTRIBUTE, core);
			long afterTime = System.currentTimeMillis();
			System.out.println("It took " + (Long) (afterTime - time) / 1000.0
					+ "to load the model");
		}
		return this.core;

	}

	// ----------------------------------------------------------------------------------------

	public ParametersModel _readParameters() {
		ParametersModel parametersModel = null;

		try {

			String configFileURL = context.getRealPath("/WEB-INF/uia.xml");

			System.out.println("AQUI --->" + configFileURL);
			parametersModel = ParametersModelReader.read(configFileURL);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Before we start the server we translate those properties that are
		// related to the
		// path where the epnoi server is deployed in order to have complete
		// routes
		// parametersModel.resolveToAbsolutePaths(EpnoiServer.class);

		return parametersModel;
	}

	

}
