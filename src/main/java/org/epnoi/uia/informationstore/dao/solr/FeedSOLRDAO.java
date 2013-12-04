package org.epnoi.uia.informationstore.dao.solr;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.epnoi.uia.parameterization.SOLRInformationStoreParameters;

import virtuoso.jena.driver.VirtGraph;
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

		newDocument.setField(SOLRDAOHelper.URI_PROPERTY, item.getLink());

		/*
		 * SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd");
		 * Date date = dateFormat.parse(item.getPubDate());
		 * 
		 * newDocument.addField(SOLRDAOHelper.DESCRIPTION_PROPERTY,
		 * item.getDescription());
		 */

		newDocument.addField("type", "");

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
		boolean exists = true;
		/*
		 * Node foo1 = NodeFactory.createURI(URI);
		 * 
		 * return graph.find(new Triple(foo1, Node.ANY, Node.ANY)).hasNext();
		 */
		return exists;
	}

	// ---------------------------------------------------------------------------------------------------

	public void show() {

	}

	// ---------------------------------------------------------------------------------------------------

	public static void main(String[] args) {

		String virtuosoURL = "jdbc:virtuoso://localhost:1111";

		String feedURI = "http://feed";
		Feed feed = new Feed();

		feed.setURI(feedURI);
		feed.setTitle("arXiv");
		feed.setLink("http://localhost:8983/solr/select?facet=true&facet.field=subject&facet.field=setSpec&facet.field=creator&facet.field=date");

		Item itemA = new Item();

		itemA.setURI("http://uriA");
		itemA.setTitle("titleA");
		itemA.setLink("http://www.cadenaser.com");

		Item itemB = new Item();

		itemB.setURI("http://uriB");
		itemB.setTitle("titleB");
		itemB.setLink("http://www.elpais.es");

		feed.addItem(itemA);
		feed.addItem(itemB);

		FeedSOLRDAO feedRDFDAO = new FeedSOLRDAO();
		SOLRInformationStoreParameters parameters = new SOLRInformationStoreParameters();
		parameters.setCore("proofs");
		parameters.setHost("localhost");
		parameters.setPort("1111");

		feedRDFDAO.init(parameters);
		System.out.println(".,.,.,.,jjjjjjj");
		if (!feedRDFDAO.exists(feedURI)) {
			System.out.println("The information source doesn't exist");

			feedRDFDAO.create(feed);
		} else {
			System.out.println("The information source already exists!");
		}

		feedRDFDAO.show();

	}
}
