package org.epnoi.uia.informationstore.dao.rdf;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;

import epnoi.model.Context;
import epnoi.model.Feed;
import epnoi.model.Item;
import epnoi.model.Resource;

public class FeedRDFDAO extends RDFDAO {

	// ---------------------------------------------------------------------------------------------------

	public void create(Resource resource) {
		Feed feed = (Feed) resource;

		// System.out.println("--------------------------------------------------------->"+feed);
		String feedURI = feed.getURI();
		/*
		 * String queryExpression = "INSERT INTO GRAPH <" +
		 * this.parameters.getGraph() + "> { <" + feedURI + "> a <" +
		 * FeedRDFHelper.FEED_CLASS + "> ; " + "<" + RDFHelper.URL_PROPERTY +
		 * ">" + " \"" + feed.getLink() + "\"  ; " + "<" +
		 * RDFHelper.TITLE_PROPERTY + ">" + " \"" + feed.getTitle() + "\" " +
		 * " . }";
		 */
		String queryExpression2 = "INSERT INTO GRAPH <{GRAPH}>"
				+ "{ <{URI}> a <{FEED_CLASS}> ; "
				+ "<{URL_PROPERTY}> \"{FEED_LINK}\" ; "
				+ "<{PUB_DATE_PROPERTY}> \"{FEED_PUB_DATE}\" ; "
				+ "<{DESCRIPTION_PROPERTY}> \"{FEED_DESCRIPTION}\" ; "
				+ "<{TITLE_PROPERTY}>  \"{FEED_TITLE}\" . }";

		queryExpression2 = queryExpression2
				.replace("{GRAPH}", this.parameters.getGraph())
				.replace("{URI}", feedURI)
				.replace("{FEED_CLASS}", FeedRDFHelper.FEED_CLASS)
				.replace("{URL_PROPERTY}", RDFHelper.URL_PROPERTY)
				.replace("{FEED_LINK}", feed.getLink())
				.replace("{TITLE_PROPERTY}", RDFHelper.TITLE_PROPERTY)
				.replace("{FEED_TITLE}", cleanOddCharacters(feed.getTitle()))
				.replace("{PUB_DATE_PROPERTY}", FeedRDFHelper.PUB_DATE_PROPERTY)
				.replace("{DESCRIPTION_PROPERTY}",
						FeedRDFHelper.DESCRIPTION_PROPERTY)
				.replace("{FEED_DESCRIPTION}", cleanOddCharacters(feed.getDescription()))
				.replace("{FEED_PUB_DATE}", feed.getPubDate());

		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(
				queryExpression2, this.graph);
		vur.exec();
		ItemRDFDAO itemRDFDAO = new ItemRDFDAO();
		itemRDFDAO.init(this.parameters);
		Node uriNode = Node.createURI(feedURI);
		Node aggregatesProperty = Node
				.createURI(RDFOAIOREHelper.AGGREGATES_PROPERTY);
		for (Item item : feed.getItems()) {
			// System.out.println(item.getURI());
			if (item.getURI() != null) {
				Node objectItem = Node.createURI(item.getURI());

				this.graph.add(new Triple(uriNode, aggregatesProperty,
						objectItem));

				itemRDFDAO.create(item);
			}
		}
	}

	// ---------------------------------------------------------------------------------------------------

	public void remove(String URI) {
		ItemRDFDAO itemRDFDAO = new ItemRDFDAO();
		itemRDFDAO.init(this.parameters);

		String feedURI = URI;

		Query sparql = QueryFactory.create("DESCRIBE <" + feedURI + "> FROM <"
				+ this.parameters.getGraph() + ">");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, this.graph);

