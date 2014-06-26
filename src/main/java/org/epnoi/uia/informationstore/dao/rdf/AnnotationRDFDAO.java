package org.epnoi.uia.informationstore.dao.rdf;

import java.util.Iterator;

import org.epnoi.model.Annotation;
import org.epnoi.model.Context;
import org.epnoi.model.InformationSource;
import org.epnoi.model.Resource;

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

public class AnnotationRDFDAO extends RDFDAO {

	// ---------------------------------------------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		Annotation annotation = (Annotation) resource;
		String userURI = annotation.getURI();

		String queryExpression = "INSERT INTO GRAPH <{GRAPH}>"
				+ "{ <{URI}> a <{ANNOTATION_CLASS}> ; "
				+ "<{ANNOTATES_DOCUMENT_PROPERTY}> <{ANNOTATED_DOCUMENT_URI}> ;"
				+ "<{HAS_TOPIC_PROPERTY}> <{TOPIC_URI}> . }";

		queryExpression = queryExpression
				.replace("{GRAPH}", this.parameters.getGraph())
				.replace("{URI}", userURI)
				.replace("{ANNOTATION_CLASS}",
						AnnotationRDFHelper.ANNOTATION_CLASS)
				.replace("{HAS_TOPIC_PROPERTY}",
						AnnotationOntologyRDFHelper.HAS_TOPIC_PROPERTY)
				.replace("{TOPIC_URI}", annotation.getHasTopic())
				.replace("{ANNOTATES_DOCUMENT_PROPERTY}",
						AnnotationOntologyRDFHelper.ANNOTATES_DOCUMENT_PROPERTY)
				.replace("{ANNOTATED_DOCUMENT_URI}",
						annotation.getAnnotatesResource());

		// System.out.println(" ------> queryExpression " + queryExpression);
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(
				queryExpression, this.graph);

		vur.exec();

		/*
		 * Node ownsProperty = Node.createURI(UserRDFHelper.OWNS_PROPERTY);
		 * 
		 * for (String knowledgeObjectURI : annotation.getKnowledgeObjects()) {
		 * 
		 * Node objectKnowledgeObject = Node.createURI(knowledgeObjectURI);
		 * 
		 * this.graph.add(new Triple(userNode, ownsProperty,
		 * objectKnowledgeObject));
		 * 
		 * }
		 */
	}

	// ---------------------------------------------------------------------------------------------------------------------

	public Boolean exists(String URI) {

		Node uriNode = NodeFactory.createURI(URI);

		return graph.find(new Triple(uriNode, Node.ANY, Node.ANY)).hasNext();

	}

	// ---------------------------------------------------------------------------------------------------------------------

	public void update(InformationSource informationSource) {

	}

	// ---------------------------------------------------------------------------------------------------------------------

	public void remove(String URI) {

		Query sparql = QueryFactory.create("DESCRIBE <" + URI + "> FROM <"
				+ this.parameters.getGraph() + ">");
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

	// ---------------------------------------------------------------------------------------------------------------------

	public Resource read(String URI) {
		Annotation annotation = new Annotation();
		annotation.setURI(URI);
		String queryExpression = "DESCRIBE <" + URI + "> FROM <"
				+ this.parameters.getGraph() + ">";
		/*
		 * System.out.println("----------------------------->>>>>>> " +
		 * queryExpression);
		 */
		Query sparql = QueryFactory.create(queryExpression);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, this.graph);

		Model model = vqe.execDescribe();
		Graph g = model.getGraph();

		for (Iterator<Triple> i = g.find(Node.ANY, Node.ANY, Node.ANY); i
				.hasNext();) {
			Triple t = (Triple) i.next();

			String predicateURI = t.getPredicate().getURI();

			if (AnnotationOntologyRDFHelper.ANNOTATES_DOCUMENT_PROPERTY
					.equals(predicateURI)) {
				annotation.setAnnotatesResource((t.getObject().getURI()
						.toString()));
			} else if (AnnotationOntologyRDFHelper.HAS_TOPIC_PROPERTY
					.equals(predicateURI)) {
				annotation.setHasTopic((t.getObject().getURI()
						.toString()));
			}

		}

		return annotation;
	}

	// ---------------------------------------------------------------------------------------------------------------------

	public void update(Resource resource) {

	}

	// ---------------------------------------------------------------------------------------------------------------------

}