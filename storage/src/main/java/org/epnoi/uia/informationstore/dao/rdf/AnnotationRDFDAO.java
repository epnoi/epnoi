package org.epnoi.uia.informationstore.dao.rdf;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import org.epnoi.model.Annotation;
import org.epnoi.model.Context;
import org.epnoi.model.InformationSource;
import org.epnoi.model.Resource;
import org.epnoi.model.commons.StringUtils;
import org.epnoi.model.rdf.AnnotationOntologyRDFHelper;
import org.epnoi.model.rdf.AnnotationRDFHelper;
import org.epnoi.model.rdf.RDFHelper;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

import java.util.Iterator;

public class AnnotationRDFDAO extends RDFDAO {

	// ---------------------------------------------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		Annotation annotation = (Annotation) resource;
		//System.out.println("The last annotation> " + annotation);
		String userURI = annotation.getUri();

		String queryExpression = null;

		if (annotation.getHasTopic() != null) {

			// Annotation of a topic (i.e. annotat
			queryExpression = "INSERT INTO GRAPH <{GRAPH}>"
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
					.replace(
							"{ANNOTATES_DOCUMENT_PROPERTY}",
							AnnotationOntologyRDFHelper.ANNOTATES_DOCUMENT_PROPERTY)
					.replace("{ANNOTATED_DOCUMENT_URI}",
							annotation.getAnnotatesResource());
		} else {
			queryExpression = "INSERT INTO GRAPH <{GRAPH}>"
					+ "{ <{URI}> a <{ANNOTATION_CLASS}> ; "
					+ "<{ANNOTATES_DOCUMENT_PROPERTY}> <{ANNOTATED_DOCUMENT_URI}> ;"
					+ "<{LABEL_PROPERTY}> \"{LABEL}\" . }";

			queryExpression = queryExpression
					.replace("{GRAPH}", parameters.getGraph())
					.replace("{URI}", userURI)
					.replace("{ANNOTATION_CLASS}",
							AnnotationRDFHelper.ANNOTATION_CLASS)
					.replace("{LABEL_PROPERTY}", RDFHelper.LABEL_PROPERTY)
					.replace("{LABEL}",
							StringUtils.cleanOddCharacters(annotation.getLabel()))
					.replace(
							"{ANNOTATES_DOCUMENT_PROPERTY}",
							AnnotationOntologyRDFHelper.ANNOTATES_DOCUMENT_PROPERTY)
					.replace("{ANNOTATED_DOCUMENT_URI}",
							annotation.getAnnotatesResource());
		}

		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(
				queryExpression, graph);
		vur.exec();

		/*
		 * Node Property = Node.createURI(UserRDFHelper.OWNS_PROPERTY);
		 * 
		 * 
		 * Node objectKnowledgeObject = Node.createURI(knowledgeObjectURI);
		 * 
		 * this.graph.add(new Triple(userNode, ownsProperty,
		 * objectKnowledgeObject));
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

	// ---------------------------------------------------------------------------------------------------------------------

	public Resource read(String URI) {
		Annotation annotation = new Annotation();
		annotation.setUri(URI);
		String queryExpression = "DESCRIBE <" + URI + "> FROM <"
				+ parameters.getGraph() + ">";
		/*
		 * System.out.println("----------------------------->>>>>>> " +
		 * queryExpression);
		 */
		Query sparql = QueryFactory.create(queryExpression);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, graph);

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
				annotation.setHasTopic((t.getObject().getURI().toString()));
			} else if (RDFHelper.LABEL_PROPERTY
					.equals(predicateURI)) {
				annotation.setLabel((t.getObject().getLiteral().getValue().toString()));
			}
		}

		return annotation;
	}

	// ---------------------------------------------------------------------------------------------------------------------

	public void update(Resource resource) {

	}

	// ---------------------------------------------------------------------------------------------------------------------

}