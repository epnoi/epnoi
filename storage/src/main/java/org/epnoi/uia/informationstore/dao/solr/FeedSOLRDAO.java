package org.epnoi.uia.informationstore.dao.solr;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.epnoi.model.Context;
import org.epnoi.model.Feed;
import org.epnoi.model.Item;
import org.epnoi.model.Resource;
import org.epnoi.model.parameterization.SOLRInformationStoreParameters;
import org.epnoi.model.rdf.FeedRDFHelper;
import org.epnoi.model.search.SearchContext;
import org.epnoi.model.search.SelectExpression;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;



public class FeedSOLRDAO extends SOLRDAO {

	// ---------------------------------------------------------------------------------------------------

	public void create(Resource resource) {
		Feed feed = (Feed) resource;
	
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
/*
		for (Item item : feed.getItems()) {
			SolrInputDocument document = _indexItem(item, null);

			try {
				this.server.add(document);
			} catch (SolrServerException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		try {
			this.server.commit();
		} catch (SolrServerException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
*/
	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		Feed feed = (Feed) resource;
	
		/*
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		for (Item item : feed.getItems()) {
			SolrInputDocument document = _indexItem(item, context);

			try {
				this.server.add(document);
			} catch (SolrServerException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		try {
			this.server.commit();
		} catch (SolrServerException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
*/
	}

	// --------------------------------------------------------------------------------

	private SolrInputDocument _indexItem(Item item, Context context) {

		SolrInputDocument newDocument = new SolrInputDocument();

		newDocument.setField(SOLRDAOHelper.URI_PROPERTY, item.getUri());
		newDocument.setField(SOLRDAOHelper.ID_PROPERTY, item.getUri());

		/*
		 * 1995-12-31T23:59:59Z
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
		
		if (context != null) {
			String content= (String) context.getElements().get(
					item.getUri());
			newDocument.addField(SOLRDAOHelper.CONTENT_PROPERTY,
					content);
			/*
			System.out.println("]" + item.getURI() + " _scanKeywords:> "
					+ _concatKeywords(keywords));
					*/
		}

		newDocument.addField(SOLRDAOHelper.DATE_PROPERTY,
				convertDateFormat(item.getPubDate()));
		newDocument.addField(SOLRDAOHelper.INFORMATION_SOURCE_NAME_PROPERTY, context
				.getParameters().get(Context.INFORMATION_SOURCE_NAME));
		
		
		newDocument.addField(SOLRDAOHelper.TYPE_PROPERTY, FeedRDFHelper.ITEM_CLASS);
		
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
		feed.setUri(URI);

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
	/*
	 * public void show() { this.query("uri:*"); }
	 * 
	 * / //
	 * ----------------------------------------------------------------------
	 * ----------------------------- /* public List<String> query(String query)
	 * { List<String> uris = new ArrayList<String>();
	 * 
	 * System.out.println("-.-.-.-.-.-.-----> " + query);
	 * 
	 * try { QueryResponse queryResponse = super.makeQuery(query);
	 * 
	 * List<FacetField> facetFields = queryResponse.getFacetFields();
	 * System.out.println(" sixe--> "+facetFields.size()); for (int i = 0; i
	 * <facetFields.size(); i++) {
	 * 
	 * FacetField facetField = facetFields.get(i);
	 * System.out.println("facet:>"+facetField.getName()); List<Count> facetInfo
	 * = facetField.getValues(); for (FacetField.Count facetInstance :
	 * facetInfo) { System.out.println(facetInstance.getName() + " : " +
	 * facetInstance.getCount() + " [drilldown qry:" +
	 * facetInstance.getAsFilterQuery()); } }
	 * 
	 * 
	 * 
	 * 
	 * String json = JsonUtils.toJson(queryResponse);
	 * System.out.println("jsonresult "+json); SolrDocumentList docs =
	 * queryResponse.getResults(); if (docs != null) {
	 * 
	 * for (int i = 0; i < docs.size(); i++) { SolrDocument document =
	 * docs.get(i);
	 * 
	 * 
	 * uris.add((String) document.get(SOLRDAOHelper.URI_PROPERTY)); } }
	 * System.out
	 * .println(" ----> "+JsonUtils.toJson(queryResponse.getResponse()));
	 * 
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } return uris; }
	 */
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
		SOLRDAOQueryResolver queryResolver = new SOLRDAOQueryResolver();
		queryResolver.init(parameters);

		SelectExpression selectExpression = new SelectExpression();
		selectExpression.setSolrExpression("content:scalability");

		SearchContext searchContext = new SearchContext();
		searchContext.getFacets().add("date");

		System.out.println("------> QR "
				+ queryResolver.query(selectExpression, searchContext));

		/*
		 * if (SOLRDAO.test(parameters)) { System.out.println("Test OK!");
		 * 
		 * feedRDFDAO.create(feed, context); } else {
		 * System.out.println("Test failed!!!, SOLR is down :( "); }
		 * 
		 * feedRDFDAO.show();
		 */
		/*
		 * ESTO ES LO QUE ESTABAS USANDO List<String> queryResults =
		 * feedRDFDAO.query("content:scalability"); for (String result :
		 * queryResults) { System.out.println("-->" + result); }
		 */

	}

	// ---------------------------------------------------------------------------------------------------
	
	@Override
	public void remove(String URI) {
		/*
		try {
			this.server.deleteById(URI);
			this.server.commit();
		} catch (SolrServerException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
*/
	}
}
