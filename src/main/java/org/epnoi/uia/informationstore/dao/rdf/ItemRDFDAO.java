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
import epnoi.model.Resource;

public class ItemRDFDAO extends RDFDAO {

	// ---------------------------------------------------------------------------------------------------

	public void create(Resource resource) {
		Item item= (Item)resource;
		String informationSourceURI = item.getURI();

		String queryExpression = "INSERT INTO GRAPH <"
				+ this.parameters.getGraph() + "> { <" + informationSourceURI
				+ "> a <" + FeedRDFHelper.ITEM_CLASS + "> ; " + "<"
				+ RDFHelper.URL_PROPERTY + ">" + " \"" + item.getLink()
				+ "\"  ; " + "<" + RDFHelper.TITLE_PROPERTY + ">" + " \""
				+ item.getTitle() + "\" " + " . }";
		System.out.println("---> " + queryExpression);
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(
				queryExpression, this.graph);
		vur.exec();
	}

	// ---------------------------------------------------------------------------------------------------

	public void update(Item item) {
		// TODO to be done
	}

	// ---------------------------------------------------------------------------------------------------

	public Item read(String URI) {
		Item item = new Item();
		item.setURI(URI);
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

			if (RDFHelper.TITLE_PROPERTY.equals(predicateURI)) {
				item.setTitle(t.getObject().getLiteral().getValue().toString());
			} else if (RDFHelper.URL_PROPERTY.equals(predicateURI)) {
				item.setLink(t.getObject().getLiteral().getValue().toString());
			}

		}
		return item;
	}

	// ---------------------------------------------------------------------------------------------------

	public Boolean exists(String URI) {

		Node foo1 = NodeFactory.createURI(URI);

		return graph.find(new Triple(foo1, Node.ANY, Node.ANY)).hasNext();

	}

	// ---------------------------------------------------------------------------------------------------

	public void showTriplets() {

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

	}

	// ---------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		String virtuosoURL = "jdbc:virtuoso://localhost:1111";

		String URI = "http://algo";
		Feed feed = new Feed();

		feed.setURI(URI);
		feed.setTitle("arXiv");
		feed
				.setLink("http://localhost:8983/solr/select?facet=true&facet.field=subject&facet.field=setSpec&facet.field=creator&facet.field=date");

		FeedRDFDAO informationSourceRDFDAO = new FeedRDFDAO();
		VirtuosoInformationStoreParameters parameters = new VirtuosoInformationStoreParameters();
		parameters.setGraph("http://feedTest");
		parameters.setHost("localhost");
		parameters.setPort("1111");
		parameters.setUser("dba");
		parameters.setPassword("dba");

		informationSourceRDFDAO.init(parameters);
		System.out.println(".,.,.,.,jjjjjjj");
		if (!informationSourceRDFDAO.exists(URI)) {
			System.out.println("The information source doesn't exist");

			informationSourceRDFDAO.create(feed);
		} else {
			System.out.println("The information source already exists!");
		}

		informationSourceRDFDAO.showTriplets();
		VirtGraph graph = new VirtGraph(parameters.getGraph(), virtuosoURL,
				"dba", "dba");
		Feed readedInformationSource = informationSourceRDFDAO.read(URI);
		System.out.println("Readed information source -> "
				+ readedInformationSource);
		
		if (informationSourceRDFDAO.exists(URI)) {
			System.out.println("The information source now exists :) ");
		}

		graph.clear();
	}
}
