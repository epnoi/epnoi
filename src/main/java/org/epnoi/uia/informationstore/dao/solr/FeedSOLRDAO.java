package org.epnoi.uia.informationstore.dao.solr;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.epnoi.uia.parameterization.SOLRInformationStoreParameters;

import epnoi.model.Feed;
import epnoi.model.Item;
import epnoi.model.Resource;

public class FeedSOLRDAO extends SOLRDAO {

	// ---------------------------------------------------------------------------------------------------

	public void create(Resource resource) {
		Feed feed = (Feed) resource;

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		for (Item item : feed.getItems()) {
			SolrInputDocument document = _indexItem(item);

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

	private SolrInputDocument _indexItem(Item item) {

		SolrInputDocument newDocument = new SolrInputDocument();

		newDocument.setField(SOLRDAOHelper.URI_PROPERTY, item.getURI());
		newDocument.setField(SOLRDAOHelper.ID_PROPERTY, item.getURI());

		/*
		 * SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd");
		 * Date date = dateFormat.parse(item.getPubDate());
		 * 
		 * newDocument.addField(SOLRDAOHelper.DESCRIPTION_PROPERTY,
		 * item.getDescription());
		 */

		newDocument
				.addField(
						"description",
						"rewrwe ewprowier  werpweoriwe rewpoirwe rpoweirpewor ewrweur ifdusfisdfoisdu w oriwue rowieu");

		return newDocument;

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
		
		this.query("uri:"+ClientUtils.escapeQueryChars("http://uriA0"));
		//this.query("uri%3Ahttp//uriA2");
	}

	// ---------------------------------------------------------------------------------------------------

	public List<String> query(String query) {
		List<String> uris = new ArrayList<String>();
		
		System.out.println("-.-.-.-.-.-.-----> "+ClientUtils.escapeQueryChars(query));
		
		try {
			QueryResponse queryResponse = super.makeQuery(query);
			SolrDocumentList docs = queryResponse.getResults();
			if (docs != null) {
				System.out.println(docs.getNumFound() + " documents found, "
						+ docs.size() + " returned : ");
				for (int i = 0; i < docs.size(); i++) {
					SolrDocument doc = docs.get(i);
					System.out.println("\t" + doc.toString());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return uris;
	}

	// ---------------------------------------------------------------------------------------------------

	public static void main(String[] args) {

		String feedURI = "http://feed";
		Feed feed = new Feed();

		feed.setURI(feedURI);
		feed.setTitle("arXiv");
		feed.setLink("http://localhost:8983/solr/select?facet=true&facet.field=subject&facet.field=setSpec&facet.field=creator&facet.field=date");

		for (int i = 0; i < 1000; i++) {
			Item itemA = new Item();

			itemA.setURI("http://uriA" + i);
			itemA.setTitle("titleA" + i);
			itemA.setLink("http://www.cadenaser.com");
			feed.addItem(itemA);
		}
		Item itemB = new Item();

		itemB.setURI("http://uriB");
		itemB.setTitle("titleB");
		itemB.setLink("http://www.elpais.es");

		feed.addItem(itemB);

		FeedSOLRDAO feedRDFDAO = new FeedSOLRDAO();
		SOLRInformationStoreParameters parameters = new SOLRInformationStoreParameters();
		parameters.setPath("solr");
		parameters.setCore("proofsCore");
		parameters.setHost("localhost");
		parameters.setPort("8983");

		feedRDFDAO.init(parameters);

		if (SOLRDAO.test(parameters)) {
			System.out.println("Test OK!");

			feedRDFDAO.create(feed);
		} else {
			System.out.println("Test failed!!!");
		}

		feedRDFDAO.show();

	}
}
