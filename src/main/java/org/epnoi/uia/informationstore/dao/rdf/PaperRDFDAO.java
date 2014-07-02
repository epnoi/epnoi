package org.epnoi.uia.informationstore.dao.rdf;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.epnoi.model.Context;
import org.epnoi.model.Item;
import org.epnoi.model.Paper;
import org.epnoi.model.Resource;

import ucar.nc2.constants._Coordinate;
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

public class PaperRDFDAO extends RDFDAO {

	// ---------------------------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		Paper paper = (Paper) resource;
		String paperURI = paper.getURI();

		String queryExpression = "INSERT INTO GRAPH <{GRAPH}>"
				+ "{ <{URI}> a <{PAPER_CLASS}> ; "
				+ "<{PUB_DATE_PROPERTY}> \"{PAPER_PUB_DATE}\"^^xsd:dateTime ; "
				+ "<{TITLE_PROPERTY}>  \"{PAPER_TITLE}\" . }";

		queryExpression = queryExpression
				.replace("{GRAPH}", this.parameters.getGraph())
				.replace("{URI}", paperURI)
				.replace("{PAPER_CLASS}", RDFHelper.PAPER_CLASS)
				.replace("{PUB_DATE_PROPERTY}",DublinCoreRDFHelper.DATE_PROPERTY)
				.replace("{PAPER_PUB_DATE}", convertDateFormat(paper.getPubDate()))
				.replace("{TITLE_PROPERTY}", DublinCoreRDFHelper.TITLE_PROPERTY)
				.replace("{PAPER_TITLE}", cleanOddCharacters(paper.getTitle()));
		System.out.println("----> " + queryExpression);
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(
				queryExpression, this.graph);
		vur.exec();

	}

	// ---------------------------------------------------------------------------------------------------

	public void remove(String URI) {
		ItemRDFDAO itemRDFDAO = new ItemRDFDAO();
		itemRDFDAO.init(this.parameters);

		String feedURI = URI;

		Query sparql = QueryFactory.create("DESCRIBE <" + feedURI + "> FROM <"
				+ this.parameters.getGraph() + ">");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, this.graph);

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
		item.setURI(URI);
		Query sparql = QueryFactory.create("DESCRIBE <" + URI + "> FROM <"
				+ this.parameters.getGraph() + ">");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, this.graph);

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

			}else if (DublinCoreRDFHelper.DATE_PROPERTY.equals(predicateURI)) {
				item.setPubDate(t.getObject().getLiteral().getValue().toString());

			}
		}
		return item;
	}

	// ---------------------------------------------------------------------------------------------------------------------

	public void update(Resource resource) {

	}

	private String clean(String expression) {
		return expression.replaceAll("[^a-zA-Z0-9]", " ");
	}

	// ---------------------------------------------------------------------------------------------------

	public Boolean exists(String URI) {

		Node foo1 = NodeFactory.createURI(URI);

		return graph.find(new Triple(foo1, Node.ANY, Node.ANY)).hasNext();

	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
/*
	protected String convertDateFormat(String dateExpression) {
		DateFormat formatter = new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss zzzz", Locale.ENGLISH);
		Date date = null;
		try {
			date = formatter.parse(dateExpression);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// "2005-02-28T00:00:00Z"^^xsd:dateTime
		// "2013-12-16T23:44:00+0100"^^xsd:dateTime
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
				Locale.ENGLISH);
		return (dt1.format(date));

	}
*/	
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
