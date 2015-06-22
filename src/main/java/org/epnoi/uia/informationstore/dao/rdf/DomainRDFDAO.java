package org.epnoi.uia.informationstore.dao.rdf;

import java.util.Iterator;

import org.epnoi.model.Context;
import org.epnoi.model.Domain;
import org.epnoi.model.InformationSource;
import org.epnoi.model.Resource;
import org.epnoi.model.Term;

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
import com.hp.hpl.jena.rdf.model.Model;

public class DomainRDFDAO extends RDFDAO {

	// ---------------------------------------------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		Domain domain = (Domain) resource;
		String domainURI = domain.getURI();

		String queryExpression = "INSERT INTO GRAPH <{GRAPH}>"
				+ "{ <{URI}> a <{DOMAIN_CLASS}> . }";

		queryExpression = queryExpression
				.replace("{GRAPH}", parameters.getGraph())
				.replace("{URI}", domainURI)
				.replace("{DOMAIN_CLASS}", RDFHelper.DOMAIN_CLASS);

		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(
				queryExpression, graph);

		vur.exec();

	}

	// ---------------------------------------------------------------------------------------------------------------------

	public Boolean exists(String URI) {

		Node foo1 = NodeFactory.createURI(URI);

		return graph.find(new Triple(foo1, Node.ANY, Node.ANY)).hasNext();

	}

	// ---------------------------------------------------------------------------------------------------------------------

	public void update(InformationSource informationSource) {
		// TODO Update operation for the Term
	}

	// ---------------------------------------------------------------------------------------------------------------------

	public void remove(String URI) {

		Query sparql = QueryFactory.create("DESCRIBE <" + URI + "> FROM <"
				+ parameters.getGraph() + ">");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, graph);

		Model model = vqe.execDescribe();
		Graph g = model.getGraph();
		// System.out.println("\nDESCRIBE results:");
		for (Iterator<Triple> i = g.find(Node.ANY, Node.ANY, Node.ANY); i
				.hasNext();) {
			Triple triple = i.next();

			graph.remove(triple);

		}
	}

	// ---------------------------------------------------------------------------------------------------------------------

	public Resource read(String URI) {
		return null;
	}

	// ---------------------------------------------------------------------------------------------------------------------

	public void update(Resource resource) {

	}

	// ---------------------------------------------------------------------------------------------------------------------

}
