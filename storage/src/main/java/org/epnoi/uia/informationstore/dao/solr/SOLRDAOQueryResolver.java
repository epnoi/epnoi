package org.epnoi.uia.informationstore.dao.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.epnoi.model.Selector;
import org.epnoi.model.parameterization.InformationStoreParameters;
import org.epnoi.model.parameterization.SOLRInformationStoreParameters;
import org.epnoi.model.search.*;
import org.epnoi.uia.informationstore.SelectorHelper;

import java.util.List;
import java.util.logging.Logger;

public class SOLRDAOQueryResolver {
	private static final Logger logger = Logger
			.getLogger(SOLRDAOQueryResolver.class.getName());
	private String solrURL = "http://localhost:8983";

	protected SOLRInformationStoreParameters parameters;
//	protected HttpSolrServer server;

	// ------------------------------------------------------------------------------

	public void init(InformationStoreParameters parameters) {

		this.parameters = (SOLRInformationStoreParameters) parameters;

		if ((this.parameters.getPort() != null)
				&& (this.parameters.getHost() != null)) {

			this.solrURL = "http://" + this.parameters.getHost() + ":"
					+ this.parameters.getPort() + "/"
					+ this.parameters.getPath() + "/"
					+ this.parameters.getCore();
			logger.info("Initializing in the URL " + this.solrURL);
		//	this.server = new HttpSolrServer(this.solrURL);

		}
	}

	// ------------------------------------------------------------------------------

	public SearchSelectResult query(SelectExpression searchSelectExpression,
			SearchContext searchContext) {
		SearchSelectResult searchSelectResult = new SearchSelectResult();
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery(searchSelectExpression.getSolrExpression());
		solrQuery.setParam("password", "password");
		solrQuery.setParam("username", "admin");
		solrQuery.addField(SOLRDAOHelper.SCORE_PROPERTY);
		solrQuery.addField(SOLRDAOHelper.URI_PROPERTY);
		solrQuery.addField(SOLRDAOHelper.TYPE_PROPERTY);

		for (String facet : searchContext.getFacets()) {
			solrQuery.setFacet(true).addFacetField(facet).setFacetMinCount(1)
					.setFacetLimit(15);
		}
		for (String filterQuery : searchContext.getFilterQueries()) {

			solrQuery.addFilterQuery(filterQuery);

		}
/*
		try {
			QueryResponse queryResponse = this.server.query(solrQuery);

			for (SolrDocument document : queryResponse.getResults()) {

				SelectionResultTuple selectionResultTuple = new SelectionResultTuple();
				selectionResultTuple.setResourceURI((String) document
						.getFieldValue(SOLRDAOHelper.URI_PROPERTY));
				selectionResultTuple.setScore((float) document
						.getFieldValue(SOLRDAOHelper.SCORE_PROPERTY));

				selectionResultTuple.setType((String) document
						.getFieldValue(SOLRDAOHelper.TYPE_PROPERTY));

				searchSelectResult.getResources().add(selectionResultTuple);
			}



			List<FacetField> facetFields = queryResponse.getFacetFields();
			if (facetFields != null) {

				for (int i = 0; i < facetFields.size(); i++) {
					Facet selectionFacet = new Facet();
					FacetField facetField = facetFields.get(i);
					// System.out.println("facet:>" + facetField.getName());

					selectionFacet.setName(facetField.getName());

					List<Count> facetInfo = facetField.getValues();
					for (FacetField.Count facetInstance : facetInfo) {
						FacetValue selectionFacetValue = new FacetValue();
						selectionFacetValue.setValue(facetInstance.getName());
						selectionFacetValue.setCount(facetInstance.getCount());
						selectionFacet.getValues().add(selectionFacetValue);

					}
					searchSelectResult.getFacets().add(selectionFacet);
				}

			}

		} catch (SolrServerException e) {

			e.printStackTrace();
		}
	*/
			return searchSelectResult;
	}

	// ------------------------------------------------------------------------------

	public boolean exists(Selector selector) {
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery("uri:\"" + selector.getProperty(SelectorHelper.URI)+"\"");
		solrQuery.setParam("password", "password");
		solrQuery.setParam("username", "admin");
		solrQuery.addField(SOLRDAOHelper.SCORE_PROPERTY);
		solrQuery.addField(SOLRDAOHelper.URI_PROPERTY);
		solrQuery.addField(SOLRDAOHelper.TYPE_PROPERTY);

		try {
		/*	QueryResponse queryResponse = this.server.query(solrQuery);
			
			return queryResponse.getResults().size() > 0;
*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	// ------------------------------------------------------------------------------

	public static void main(String[] args) {
		System.out.println("SOLRDAOQueryResolver main");
	}
}
