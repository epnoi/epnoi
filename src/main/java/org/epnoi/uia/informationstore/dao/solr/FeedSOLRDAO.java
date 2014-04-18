package org.epnoi.uia.informationstore.dao.solr;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.epnoi.uia.parameterization.SOLRInformationStoreParameters;
import org.epnoi.uia.rest.services.JsonUtils;

import epnoi.model.Context;
import epnoi.model.Feed;
import epnoi.model.Item;
import epnoi.model.Resource;

public class FeedSOLRDAO extends SOLRDAO {

	// ---------------------------------------------------------------------------------------------------

	public void create(Resource resource) {
		Feed feed = (Feed) resource;
		System.out
				.println("]------------------------------------------------------------");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

		for (Item item : feed.getItems()) {
			SolrInputDocument document = _indexItem(item, null);

			try {
				this.server.add(document);
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			this.server.commit();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		Feed feed = (Feed) resource;
		System.out
				.println("]------------------------------------------------------------");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		for (Item item : feed.getItems()) {
			SolrInputDocument document = _indexItem(item, context);

			try {
				this.server.add(document);
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			this.server.commit();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// --------------------------------------------------------------------------------

	private SolrInputDocument _indexItem(Item item, Context context) {

		SolrInputDocument newDocument = new SolrInputDocument();

		newDocument.setField(SOLRDAOHelper.URI_PROPERTY, item.getURI());
		newDocument.setField(SOLRDAOHelper.ID_PROPERTY, item.getURI());
		
		/*1995-12-31T23:59:59Z
		 * 
		 * 
		 * METER LA FECHA!!!! SimpleDateFormat dateFormat = new
		 * SimpleDateFormat( "yyyy-MM-dd"); Date date =
		 * dateFormat.parse(item.getPubDate());
		 * 
		 * newDocument.addField(SOLRDAOHelper.DESCRIPTION_PROPERTY,
		 * item.getDescription());
		 */

		newDocument.addField(SOLRDAOHelper.DESCRIPTION_PROPERTY,
				item.getDescription());
		System.out
				.println("]------------------------------------------------------------");
		if (context != null) {
			List<String> keywords = (List<String>) context.getElements().get(
					item.getURI());
			newDocument.addField(SOLRDAOHelper.CONTENT_PROPERTY,
					_concatKeywords(keywords));
			System.out.println("]" + item.getURI() + " _scanKeywords:> "
					+ _concatKeywords(keywords));
		}
		
		
		newDocument.addField(SOLRDAOHelper.DATE_PROPERTY, convertDateFormat(item.getPubDate()));
		

		return newDocument;

	}

	private String _concatKeywords(List<String> keywords) {
		String listString = "";

		for (String s : keywords) {
			listString += s + "\t";
		}
		return listString;
	}

	// ---------------------------------------------------------------------------------------------------

	public void update(Resource resource) {
		Feed feed = (Feed) resource;
		// TODO to be done
	}

	// ---------------------------------------------------------------------------------------------------

	public Feed read(String URI) {

		Feed feed = new Feed();
		feed.setURI(URI);

		return feed;
	}

	// ---------------------------------------------------------------------------------------------------

	public Boolean exists(String URI) {
		boolean exists = false;
		/*
		 * Node foo1 = NodeFactory.createURI(URI);
		 * 
		 * return graph.find(new Triple(foo1, Node.ANY, Node.ANY)).hasNext();
		 */
		return exists;
	}

	// ---------------------------------------------------------------------------------------------------

	public void show() {
		this.query("uri:*");
		// this.query(ClientUtils.escapeQueryChars("http://uriA0"));
		// this.query("uri%3Ahttp//uriA2");
	}

	// ---------------------------------------------------------------------------------------------------

	public List<String> query(String query) {
		List<String> uris = new ArrayList<String>();

		System.out.println("-.-.-.-.-.-.-----> " + query);

		try {
			QueryResponse queryResponse = super.makeQuery(query);
			
			List<FacetField> facetFields = queryResponse.getFacetFields();
			System.out.println(" sixe--> "+facetFields.size()); 
			for (int i = 0; i <facetFields.size(); i++) {
				
			    FacetField facetField = facetFields.get(i);
			    System.out.println("facet:>"+facetField.getName());
			    List<Count> facetInfo = facetField.getValues();
			    for (FacetField.Count facetInstance : facetInfo) {
			        System.out.println(facetInstance.getName() + " : " + facetInstance.getCount() + " [drilldown qry:" + facetInstance.getAsFilterQuery());
			    }
			}
			
			
			
			
			String json = JsonUtils.toJson(queryResponse);
			System.out.println("jsonresult "+json);
			SolrDocumentList docs = queryResponse.getResults();
			if (docs != null) {
				// System.out.println(docs.getNumFound() + " documents found, "
				// + docs.size() + " returned : ");
				for (int i = 0; i < docs.size(); i++) {
					SolrDocument document = docs.get(i);
				
					// System.out.println("\t" + document.toString());
					uris.add((String) document.get(SOLRDAOHelper.URI_PROPERTY));
				}
			}
			System.out.println(" ----> "+JsonUtils.toJson(queryResponse.getResponse()));
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return uris;
	}

	// ---------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		/*
		 * String feedURI = "http://feed"; Feed feed = new Feed(); Context
		 * context = new Context();
		 * 
		 * feed.setURI(feedURI); feed.setTitle("arXiv"); feed.setLink(
		 * "http://localhost:8983/solr/select?facet=true&facet.field=subject&facet.field=setSpec&facet.field=creator&facet.field=date"
		 * );
		 * 
		 * for (int i = 0; i < 1000; i++) { Item itemA = new Item();
		 * 
		 * itemA.setURI("http://uriA" + i); itemA.setTitle("titleA" + i);
		 * itemA.setLink("http://www.cadenaser.com");
		 * itemA.setDescription("Description for item" + i); List<String>
		 * kewords = Arrays.asList("mi" + i, "mama" + i, "me" + i, "mima" + i);
		 * context.getElements().put(itemA.getURI(), kewords);
		 * feed.addItem(itemA); }
		 * 
		 * Item itemB = new Item();
		 * 
		 * itemB.setURI("http://uriB"); itemB.setTitle("titleB");
		 * itemB.setLink("http://www.elpais.es");
		 * itemB.setDescription("bla bla bla gato blab lba lba"); List<String>
		 * kewords = Arrays.asList("mi", "mama", "me", "mima", "cosarara");
		 * context.getElements().put(itemB.getURI(), kewords);
		 * feed.addItem(itemB);
		 */
		FeedSOLRDAO feedRDFDAO = new FeedSOLRDAO();
		SOLRInformationStoreParameters parameters = new SOLRInformationStoreParameters();
		parameters.setPath("solr");
		parameters.setCore("proofsCore");
		parameters.setHost("localhost");
		parameters.setPort("8983");

		feedRDFDAO.init(parameters);
		/*
		 * if (SOLRDAO.test(parameters)) { System.out.println("Test OK!");
		 * 
		 * feedRDFDAO.create(feed, context); } else {
		 * System.out.println("Test failed!!!, SOLR is down :( "); }
		 * 
		 * feedRDFDAO.show();
		 */
		List<String> queryResults = feedRDFDAO.query("content:scalability");
		for (String result : queryResults) {
			System.out.println("-->" + result);
		}
		

		
		

	}
}
