package org.epnoi.uia.informationstore.dao.solr;

import java.util.logging.Logger;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.epnoi.uia.parameterization.InformationStoreParameters;
import org.epnoi.uia.parameterization.SOLRInformationStoreParameters;

import epnoi.model.Context;
import epnoi.model.Resource;

public abstract class SOLRDAO {
	private static final Logger logger = Logger.getLogger(SOLRDAO.class
			.getName());
	private String solrURL = "http://localhost:8983";

	protected SOLRInformationStoreParameters parameters;
	protected HttpSolrServer server;

	abstract public void create(Resource resource);
	
	abstract public void create(Resource resource, Context context);

	// --------------------------------------------------------------------------------

	public void init(InformationStoreParameters parameters) {

		System.out.println(".............................................. "
				+ parameters);
		this.parameters = (SOLRInformationStoreParameters) parameters;
		if ((this.parameters.getPort() != null)
				&& (this.parameters.getHost() != null)) {

			this.solrURL = "http://" + this.parameters.getHost() + ":"
					+ this.parameters.getPort() + "/"
					+ this.parameters.getPath() + "/"
					+ this.parameters.getCore();
			logger.info("Initializing in the URL " + this.solrURL);
			this.server = new HttpSolrServer(this.solrURL);

		}
	}

	// --------------------------------------------------------------------------------

	public Boolean exists(String URI) {

		return true;
	}

	// --------------------------------------------------------------------------------

	public static boolean test(InformationStoreParameters parameters) {
		SOLRInformationStoreParameters testParameters = (SOLRInformationStoreParameters) parameters;
		boolean testResult;
		try {
			String solrURL = "http://" + parameters.getHost() + ":"
					+ testParameters.getPort() + "/" + testParameters.getPath()
					+ "/" + testParameters.getCore();

			logger.info("Testing the URL " + solrURL);
			HttpSolrServer server = new HttpSolrServer(solrURL);
			server.commit();

			testResult = true;
		} catch (Exception e) {
			logger.info("The following exception was obntained in the test "
					+ e.getMessage());
			testResult = false;
		}
		return testResult;
	}

	// --------------------------------------------------------------------------------

	protected QueryResponse makeQuery(String query) throws SolrServerException {
		ModifiableSolrParams solrParams = new ModifiableSolrParams();
		// solrParams.set("collectionName", myCollection);
		solrParams.set("username", "admin");
		solrParams.set("password", "password");
		// solrParams.set("facet", facet);
		solrParams.set("q", query);
		// solrParams.set("start", start);
		// solrParams.set("rows", nbDocuments);
		System.out.println("------------> " + solrParams.toString());
		return server.query(solrParams);
	}
}