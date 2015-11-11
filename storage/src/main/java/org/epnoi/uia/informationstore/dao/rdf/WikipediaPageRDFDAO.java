package org.epnoi.uia.informationstore.dao.rdf;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import org.epnoi.model.Context;
import org.epnoi.model.Paper;
import org.epnoi.model.Resource;
import org.epnoi.model.WikipediaPage;
import org.epnoi.model.rdf.RDFHelper;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

import java.util.Iterator;

public class WikipediaPageRDFDAO extends RDFDAO {

	// ---------------------------------------------------------------------------------------------------

	public synchronized void create(Resource resource, Context context) {
		WikipediaPage wikipediaPage = (WikipediaPage) resource;
		String paperURI = wikipediaPage.getUri();

		String queryExpression = "INSERT INTO GRAPH <{GRAPH}>"
				+ "{ <{URI}> a <{WIKIPEDIA_PAGE_CLASS}> . }";

		queryExpression = queryExpression
				.replace("{GRAPH}", parameters.getGraph())
				.replace("{URI}", paperURI)
				.replace("{WIKIPEDIA_PAGE_CLASS}",
						RDFHelper.WIKIPEDIA_PAGE_CLASS);
		//System.out.println("----> " + queryExpression);
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(
				queryExpression, graph);
		vur.exec();

	}

	// ---------------------------------------------------------------------------------------------------

	public void remove(String URI) {

		String feedURI = URI;

		Query sparql = QueryFactory.create("DESCRIBE <" + feedURI + "> FROM <"
				+ parameters.getGraph() + ">");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, graph);

		Model model = vqe.execDescribe();
		Graph g = model.getGraph();
		// System.out.println("\nDESCRIBE results:");
		for (Iterator<Triple> i = g.find(Node.ANY, Node.ANY, Node.ANY); i
				.hasNext();) {
			Triple triple = (Triple) i.next();
			graph.remove(triple);
		}
	}

	// ---------------------------------------------------------------------------------------------------

	public Resource read(String URI) {

		Paper item = new Paper();

		item.setUri(URI);
		/*
		 * Query sparql = QueryFactory.create("DESCRIBE <" + URI + "> FROM <" +
		 * this.parameters.getGraph() + ">"); VirtuosoQueryExecution vqe =
		 * VirtuosoQueryExecutionFactory.create( sparql, this.graph);
		 * 
		 * Model model = vqe.execDescribe(); Graph g = model.getGraph(); //
		 * System.out.println("\nDESCRIBE results:");
		 * 
		 * for (Iterator<Triple> i = g.find(Node.ANY, Node.ANY, Node.ANY); i
		 * .hasNext();) { Triple t = (Triple) i.next(); //
		 * System.out.println(" { " + t.getSubject() + " SSS "+ //
		 * t.getPredicate().getURI() + " " + t.getObject() + " . }"); String
		 * predicateURI = t.getPredicate().getURI();
		 * 
		 * if (DublinCoreRDFHelper.TITLE_PROPERTY.equals(predicateURI)) {
		 * item.setTitle(t.getObject().getLiteral().getValue().toString());
		 * 
		 * }else if (DublinCoreRDFHelper.DATE_PROPERTY.equals(predicateURI)) {
		 * item.setPubDate(t.getObject().getLiteral().getValue().toString());
		 * 
		 * } }
		 */
		return item;
	}

	// ---------------------------------------------------------------------------------------------------------------------

	public void update(Resource resource) {
		throw new RuntimeException(
				"The method update for a WikipediaPageDAO is not defined");
	}

	// ---------------------------------------------------------------------------------------------------------------------

	public Boolean exists(String URI) {

		Node foo1 = NodeFactory.createURI(URI);

		return graph.find(new Triple(foo1, Node.ANY, Node.ANY)).hasNext();

	}

	// ----------------------------------------------------------------------------------------------------------------------
		

}
