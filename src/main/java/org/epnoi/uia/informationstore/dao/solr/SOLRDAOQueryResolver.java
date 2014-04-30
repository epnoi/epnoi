package org.epnoi.uia.informationstore.dao.solr;

import java.util.List;
import java.util.logging.Logger;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.tools.ant.taskdefs.Sleep;
import org.epnoi.uia.parameterization.InformationStoreParameters;
import org.epnoi.uia.parameterization.SOLRInformationStoreParameters;
import org.epnoi.uia.search.SearchContext;
import org.epnoi.uia.search.select.SearchSelectResult;
import org.epnoi.uia.search.select.SelectExpression;
import org.epnoi.uia.search.select.Facet;
import org.epnoi.uia.search.select.FacetValue;
import org.epnoi.uia.search.select.SelectionResultPair;

public class SOLRDAOQueryResolver {
	private static final Logger logger = Logger
			.getLogger(SOLRDAOQueryResolver.class.getName());
	private String solrURL = "http://localhost:8983";

	protected SOLRInformationStoreParameters parameters;
	protected HttpSolrServer server;

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
			this.server = new HttpSolrServer(this.solrURL);

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
		solrQuery.addField("uri");
		solrQuery.addField("score");

		for (String facet : searchContext.getFacets()) {
			solrQuery.setFacet(true).addFacetField(facet).setFacetMinCount(1)
					.setFacetLimit(15);
		}
		for (String filterQuery : searchContext.getFilterQueries()) {

			solrQuery.addFilterQuery(filterQuery);

		}

		try {
			QueryResponse queryResponse = this.server.query(solrQuery);

			for (SolrDocument document : queryResponse.getResults()) {
				System.out.println(">>" + document.toString());

				SelectionResultPair selectionResultPair = new SelectionResultPair();
				selectionResultPair.setResourceURI((String) document
						.getFieldValue("uri"));
				selectionResultPair.setScore((float) document
						.getFieldValue("score"));

				searchSelectResult.getResources().add(selectionResultPair);
			}

//			System.out.println("-----------> " + queryResponse.getExplainMap());

			List<FacetField> facetFields = queryResponse.getFacetFields();
			if (facetFields != null) {

				for (int i = 0; i < facetFields.size(); i++) {
					Facet selectionFacet = new Facet();
					FacetField facetField = facetFields.get(i);
//					System.out.println("facet:>" + facetField.getName());

					selectionFacet.setName(facetField.getName());

					List<Count> facetInfo = facetField.getValues();
					for (FacetField.Count facetInstance : facetInfo) {
						FacetValue selectionFacetValue = new FacetValue();
						selectionFacetValue.setValue(facetInstance.getName());
						selectionFacetValue.setCount(facetInstance.getCount());
						selectionFacet.getValues().add(selectionFacetValue);
/*
						System.out.println(facetInstance.getName() + " : "
								+ facetInstance.getCount() + " [drilldown qry:"
								+ facetInstance.getAsFilterQuery());
								*/
					}
					searchSelectResult.getFacets().add(selectionFacet);
				}

			}

		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("The result ---------> " + searchSelectResult);
		return searchSelectResult;
	}

	// ------------------------------------------------------------------------------

	public static void main(String[] args) {
		System.out.println("SOLRDAOQueryResolver main");
	}
}
