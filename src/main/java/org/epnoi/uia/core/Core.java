package org.epnoi.uia.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Logger;

import org.epnoi.uia.hoarder.RSSHoarder;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreFactory;
import org.epnoi.uia.parameterization.ParametersModel;
import org.epnoi.uia.parameterization.RSSHoarderParameters;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;
import org.epnoi.uia.rest.services.response.UIA;

public class Core {

	private static final Logger logger = Logger.getLogger(Core.class.getName());

	private HashMap<String, InformationStore> informationStores;
	private RSSHoarder rssHoarder;
	
	private ParametersModel parametersModel = null;
	

	// --------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * The initialization method for the epnoiCore
	 * 
	 * @param initializationProperties
	 *            The properties that define the characteristics of the
	 *            epnoiCore.
	 */

	public void init(ParametersModel parametersModel) {
		logger.info("Initializing the epnoi uia core");
		this.informationStores = new HashMap<String, InformationStore>();
		this.parametersModel = parametersModel;

		this._informationStoresInitialization();
		this._hoardersInitialization();

	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * Information Stores initialization
	 */

	private void _informationStoresInitialization() {

		logger.info("Initializing information stores");
		for (VirtuosoInformationStoreParameters virtuosoInformationStoreParameters : parametersModel
				.getVirtuosoInformationStore()) {
			System.out.println("-------------> "
					+ virtuosoInformationStoreParameters);
			System.out.println("-- http://"
					+ virtuosoInformationStoreParameters.getHost() + ":"
					+ virtuosoInformationStoreParameters.getPort()
					+ virtuosoInformationStoreParameters.getPath());

			InformationStore newInformationStore = InformationStoreFactory
					.buildInformationStore(virtuosoInformationStoreParameters,
							parametersModel);

			this.informationStores.put(
					virtuosoInformationStoreParameters.getURI(),
					newInformationStore);
		}

	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------------

	private void _hoardersInitialization() {
		logger.info("Initializing hoarders");
		RSSHoarderParameters parameters=this.parametersModel.getRssHoarder();
		this.rssHoarder= new RSSHoarder(parameters);
		this.rssHoarder.start();
		System.out.println("------------------------------------------------------------------------------------------!!!!!!!");
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------------

	public Collection<InformationStore> getInformationStores() {
		return this.informationStores.values();
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------------

	public boolean checkStatus(String informationStoreURI) {
		InformationStore informationStore = this.informationStores
				.get(informationStoreURI);
		return informationStore.test();
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------------

	public void close() {
		for (InformationStore dataSource : this.informationStores.values()) {
			dataSource.close();
		}

	}

}
