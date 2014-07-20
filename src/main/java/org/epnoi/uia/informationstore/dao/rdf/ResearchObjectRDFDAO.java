package org.epnoi.uia.informationstore.dao.rdf;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.epnoi.model.Context;
import org.epnoi.model.DublinCoreMetadataElementsSet;
import org.epnoi.model.ResearchObject;
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

public class ResearchObjectRDFDAO extends RDFDAO {

	public static final String MANIFEST = "/Manifest";

	// ---------------------------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		ResearchObject researchObject = (ResearchObject) resource;

		// System.out.println("--------------------------------------------------------->"+feed);
		String researchObjectURI = researchObject.getURI();

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

		/*
		 * .replace("{TITLE_PROPERTY}", DublinCoreRDFHelper.TITLE_PROPERTY)
		 * .replace("{FEED_TITLE}", cleanOddCharacters(feed.getTitle()))
		 * .replace("{PUB_DATE_PROPERTY}", FeedRDFHelper.PUB_DATE_PROPERTY);
		 * 
		 * + "<{PUB_DATE_PROPERTY}> \"{FEED_PUB_DATE}\"^^xsd:dateTime ; " +
		 * "<{DESCRIPTION_PROPERTY}> \"{FEED_DESCRIPTION}\" ; "
		 * 
		 * + "<{TITLE_PROPERTY}>  \"{FEED_TITLE}\" . }";
		 * 
		 * .replace("{DESCRIPTION_PROPERTY}",
		 * FeedRDFHelper.DESCRIPTION_PROPERTY) .replace("{FEED_DESCRIPTION}",
		 * cleanOddCharacters(feed.getDescription()))
		 * .replace("{FEED_PUB_DATE}",
		 * 
		 * 
		 * 
		 * DateConverter.convertDateFormat(feed.getPubDate()));
		 */
		System.out.println("queryExpression ----------------------->"
				+ queryExpression);
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(
				queryExpression, this.graph);
		vur.exec();

		this.addDublinCoreProperties(researchObjectURI + MANIFEST,
				researchObject.getDCProperties());

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
				System.out.println("p:> " + propertyURI + " v:>" + value);
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
		System.out.println("\nDESCRIBE results:");
		for (Iterator<Triple> i = g.find(Node.ANY, Node.ANY, Node.ANY); i
				.hasNext();) {
			Triple triple = (Triple) i.next();

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
		researchObject.setURI(URI);
		for (Iterator<Triple> i = g.find(Node.ANY, Node.ANY, Node.ANY); i
				.hasNext();) {
			Triple t = i.next();
			String predicateURI = t.getPredicate().getURI();

			if (OAIORERDFHelper.AGGREGATES_PROPERTY.equals(predicateURI)) {
				researchObject.getAggregatedResources().add(
						t.getObject().getURI().toString());
			}

			DublinCoreMetadataElementsSet dcProperties = this.readDC(URI+MANIFEST);
			researchObject.setDCProperties(dcProperties);
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

			if (dcProperties.isDublinCoreProperty(predicateURI)) {

				if (dcProperties.isDatatypeProperty(predicateURI)) {

					dcProperties.addPropertyValue(predicateURI, t.getObject()
							.getURI().toString());
				} else if (dcProperties.isObjectProperty(predicateURI)) {
					dcProperties.addPropertyValue(predicateURI, t.getObject()
							.getLiteral().getValue().toString());
				}
			}

		}
		return dcProperties;
	}

	// ---------------------------------------------------------------------------------------------------

	/*
	 * if (DublinCoreRDFHelper.TITLE_PROPERTY.equals(predicateURI)) {
	 * feed.setTitle(t.getObject().getLiteral().getValue().toString()); } else
	 * if (RDFHelper.URL_PROPERTY.equals(predicateURI)) {
	 * feed.setLink(t.getObject().getLiteral().getValue().toString()); } else if
	 * (FeedRDFHelper.DESCRIPTION_PROPERTY.equals(predicateURI)) {
	 * feed.setDescription(t.getObject().toString()); } else if
	 * (FeedRDFHelper.PUB_DATE_PROPERTY.equals(predicateURI)) {
	 * System.out.println("----------->" + t.getObject().toString());
	 * feed.setPubDate(t.getObject().getLiteral().getValue() .toString()); }
	 * else if (FeedRDFHelper.COPYRIGHT_PROPERTY.equals(predicateURI)) {
	 * feed.setCopyright(t.getObject().getURI().toString()); } else if
	 * (FeedRDFHelper.LANGUAGE_PROPERTY.equals(predicateURI)) {
	 * feed.setLanguage(t.getObject().getURI().toString());
	 */

	// ---------------------------------------------------------------------------------------------------

	public Boolean exists(String URI) {

		Node foo1 = NodeFactory.createURI(URI);

		return graph.find(new Triple(foo1, Node.ANY, Node.ANY)).hasNext();

	}

	// ---------------------------------------------------------------------------------------------------
	/*
	 * String convertDateFormat(String dateExpression) { List<SimpleDateFormat>
	 * knownPatterns = new ArrayList<SimpleDateFormat>(); knownPatterns.add(new
	 * SimpleDateFormat( "EEE, dd MMM yyyy HH:mm:ss zzzz", Locale.ENGLISH));
	 * 
	 * knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH));
	 * knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
	 * Locale.ENGLISH)); knownPatterns.add(new
	 * SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH));
	 * 
	 * for (SimpleDateFormat pattern : knownPatterns) { try { // Take a try Date
	 * parsedDate = pattern.parse(dateExpression); SimpleDateFormat dt1 = new
	 * SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH); return
	 * (dt1.format(parsedDate)); } catch (ParseException pe) { // Loop on } }
	 * System.err.println("No known Date format found: " + dateExpression);
	 * return null;
	 * 
	 * }
	 */
	// ---------------------------------------------------------------------------------------------------

	public void update(Resource resource) {

	}

}
