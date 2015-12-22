package org.epnoi.uia.informationstore.dao.solr;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.model.parameterization.InformationStoreParameters;
import org.epnoi.model.parameterization.SOLRInformationStoreParameters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

public abstract class SOLRDAO {
	private static final Logger logger = Logger.getLogger(SOLRDAO.class
			.getName());
	private String solrURL = "http://localhost:8983";

	protected SOLRInformationStoreParameters parameters;
	//protected static HttpSolrServer server;
	protected static boolean initialized = false;

	abstract public void create(Resource resource);

	abstract public void create(Resource resource, Context context);

	abstract public void remove(String URI);

	// --------------------------------------------------------------------------------

	public synchronized void init(InformationStoreParameters parameters) {
		if (!initialized) {
			this.parameters = (SOLRInformationStoreParameters) parameters;
			if ((this.parameters.getPort() != null)
					&& (this.parameters.getHost() != null)) {

				this.solrURL = "http://" + this.parameters.getHost() + ":"
						+ this.parameters.getPort() + "/"
						+ this.parameters.getPath() + "/"
						+ this.parameters.getCore();
				logger.info("Initializing in the URL " + this.solrURL
						+ "with the following paramters: " + this.parameters);
				//server = new HttpSolrServer(this.solrURL);
				initialized=true;
			}
		}
	}

	// --------------------------------------------------------------------------------

	public Boolean exists(String URI) {

		return true;
	}

	// --------------------------------------------------------------------------------

	public static boolean test(InformationStoreParameters parameters) {
		/*
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
		*/
		return true;
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
