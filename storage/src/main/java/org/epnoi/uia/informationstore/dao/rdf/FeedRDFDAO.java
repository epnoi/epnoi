package org.epnoi.uia.informationstore.dao.rdf;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import org.epnoi.model.Context;
import org.epnoi.model.Feed;
import org.epnoi.model.Item;
import org.epnoi.model.Resource;
import org.epnoi.model.commons.DateConverter;
import org.epnoi.model.commons.StringUtils;
import org.epnoi.model.parameterization.VirtuosoInformationStoreParameters;
import org.epnoi.model.rdf.DublinCoreRDFHelper;
import org.epnoi.model.rdf.FeedRDFHelper;
import org.epnoi.model.rdf.OAIORERDFHelper;
import org.epnoi.model.rdf.RDFHelper;
import virtuoso.jena.driver.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FeedRDFDAO extends RDFDAO {

	// ---------------------------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		Feed feed = (Feed) resource;

		// System.out.println("--------------------------------------------------------->"+feed);
		String feedURI = feed.getUri();

		String queryExpression = "PREFIX  xsd:  <http://www.w3.org/2001/XMLSchema#> INSERT INTO GRAPH <{GRAPH}>"
				+ "{ <{URI}> a <{FEED_CLASS}> ; "
				+ "<{URL_PROPERTY}> \"{FEED_LINK}\" ; "
				+ "<{PUB_DATE_PROPERTY}> \"{FEED_PUB_DATE}\"^^xsd:dateTime ; "
				+ "<{DESCRIPTION_PROPERTY}> \"{FEED_DESCRIPTION}\" ; "

				+ "<{TITLE_PROPERTY}>  \"{FEED_TITLE}\" . }";

		System.out.println("pubDate ----------------------->"
				+ feed.getPubDate());

		queryExpression = queryExpression
				.replace("{GRAPH}", this.parameters.getGraph())
				.replace("{URI}", feedURI)
				.replace("{FEED_CLASS}", FeedRDFHelper.FEED_CLASS)
				.replace("{URL_PROPERTY}", RDFHelper.URL_PROPERTY)
				.replace("{FEED_LINK}", feed.getLink())
				.replace("{TITLE_PROPERTY}", DublinCoreRDFHelper.TITLE_PROPERTY)
				.replace("{FEED_TITLE}", StringUtils.cleanOddCharacters(feed.getTitle()))
				.replace("{PUB_DATE_PROPERTY}", FeedRDFHelper.PUB_DATE_PROPERTY)

				.replace("{DESCRIPTION_PROPERTY}",
						FeedRDFHelper.DESCRIPTION_PROPERTY)
				.replace("{FEED_DESCRIPTION}",
						StringUtils.cleanOddCharacters(feed.getDescription()))
				.replace("{FEED_PUB_DATE}",
						DateConverter.convertDateFormat(feed.getPubDate()));
	
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(
				queryExpression, this.graph);
		vur.exec();
		ItemRDFDAO itemRDFDAO = new ItemRDFDAO();
		itemRDFDAO.init(this.parameters);
		Node uriNode = Node.createURI(feedURI);
		Node aggregatesProperty = Node
				.createURI(OAIORERDFHelper.AGGREGATES_PROPERTY);
		for (Item item : feed.getItems()) {
			// System.out.println(item.getURI());
			if (item.getUri() != null) {
				Node objectItem = Node.createURI(item.getUri());

				this.graph.add(new Triple(uriNode, aggregatesProperty,
						objectItem));

				itemRDFDAO.create(item, context);
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
			if (OAIORERDFHelper.AGGREGATES_PROPERTY.equals(triple
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
		feed.setUri(URI);
		for (Iterator i = g.find(Node.ANY, Node.ANY, Node.ANY); i.hasNext();) {
			Triple t = (Triple) i.next();
			// System.out.println(" { " + t.getSubject() + " SSS "
			// + t.getPredicate().getURI() + " " + t.getObject() + " . }");
			String predicateURI = t.getPredicate().getURI();

			if (DublinCoreRDFHelper.TITLE_PROPERTY.equals(predicateURI)) {
				feed.setTitle(t.getObject().getLiteral().getValue().toString());
			} else if (RDFHelper.URL_PROPERTY.equals(predicateURI)) {
				feed.setLink(t.getObject().getLiteral().getValue().toString());
			} else if (FeedRDFHelper.DESCRIPTION_PROPERTY.equals(predicateURI)) {
				feed.setDescription(t.getObject().toString());
			} else if (FeedRDFHelper.PUB_DATE_PROPERTY.equals(predicateURI)) {
				System.out.println("----------->" + t.getObject().toString());
				feed.setPubDate(t.getObject().getLiteral().getValue()
						.toString());
			} else if (FeedRDFHelper.COPYRIGHT_PROPERTY.equals(predicateURI)) {
				feed.setCopyright(t.getObject().getURI().toString());
			} else if (FeedRDFHelper.LANGUAGE_PROPERTY.equals(predicateURI)) {
				feed.setLanguage(t.getObject().getURI().toString());

			} else if (OAIORERDFHelper.AGGREGATES_PROPERTY.equals(predicateURI)) {
				// System.out.println("predicateURI " + predicateURI);
				String itemURI = t.getObject().toString();

				// System.out.println("itemURI " + itemURI);
				Item item = (Item) itemRDFDAO.read(itemURI);
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
/*
	String convertDateFormat(String dateExpression) {
		List<SimpleDateFormat> knownPatterns = new ArrayList<SimpleDateFormat>();
		knownPatterns.add(new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss zzzz", Locale.ENGLISH));

		knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH));
		knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
				Locale.ENGLISH));
		knownPatterns.add(new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",
				Locale.ENGLISH));

		for (SimpleDateFormat pattern : knownPatterns) {
			try {
				// Take a try
				Date parsedDate = pattern.parse(dateExpression);
				SimpleDateFormat dt1 = new SimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
				return (dt1.format(parsedDate));
			} catch (ParseException pe) {
				// Loop on
			}
		}
		System.err.println("No known Date format found: " + dateExpression);
		return null;

	}
*/
	// ---------------------------------------------------------------------------------------------------

	private static List<Feed> _generateData() {
		List<Feed> feeds = new ArrayList<Feed>();
		String feedURI = "http://feedA";
		Feed feedA = new Feed();
		Context context = new Context();

		feedA.setUri(feedURI);
		feedA.setTitle("arXiv");
		feedA.setLink("http://localhost:8983/solr/select?facet=true&facet.field=subject&facet.field=setSpec&facet.field=creator&facet.field=date");
		feedA.setPubDate("Mon, 12 Dec 2013 22:22:16 GMT");
		feedA.setDescription("This is the description of feed A");
		for (int i = 0; i < 10; i++) {
			Item itemA = new Item();

			itemA.setUri("http://uriA" + i);
			itemA.setTitle("titleA" + i);
			itemA.setLink("http://www.cadenaser.com");
			itemA.setDescription("Description \" for item" + i);
			itemA.setPubDate("Mon, 16 Dec 2013 22:22:16 GMT");

			List<String> kewords = Arrays.asList("mi" + i, "mama" + i,
					"me" + i, "mima" + i);
			context.getElements().put(itemA.getUri(), kewords);
			feedA.addItem(itemA);
		}

		Item itemB = new Item();

		itemB.setUri("http://uriB");
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

		feedB.setUri(feedURIB);
		feedB.setTitle("slashdot");
		feedB.setLink("http://localhost:8983/solr/select?facet=true&facet.field=subject&facet.field=setSpec&facet.field=creator&facet.field=date");
		feedB.setPubDate("Fri, 13 Dec 2013 16:57:49 +0000");
		feedB.setDescription("This is the description of feed B");
		for (int i = 0; i < 10; i++) {
			Item itemA = new Item();

			itemA.setUri("http://uriB" + i);
			itemA.setTitle("titleB" + i);
			itemA.setLink("http://www.whatever.com");
			itemA.setDescription("Description for item" + i);
			itemA.setPubDate("Fri, 13 Dec 2013 16:57:49 +0000");
			List<String> kewordsA = Arrays.asList("mi" + i, "mama" + i, "me"
					+ i, "mima" + i);
			context.getElements().put(itemA.getUri(), kewordsA);
			feedB.addItem(itemA);
		}
		contextB.getElements().put(itemB.getUri(), kewords);

		feedA.addItem(itemB);
		feeds.add(feedA);
		feeds.add(feedB);
		return feeds;
	}

	// ---------------------------------------------------------------------------------------------------------------------

	public void update(Resource resource) {

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

		Context context = new Context();
		context.getParameters().put(Context.INFORMATION_SOURCE_URI,
				"http://informationsourceURI");

		feedRDFDAO.init(parameters);

		if (!feedRDFDAO.exists(feedURI)) {
			System.out.println("The information source doesn't exist");

			for (Feed feed : feeds) {
				System.out.println("---> " + feed);
				feedRDFDAO.create(feed, context);
			}
		} else {
			System.out.println("The information source already exists!");
		}

		feedRDFDAO.showTriplets();
		/*
		 * System.out.println("Deleting the feed " + feedURI);
		 * feedRDFDAO.remove(feedURI); feedRDFDAO.showTriplets();
		 */
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
		// graph.clear();
	}
}
