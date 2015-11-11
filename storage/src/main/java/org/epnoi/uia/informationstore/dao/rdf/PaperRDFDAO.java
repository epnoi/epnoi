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
import org.epnoi.model.commons.StringUtils;
import org.epnoi.model.rdf.DublinCoreRDFHelper;
import org.epnoi.model.rdf.RDFHelper;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PaperRDFDAO extends RDFDAO {

	// ---------------------------------------------------------------------------------------------------

	public synchronized void create(Resource resource, Context context) {
		Paper paper = (Paper) resource;
		String paperURI = paper.getUri();
		String queryExpression=null;
		if(paper.getPubDate()!=null){
		queryExpression = "INSERT INTO GRAPH <{GRAPH}>"
				+ "{ <{URI}> a <{PAPER_CLASS}> ; "
				+ "<{PUB_DATE_PROPERTY}> \"{PAPER_PUB_DATE}\"^^xsd:dateTime ; "
				+ "<{TITLE_PROPERTY}>  \"{PAPER_TITLE}\" . }";
		}else{
			queryExpression = "INSERT INTO GRAPH <{GRAPH}>"
					+ "{ <{URI}> a <{PAPER_CLASS}> ; "
					+ "<{TITLE_PROPERTY}>  \"{PAPER_TITLE}\" . }";
		}
		
		String datePubDate = (paper.getPubDate()==null)? "": convertDateFormat(paper.getPubDate());
		queryExpression = queryExpression
				.replace("{GRAPH}", this.parameters.getGraph())
				.replace("{URI}", paperURI)
				.replace("{PAPER_CLASS}", RDFHelper.PAPER_CLASS)
				.replace("{PUB_DATE_PROPERTY}",
						DublinCoreRDFHelper.DATE_PROPERTY)
				.replace("{PAPER_PUB_DATE}",datePubDate)
				.replace("{TITLE_PROPERTY}", DublinCoreRDFHelper.TITLE_PROPERTY)
				.replace("{PAPER_TITLE}", StringUtils.cleanOddCharacters(paper.getTitle()));
		System.out.println("----> " + queryExpression);
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
			this.graph.remove(triple);
		}
	}

	// ---------------------------------------------------------------------------------------------------

	public Resource read(String URI) {
		Paper item = new Paper();
		item.setUri(URI);
		Query sparql = QueryFactory.create("DESCRIBE <" + URI + "> FROM <"
				+ parameters.getGraph() + ">");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, graph);

		Model model = vqe.execDescribe();
		Graph g = model.getGraph();
		// System.out.println("\nDESCRIBE results:");
		for (Iterator<Triple> i = g.find(Node.ANY, Node.ANY, Node.ANY); i
				.hasNext();) {
			Triple t = (Triple) i.next();
			// System.out.println(" { " + t.getSubject() + " SSS "+
			// t.getPredicate().getURI() + " " + t.getObject() + " . }");
			String predicateURI = t.getPredicate().getURI();

			if (DublinCoreRDFHelper.TITLE_PROPERTY.equals(predicateURI)) {
				item.setTitle(t.getObject().getLiteral().getValue().toString());

			} else if (DublinCoreRDFHelper.DATE_PROPERTY.equals(predicateURI)) {
				item.setPubDate(t.getObject().getLiteral().getValue()
						.toString());

			}
		}
		return item;
	}

	// ---------------------------------------------------------------------------------------------------------------------

	public void update(Resource resource) {

	}

	// ---------------------------------------------------------------------------------------------------

	public Boolean exists(String URI) {

		Node foo1 = NodeFactory.createURI(URI);

		return graph.find(new Triple(foo1, Node.ANY, Node.ANY)).hasNext();

	}

	// ---------------------------------------------------------------------------------------------------------------------------------------

	String convertDateFormat(String dateExpression) {
		List<SimpleDateFormat> knownPatterns = new ArrayList<SimpleDateFormat>();
		knownPatterns.add(new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss zzzz", Locale.ENGLISH));
		knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH));
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

}
