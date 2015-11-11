package org.epnoi.uia.informationstore.dao.rdf;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import org.epnoi.model.*;
import org.epnoi.model.rdf.OAIORERDFHelper;
import org.epnoi.model.rdf.RDFHelper;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class ResearchObjectRDFDAO extends RDFDAO {

	public static final String MANIFEST = "/Manifest";

	// ---------------------------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		ResearchObject researchObject = (ResearchObject) resource;

		String researchObjectURI = researchObject.getUri();

		String queryExpression = "PREFIX  xsd:  <http://www.w3.org/2001/XMLSchema#> INSERT INTO GRAPH <{GRAPH}>"
				+ "{"
				+ " <{URI}> a <{AGGREGATION_CLASS}> ."
				+ " <{URI}> a <{RESEARCH_OBJECT_CLASS}> . "
				+ " <{URI}"
				+ MANIFEST
				+ "> a <{RESOURCE_MAP_CLASS}> . "
				+ " <{URI}"
				+ MANIFEST
				+ "> <{DESCRIBES_PROPERTY}> <{URI}> ."
				+ " <{URI}> <{IS_DESCRIBED_BY_PROPERTY}> <{URI}"
				+ MANIFEST
				+ "> ." + "}";

		queryExpression = queryExpression
				.replace("{GRAPH}", parameters.getGraph())
				.replace("{URI}", researchObjectURI)
				.replace("{AGGREGATION_CLASS}",
						OAIORERDFHelper.AGGREGATION_CLASS)
				.replace("{DESCRIBES_PROPERTY}",
						OAIORERDFHelper.DESCRIBES_PROPERTY)
				.replace("{IS_DESCRIBED_BY_PROPERTY}",
						OAIORERDFHelper.IS_DESCRIBED_BY_PROPERTY)
				.replace("{RESEARCH_OBJECT_CLASS}",
						RDFHelper.RESEARCH_OBJECT_CLASS)
				.replace("{RESOURCE_MAP_CLASS}",
						OAIORERDFHelper.RESOURCE_MAP_CLASS);

		System.out.println("queryExpression ----------------------->"+ queryExpression);
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(
				queryExpression, this.graph);
		vur.exec();

		this.addDublinCoreProperties(researchObjectURI + MANIFEST,
				researchObject.getDcProperties());

		Node uriNode = Node.createURI(researchObjectURI);
		Node aggregatesProperty = Node
				.createURI(OAIORERDFHelper.AGGREGATES_PROPERTY);
		Node aggregatedByProperty = Node
				.createURI(OAIORERDFHelper.IS_AGGREGATED_BY_PROPERTY);

		for (String aggreagatedResourceURI : researchObject
				.getAggregatedResources()) {

			Node objectItem = Node.createURI(aggreagatedResourceURI);

			graph.add(new Triple(uriNode, aggregatesProperty, objectItem));
			graph.add(new Triple(objectItem, aggregatedByProperty, uriNode));
		}

	}

	// ---------------------------------------------------------------------------------------------------

	private void addDublinCoreProperties(String URI,
			DublinCoreMetadataElementsSet dublinCoreProperties) {
		Node uriNode = Node.createURI(URI);

		for (Entry<String, List<String>> dublinCoreEntry : dublinCoreProperties
				.getDublinCoreProperties().entrySet()) {
			String propertyURI = dublinCoreEntry.getKey();
			Node propertyNode = Node.createURI(propertyURI);
			for (String value : dublinCoreEntry.getValue()) {
				//System.out.println("p:> " + propertyURI + " v:>" + value);
				graph.add(new Triple(uriNode, propertyNode, Node
						.createURI(value)));
			}
		}
	}

	// ---------------------------------------------------------------------------------------------------

	public void remove(String URI) {

		Query sparql = QueryFactory.create("DESCRIBE <" + URI + "> FROM <"
				+ parameters.getGraph() + ">");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, graph);

		Model model = vqe.execDescribe();
		Graph g = model.getGraph();
		//System.out.println("\nDESCRIBE results:");
		for (Iterator<Triple> i = g.find(Node.ANY, Node.ANY, Node.ANY); i
				.hasNext();) {
			Triple triple = (Triple) i.next();
System.out.println("---------------------------------------------------------___>"+triple.toString());
			graph.remove(triple);

		}

		sparql = QueryFactory.create("DESCRIBE <" + URI + MANIFEST + "> FROM <"
				+ parameters.getGraph() + ">");
		vqe = VirtuosoQueryExecutionFactory.create(sparql, graph);
		model = vqe.execDescribe();
		g = model.getGraph();

		for (Iterator<Triple> i = g.find(Node.ANY, Node.ANY, Node.ANY); i
				.hasNext();) {
			Triple triple = (Triple) i.next();

			graph.remove(triple);

		}

	}

	// ---------------------------------------------------------------------------------------------------

	public ResearchObject read(String URI) {
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
		ResearchObject researchObject = new ResearchObject();
		researchObject.setUri(URI);
		for (Iterator<Triple> i = g.find(Node.ANY, Node.ANY, Node.ANY); i
				.hasNext();) {
			Triple t = i.next();
			String predicateURI = t.getPredicate().getURI();

			if (OAIORERDFHelper.AGGREGATES_PROPERTY.equals(predicateURI)) {
				researchObject.getAggregatedResources().add(
						t.getObject().getURI().toString());
			}

			DublinCoreMetadataElementsSet dcProperties = this.readDC(URI
					+ MANIFEST);
			researchObject.setDcProperties(dcProperties);
		}
		return researchObject;
	}

	// ---------------------------------------------------------------------------------------------------

	public DublinCoreMetadataElementsSet readDC(String manifestURI) {

		DublinCoreMetadataElementsSet dcProperties = new DublinCoreMetadataElementsSet();
		Query sparql = QueryFactory.create("DESCRIBE <" + manifestURI
				+ "> FROM <" + parameters.getGraph() + ">");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, graph);

		Model model = vqe.execDescribe();
		Graph g = model.getGraph();
		// System.out.println("\nDESCRIBE results:");
		if (!g.find(Node.ANY, Node.ANY, Node.ANY).hasNext()) {
			return null;
		}

		for (Iterator<Triple> i = g.find(Node.ANY, Node.ANY, Node.ANY); i
				.hasNext();) {
			Triple t = i.next();
			String predicateURI = t.getPredicate().getURI();

			if (DublinCoreMetadataElementsSetHelper
					.isDublinCoreProperty(predicateURI)) {

				if (DublinCoreMetadataElementsSetHelper
						.isDatatypeProperty(predicateURI)) {

					dcProperties.addPropertyValue(predicateURI, t.getObject()
							.getURI().toString());
				} else if (DublinCoreMetadataElementsSetHelper
						.isObjectProperty(predicateURI)) {
					dcProperties.addPropertyValue(predicateURI, t.getObject()
							.getLiteral().getValue().toString());
				}
			}

		}
		return dcProperties;
	}

	public Boolean exists(String URI) {
		System.out.println("AQUI PINTO > " + URI);
		Node foo1 = NodeFactory.createURI(URI);

		return graph.find(new Triple(foo1, Node.ANY, Node.ANY)).hasNext();

	}

	// ---------------------------------------------------------------------------------------------------

	public void update(Resource resource) {

	}

}
