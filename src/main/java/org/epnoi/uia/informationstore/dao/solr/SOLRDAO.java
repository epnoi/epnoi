package org.epnoi.uia.informationstore.dao.solr;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.uia.parameterization.InformationStoreParameters;
import org.epnoi.uia.parameterization.SOLRInformationStoreParameters;

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

		this.parameters = (SOLRInformationStoreParameters) parameters;
		if ((this.parameters.getPort() != null)
				&& (this.parameters.getHost() != null)) {

			this.solrURL = "http://" + this.parameters.getHost() + ":"
					+ this.parameters.getPort() + "/"
					+ this.parameters.getPath() + "/"
					+ this.parameters.getCore();
			logger.info("Initializing in the URL " + this.solrURL+ "with the following paramters: "+this.parameters);
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

	protected String convertDateFormat(String dateExpression) {
		DateFormat formatter = new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss zzzz", Locale.ENGLISH);
		Date date = null;
		try {
			date = formatter.parse(dateExpression);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// "2005-02-28T00:00:00Z"^^xsd:dateTime
		// "2013-12-16T23:44:00+0100"^^xsd:dateTime
		/*
		 * SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
		 * "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		 */
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
				Locale.ENGLISH);
		return (dt1.format(date));

	}

	
}
