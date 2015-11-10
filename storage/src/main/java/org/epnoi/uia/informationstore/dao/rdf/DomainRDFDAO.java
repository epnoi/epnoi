package org.epnoi.uia.informationstore.dao.rdf;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import org.epnoi.model.Context;
import org.epnoi.model.Domain;
import org.epnoi.model.InformationSource;
import org.epnoi.model.Resource;
import org.epnoi.model.rdf.RDFHelper;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

import java.util.Iterator;

public class DomainRDFDAO extends RDFDAO {


	// ---------------------------------------------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		Domain domain = (Domain) resource;
		String domainURI = domain.getUri();

		// First of all we insert the domain (URI plus its properties)
		String queryExpression = "INSERT INTO GRAPH <{GRAPH}>"

		+ " { <{URI}> a <{DOMAIN_CLASS}> . "
				+ " <{URI}> <{HAS_RESOURCES_PROPERTY}> <{RESOURCES_URI}> . }";

		queryExpression = queryExpression
				.replace("{GRAPH}", parameters.getGraph())
				.replace("{URI}", domainURI)
				.replace("{DOMAIN_CLASS}", RDFHelper.DOMAIN_CLASS)
				.replace("{HAS_RESOURCES_PROPERTY}", RDFHelper.HAS_RESOURCES_PROPERTY)
				.replace("{RESOURCES_URI}", domain.getResources());
		System.out.println("DOMAIN EXPRESSION ...> "+queryExpression);
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
		Query sparql = QueryFactory.create("DESCRIBE <" + URI + "> FROM <"
				+ parameters.getGraph() + ">");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, graph);

		Model model = vqe.execDescribe();
		Graph g = model.getGraph();
		// System.out.println("\nDESCRIBE results:");
		if (!g.find(Node.ANY, Node.ANY, Node.ANY).hasNext()) {
			return null;
		}
		Domain domain = new Domain();
		domain.setUri(URI);
		for (Iterator<Triple> i = g.find(Node.ANY, Node.ANY, Node.ANY); i
				.hasNext();) {
			Triple t = i.next();
			String predicateURI = t.getPredicate().getURI();
			
			System.out.println(predicateURI);
			if (RDFHelper.HAS_RESOURCES_PROPERTY.equals(predicateURI)) {
				System.out.println("ENTRA");
				domain.setResources(
						t.getObject().getURI().toString());
			}
		
		}
		return domain;
	}

	// ---------------------------------------------------------------------------------------------------------------------

	public void update(Resource resource) {

	}

	// ---------------------------------------------------------------------------------------------------------------------

}
