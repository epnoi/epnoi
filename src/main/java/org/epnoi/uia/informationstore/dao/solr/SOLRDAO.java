package org.epnoi.uia.informationstore.dao.solr;

import java.util.logging.Logger;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.parameterization.InformationStoreParameters;
import org.epnoi.uia.parameterization.SOLRInformationStoreParameters;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;

import epnoi.model.Resource;

public abstract class SOLRDAO {
	private static final Logger logger = Logger.getLogger(SOLRDAO.class.getName());
	private String solrURL = "http://localhost:8983";

	protected SOLRInformationStoreParameters parameters;
	protected HttpSolrServer server;
	

	abstract public void create(Resource resource);

	// --------------------------------------------------------------------------------
	
	public void init(InformationStoreParameters parameters) {

		System.out.println(".............................................. "
				+ parameters);
		this.parameters = (SOLRInformationStoreParameters) parameters;
		if ((this.parameters.getPort() != null)
				&& (this.parameters.getHost() != null)) {
			
			this.solrURL="http://" + parameters.getHost()
					+ ":" + parameters.getPort()+"/"+parameters.getPath();
			this.server = new HttpSolrServer(this.solrURL);

		}
	}

	// --------------------------------------------------------------------------------
	
	public Boolean exists(String URI) {

		return true;
	}

	// --------------------------------------------------------------------------------
	
	public static boolean test(InformationStoreParameters parameters) {
		SOLRInformationStoreParameters testParameters = (SOLRInformationStoreParameters)parameters;
		boolean testResult;
		try {
			
			String solrURL = "http://" + parameters.getHost()
					+ ":" + testParameters.getPort()+"/"+testParameters.getPath()+"/"+testParameters.getCore();
			
			logger.info("Testing the URL "+solrURL);
			HttpSolrServer server = new HttpSolrServer(solrURL);
			server.commit();
			
			testResult = true;
		} catch (Exception e) {
			e.printStackTrace();
			testResult = false;
		}
		return testResult;
	}

}