package org.epnoi.uia.informationstore.dao.rdf;

import java.util.Iterator;

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

import epnoi.model.Feed;
import epnoi.model.Item;

public class FeedRDFDAO extends RDFDAO {

	// ---------------------------------------------------------------------------------------------------

	public void create(Feed feed) {

		String informationSourceURI = feed.getURI();

		String queryExpression = "INSERT INTO GRAPH <"
				+ this.parameters.getGraph() + "> { <" + informationSourceURI
				+ "> a <" + FeedRDFHelper.FEED_CLASS + "> ; " + "<"
				+ RDFHelper.URL_PROPERTY + ">" + " \"" + feed.getLink()
				+ "\"  ; " + "<" + RDFHelper.TITLE_PROPERTY + ">" + " \""
				+ feed.getTitle() + "\" " + " . }";
		System.out.println("---> " + queryExpression);

		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(
				queryExpression, this.graph);
		vur.exec();
		ItemRDFDAO itemRDFDAO = new ItemRDFDAO();
		itemRDFDAO.init(this.parameters);
		Node foo1 = Node.createURI(informationSourceURI);
		Node bar1 = Node.createURI(RDFOAIOREHelper.AGGREGATES_PROPERTY);
		for (Item item : feed.getItems()) {

			Node baz1 = Node.createURI(item.getURI());

			this.graph.add(new Triple(foo1, bar1, baz1));
			itemRDFDAO.create(item);
		}
	}

	// ---------------------------------------------------------------------------------------------------

	public void update(Feed feed) {
		// TODO to be done
	}

	// ---------------------------------------------------------------------------------------------------

	public Feed read(String URI) {
		Feed feed = new Feed();
		feed.setURI(URI);
		Query sparql = QueryFactory.create("DESCRIBE <" + URI + "> FROM <"
				+ this.parameters.getGraph() + ">");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, this.graph);

		Model model = vqe.execDescribe();
		Graph g = model.getGraph();
		System.out.println("\nDESCRIBE results:");
		for (Iterator i = g.find(Node.ANY, Node.ANY, Node.ANY); i.hasNext();) {
			Triple t = (Triple) i.next();
			System.out.println(" { " + t.getSubject() + " SSS "
					+ t.getPredicate().getURI() + " " + t.getObject() + " . }");
			String predicateURI = t.getPredicate().getURI();
			ItemRDFDAO itemRDFDAO = new ItemRDFDAO();
			itemRDFDAO.init(this.parameters);
			if (RDFHelper.TITLE_PROPERTY.equals(predicateURI)) {
				feed.setTitle(t.getObject().getLiteral().getValue().toString());
			} else if (RDFHelper.URL_PROPERTY.equals(predicateURI)) {
				feed.setLink(t.getObject().getLiteral().getValue().toString());
			} else if (RDFHelper.COMMENT_PROPERTY.equals(predicateURI)) {
				feed.setDescription(t.getObject().getURI().toString());
			}else if (FeedRDFHelper.COPYRIGHT_PROPERTY.equals(predicateURI)) {
				feed.setCopyright(t.getObject().getURI().toString());
			}
			else if (FeedRDFHelper.LANGUAGE_PROPERTY.equals(predicateURI)) {
				feed.setLanguage(t.getObject().getURI().toString());
			} else if (RDFOAIOREHelper.AGGREGATES_PROPERTY.equals(predicateURI)) {
				System.out.println("predicateURI " + predicateURI);
				String itemURI = t.getObject().toString();

				System.out.println("itemURI " + itemURI);
				Item item = itemRDFDAO.read(itemURI);
				System.out.println(".>>>" + item);
				if (item != null) {
					feed.addItem(item);
					System.out.println("items> " + feed.getItems());
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
				.println("//-----------------------------------------------------------------------------------------------------");
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
		itemA.setLink("http://urlA");

		Item itemB = new Item();

		itemB.setURI("http://uriB");
		itemB.setTitle("titleB");
		itemB.setLink("http://urlB");

		feed.addItem(itemA);
		feed.addItem(itemB);

		FeedRDFDAO feedRDFDAO = new FeedRDFDAO();
		VirtuosoInformationStoreParameters parameters = new VirtuosoInformationStoreParameters();
		parameters.setGraph("http://feedTest");
		parameters.setHost("localhost");
		parameters.setPort("1111");
		parameters.setUser("dba");
		parameters.setPassword("dba");

		feedRDFDAO.init(parameters);
		System.out.println(".,.,.,.,jjjjjjj");
		if (!feedRDFDAO.exists(feedURI)) {
			System.out.println("The information source doesn't exist");

			feedRDFDAO.create(feed);
		} else {
			System.out.println("The information source already exists!");
		}

		feedRDFDAO.showTriplets();
		VirtGraph graph = new VirtGraph(parameters.getGraph(), virtuosoURL,
				"dba", "dba");
		Feed readedInformationSource = feedRDFDAO.read(feedURI);

		System.out.println("Readed information source -> "
				+ readedInformationSource);

		for (Item item : readedInformationSource.getItems()) {
			System.out.println("              ---------->" + item);
		}

		if (feedRDFDAO.exists(feedURI)) {
			System.out.println("The information source now exists :) ");
		}

		graph.clear();
	}
}