		Model model = vqe.execDescribe();
		Graph g = model.getGraph();
		// System.out.println("\nDESCRIBE results:");
		for (Iterator i = g.find(Node.ANY, Node.ANY, Node.ANY); i.hasNext();) {
			Triple triple = (Triple) i.next();

			this.graph.remove(triple);
			if (RDFOAIOREHelper.AGGREGATES_PROPERTY.equals(triple
					.getPredicate().getURI())) {
				itemRDFDAO.remove(triple.getObject().getURI());
			}

		}
	}

	// ---------------------------------------------------------------------------------------------------

	public Feed read(String URI) {

		ItemRDFDAO itemRDFDAO = new ItemRDFDAO();
		itemRDFDAO.init(this.parameters);
		Query sparql = QueryFactory.create("DESCRIBE <" + URI + "> FROM <"
				+ this.parameters.getGraph() + ">");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, this.graph);

		Model model = vqe.execDescribe();
		Graph g = model.getGraph();
		// System.out.println("\nDESCRIBE results:");
		if (!g.find(Node.ANY, Node.ANY, Node.ANY).hasNext()) {
			return null;
		}
		Feed feed = new Feed();
		feed.setURI(URI);
		for (Iterator i = g.find(Node.ANY, Node.ANY, Node.ANY); i.hasNext();) {
			Triple t = (Triple) i.next();
			// System.out.println(" { " + t.getSubject() + " SSS "
			// + t.getPredicate().getURI() + " " + t.getObject() + " . }");
			String predicateURI = t.getPredicate().getURI();

			if (RDFHelper.TITLE_PROPERTY.equals(predicateURI)) {
				feed.setTitle(t.getObject().getLiteral().getValue().toString());
			} else if (RDFHelper.URL_PROPERTY.equals(predicateURI)) {
				feed.setLink(t.getObject().getLiteral().getValue().toString());
			} else if (RDFHelper.COMMENT_PROPERTY.equals(predicateURI)) {
				feed.setDescription(t.getObject().getURI().toString());
			} else if (FeedRDFHelper.COPYRIGHT_PROPERTY.equals(predicateURI)) {
				feed.setCopyright(t.getObject().getURI().toString());
			} else if (FeedRDFHelper.LANGUAGE_PROPERTY.equals(predicateURI)) {
				feed.setLanguage(t.getObject().getURI().toString());
			} else if (RDFOAIOREHelper.AGGREGATES_PROPERTY.equals(predicateURI)) {
				// System.out.println("predicateURI " + predicateURI);
				String itemURI = t.getObject().toString();

				// System.out.println("itemURI " + itemURI);
				Item item = itemRDFDAO.read(itemURI);
				// System.out.println(".>>>" + item);
				if (item != null) {
					feed.addItem(item);
					// System.out.println("items> " + feed.getItems());
				}

				// feed.setLink(t.getObject().getLiteral().getValue().toString());
			}

		}
		return feed;
	}

	// ---------------------------------------------------------------------------------------------------

	public Boolean exists(String URI) {

		Node foo1 = NodeFactory.createURI(URI);

		return graph.find(new Triple(foo1, Node.ANY, Node.ANY)).hasNext();

	}

	// ---------------------------------------------------------------------------------------------------

	public void showTriplets() {
		System.out
				.println("SHOWING TRIPLETS-----------------------------------------------------------------------------------------------------");

		Query sparql = QueryFactory.create("SELECT * FROM <"
				+ this.parameters.getGraph() + ">  WHERE { { ?s ?p ?o } }");

		VirtuosoQueryExecution virtuosoQueryEngine = VirtuosoQueryExecutionFactory
				.create(sparql, this.graph);

		ResultSet results = virtuosoQueryEngine.execSelect();
		while (results.hasNext()) {
			QuerySolution result = results.nextSolution();
			RDFNode s = result.get("s");
			RDFNode p = result.get("p");
			RDFNode o = result.get("o");
			System.out.println(" { " + s + " | " + p + " | " + o + " }");
		}
		System.out
				.println("-----------------------------------------------------------------------------------------------------");
	}

	// ---------------------------------------------------------------------------------------------------

	private static List<Feed> _generateData() {
		List<Feed> feeds = new ArrayList<Feed>();
		String feedURI = "http://feedA";
		Feed feedA = new Feed();
		Context context = new Context();

		feedA.setURI(feedURI);
		feedA.setTitle("arXiv");
		feedA.setLink("http://localhost:8983/solr/select?facet=true&facet.field=subject&facet.field=setSpec&facet.field=creator&facet.field=date");
		feedA.setPubDate("2013-10-10");
		feedA.setDescription("This is the description of feed A");
		for (int i = 0; i < 10; i++) {
			Item itemA = new Item();

			itemA.setURI("http://uriA" + i);
			itemA.setTitle("titleA" + i);
			itemA.setLink("http://www.cadenaser.com");
			itemA.setDescription("Description \" for item" + i);
			itemA.setPubDate("Mon, 16 Dec 2013 22:22:16 GMT");

			List<String> kewords = Arrays.asList("mi" + i, "mama" + i,
					"me" + i, "mima" + i);
			context.getElements().put(itemA.getURI(), kewords);
			feedA.addItem(itemA);
		}

		Item itemB = new Item();

		itemB.setURI("http://uriB");
		itemB.setTitle("titleB");
		itemB.setLink("http://www.elpais.es");
		itemB.setDescription("bla bla bla gato blab lba lba");
		itemB.setPubDate("Mon, 16 Dec 2013 23:22:16 GMT");
		List<String> kewords = Arrays.asList("mi", "mama", "me", "mima",
				"cosarara");

		// ----------------------------------------------------------------
		String feedURIB = "http://feedB";
		Feed feedB = new Feed();
		Context contextB = new Context();

		feedB.setURI(feedURIB);
		feedB.setTitle("arXiv");
		feedB.setLink("http://localhost:8983/solr/select?facet=true&facet.field=subject&facet.field=setSpec&facet.field=creator&facet.field=date");
		feedB.setPubDate("Fri, 13 Dec 2013 16:57:49 +0000");
		feedB.setDescription("This is the description of feed B");
		for (int i = 0; i < 10; i++) {
			Item itemA = new Item();

			itemA.setURI("http://uriB" + i);
			itemA.setTitle("titleB" + i);
			itemA.setLink("http://www.whatever.com");
			itemA.setDescription("Description for item" + i);
			itemA.setPubDate("Fri, 13 Dec 2013 16:57:49 +0000");
			List<String> kewordsA = Arrays.asList("mi" + i, "mama" + i, "me"
					+ i, "mima" + i);
			context.getElements().put(itemA.getURI(), kewordsA);
			feedB.addItem(itemA);
		}
		contextB.getElements().put(itemB.getURI(), kewords);

		feedA.addItem(itemB);
		feeds.add(feedA);
		feeds.add(feedB);
		return feeds;
	}

	
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		String virtuosoURL = "jdbc:virtuoso://localhost:1111";
		List<Feed> feeds = _generateData();
		String feedURI = "http://feedB";

		FeedRDFDAO feedRDFDAO = new FeedRDFDAO();
		VirtuosoInformationStoreParameters parameters = new VirtuosoInformationStoreParameters();
		parameters.setGraph("http://feedTest");
		parameters.setHost("localhost");
		parameters.setPort("1111");
		parameters.setUser("dba");
		parameters.setPassword("dba");

		feedRDFDAO.init(parameters);

		if (!feedRDFDAO.exists(feedURI)) {
			System.out.println("The information source doesn't exist");

			for (Feed feed : feeds) {
				System.out.println("---> " + feed);
				feedRDFDAO.create(feed);
			}
		} else {
			System.out.println("The information source already exists!");
		}

		feedRDFDAO.showTriplets();

		System.out.println("Deleting the feed " + feedURI);
		feedRDFDAO.remove(feedURI);
		feedRDFDAO.showTriplets();
		// System.out.println("Lests add it again ");
		// feedRDFDAO.create(feed);

		Feed readedFeed = feedRDFDAO.read(feedURI);

		System.out.println("Readed feed " + readedFeed);
		if (readedFeed != null) {
			for (Item item : readedFeed.getItems()) {
				System.out.println("              ---------->" + item);
			}
		}
		if (feedRDFDAO.exists(feedURI)) {
			System.out.println("The information source now exists :) ");
		}
		VirtGraph graph = new VirtGraph(parameters.getGraph(), virtuosoURL,
				"dba", "dba");
		graph.clear();
	}
}
