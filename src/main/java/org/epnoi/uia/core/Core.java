package org.epnoi.uia.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.epnoi.uia.harvester.rss.RSSHarvester;
import org.epnoi.uia.hoarder.RSSHoarder;
import org.epnoi.uia.informationaccess.InformationAccess;
import org.epnoi.uia.informationaccess.InformationAccessImplementation;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreFactory;
import org.epnoi.uia.informationstore.InformationStoreHelper;
import org.epnoi.uia.parameterization.ParametersModel;
import org.epnoi.uia.parameterization.RSSHarvesterParameters;
import org.epnoi.uia.parameterization.RSSHoarderParameters;
import org.epnoi.uia.parameterization.SOLRInformationStoreParameters;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;

public class Core {

	private static final Logger logger = Logger.getLogger(Core.class.getName());

	private HashMap<String, InformationStore> informationStores;
	private HashMap<String, List<InformationStore>> informationStoresByType;

	private RSSHoarder rssHoarder;
	private RSSHarvester rssHarvester;
	private InformationAccess informationAccess;

	private ParametersModel parametersModel = null;

	// --------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * The initialization method for the epnoiCore
	 * 
	 * @param initializationProperties
	 *            The properties that define the characteristics of the
	 *            epnoiCore.
	 */

	public synchronized void init(ParametersModel parametersModel) {
		logger.info("Initializing the epnoi uia core");
		this.informationStores = new HashMap<String, InformationStore>();
		this.informationStoresByType = new HashMap<String, List<InformationStore>>();
		this.parametersModel = parametersModel;

		this._informationStoresInitialization();
		this._initInformationAccess();
		/*
		 * this._hoardersInitialization(); this._harvestersInitialization();
		 */
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

			_addInformationStoreByType(newInformationStore,
					InformationStoreHelper.RDF_INFORMATION_STORE);

		}
		System.out.println("---> "+parametersModel.getSolrInformationStore());
		for (SOLRInformationStoreParameters solrInformationStoreParameters : parametersModel
				.getSolrInformationStore()) {
			System.out.println("-------------> "
					+ solrInformationStoreParameters);
			System.out.println("-- http://"
					+ solrInformationStoreParameters.getHost() + ":"
					+ solrInformationStoreParameters.getPort()
					+ solrInformationStoreParameters.getPath());

			InformationStore newInformationStore = InformationStoreFactory
					.buildInformationStore(solrInformationStoreParameters,
							parametersModel);

			this.informationStores.put(solrInformationStoreParameters.getURI(),
					newInformationStore);

			_addInformationStoreByType(newInformationStore,
					InformationStoreHelper.SOLR_INFORMATION_STORE);

		}
	}

	private void _initInformationAccess() {
		this.informationAccess = new InformationAccessImplementation(this);
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------------

	private void _addInformationStoreByType(InformationStore informationStore,
			String type) {
		List<InformationStore> informationsStoresOfType = this.informationStoresByType
				.get(type);
		if (informationsStoresOfType == null) {
			informationsStoresOfType = new ArrayList<InformationStore>();
			this.informationStoresByType.put(type, informationsStoresOfType);
		}
		informationsStoresOfType.add(informationStore);
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------------

	private void _hoardersInitialization() {
		logger.info("Initializing hoarders");
		RSSHoarderParameters parameters = this.parametersModel.getRssHoarder();
		this.rssHoarder = new RSSHoarder(parameters);
		this.rssHoarder.start();
		System.out
				.println("------------------------------------------------------------------------------------------!!!!!!!");
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------------

	private void _harvestersInitialization() {
		logger.info("Initializing harvesters");
		RSSHarvesterParameters parameters = this.parametersModel
				.getRssHarvester();
		this.rssHarvester = new RSSHarvester(this, parameters);
		this.rssHarvester.start();
		System.out
				.println("------------------------------------------------------------------------------------------!!!!!!!");
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------------

	public Collection<InformationStore> getInformationStores() {
		return this.informationStores.values();
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------------

	public List<InformationStore> getInformationStoresByType(String type) {
		return this.informationStoresByType.get(type);
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------------

	public InformationAccess getInformationAccess() {
		return this.informationAccess;
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
