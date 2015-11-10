package org.epnoi.uia.informationstore.dao.rdf;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import org.epnoi.model.Context;
import org.epnoi.model.Item;
import org.epnoi.model.Resource;
import org.epnoi.model.commons.StringUtils;
import org.epnoi.model.rdf.DublinCoreRDFHelper;
import org.epnoi.model.rdf.FeedRDFHelper;
import org.epnoi.model.rdf.OAIORERDFHelper;
import org.epnoi.model.rdf.RDFHelper;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;



public class ItemRDFDAO extends RDFDAO {

	// ---------------------------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		Item item = (Item) resource;
		String itemURI = item.getUri();
		String queryExpression = "PREFIX  xsd:  <http://www.w3.org/2001/XMLSchema#> INSERT INTO GRAPH <{GRAPH}>"
				+ "{ <{URI}> a <{ITEM_CLASS}> ; "
				+ "<{URL_PROPERTY}> \"{ITEM_LINK}\" ; "
				+ "<{PUB_DATE_PROPERTY}> \"{ITEM_PUB_DATE}\"^^xsd:dateTime ; "
				+ "<{DESCRIPTION_PROPERTY}> \"{ITEM_DESCRIPTION}\" ; "
				+ "<{AUTHOR_PROPERTY}> \"{ITEM_AUTHOR}\" ; "
				+ "<{IS_AGGREGATED_BY_PROPERTY}> <{INFORMATION_SOURCE_URI}> ; "
				+ "<{TITLE_PROPERTY}>  \"{ITEM_TITLE}\" . }";
		
		queryExpression = queryExpression
				.replace("{GRAPH}", this.parameters.getGraph())
				.replace("{URI}", itemURI)
				.replace("{ITEM_CLASS}", FeedRDFHelper.ITEM_CLASS)
				.replace("{URL_PROPERTY}", RDFHelper.URL_PROPERTY)
				.replace("{ITEM_LINK}", item.getLink())
				.replace("{TITLE_PROPERTY}", DublinCoreRDFHelper.TITLE_PROPERTY)
				.replace("{ITEM_TITLE}", StringUtils.cleanOddCharacters(item.getTitle()))
				.replace("{PUB_DATE_PROPERTY}", FeedRDFHelper.PUB_DATE_PROPERTY)
				.replace("{DESCRIPTION_PROPERTY}",
						FeedRDFHelper.DESCRIPTION_PROPERTY)
				.replace("{ITEM_DESCRIPTION}",
						StringUtils.cleanOddCharacters(item.getDescription()))
				.replace("{ITEM_PUB_DATE}",
						convertDateFormat(item.getPubDate()))
				.replace("{AUTHOR_PROPERTY}", FeedRDFHelper.AUTHOR_PROPERTY)
				.replace("{ITEM_AUTHOR}", item.getAuthor())
				.replace("{IS_AGGREGATED_BY_PROPERTY}", OAIORERDFHelper.IS_AGGREGATED_BY_PROPERTY)
				.replace("{INFORMATION_SOURCE_URI}", context.getParameters().get(Context.INFORMATION_SOURCE_URI));
		

		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(
				queryExpression, graph);
		vur.exec();
	}

	// ---------------------------------------------------------------------------------------------------

	public void remove(String URI) {
		ItemRDFDAO itemRDFDAO = new ItemRDFDAO();
		itemRDFDAO.init(parameters);

		String feedURI = URI;

		Query sparql = QueryFactory.create("DESCRIBE <" + feedURI + "> FROM <"
				+ parameters.getGraph() + ">");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, graph);

		Model model = vqe.execDescribe();
		Graph g = model.getGraph();
		// System.out.println("\nDESCRIBE results:");
		for (Iterator<Triple> i = g.find(Node.ANY, Node.ANY, Node.ANY); i.hasNext();) {
			Triple triple =  i.next();
			graph.remove(triple);
		}
	}

	// ---------------------------------------------------------------------------------------------------

	public Resource read(String URI) {
		Item item = new Item();
		item.setUri(URI);
		Query sparql = QueryFactory.create("DESCRIBE <" + URI + "> FROM <"
				+ this.parameters.getGraph() + ">");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, this.graph);

		Model model = vqe.execDescribe();
		Graph g = model.getGraph();
		// System.out.println("\nDESCRIBE results:");
		for (Iterator i = g.find(Node.ANY, Node.ANY, Node.ANY); i.hasNext();) {
			Triple t = (Triple) i.next();
			// System.out.println(" { " + t.getSubject() + " SSS "+
			// t.getPredicate().getURI() + " " + t.getObject() + " . }");
			String predicateURI = t.getPredicate().getURI();

			if (DublinCoreRDFHelper.TITLE_PROPERTY.equals(predicateURI)) {
				item.setTitle(t.getObject().getLiteral().getValue().toString());
			} else if (RDFHelper.URL_PROPERTY.equals(predicateURI)) {
				item.setLink(t.getObject().getLiteral().getValue().toString());

			} else if (FeedRDFHelper.DESCRIPTION_PROPERTY.equals(predicateURI)) {
				item.setDescription(clean(t.getObject().getLiteral().getValue()
						.toString()));

			} else if (FeedRDFHelper.PUB_DATE_PROPERTY.equals(predicateURI)) {
				item.setPubDate(t.getObject().getLiteral().getValue()
						.toString());
			} else if (FeedRDFHelper.AUTHOR_PROPERTY.equals(predicateURI)) {
				item.setAuthor(t.getObject().getLiteral().getValue().toString());
			} else if (FeedRDFHelper.GUID_PROPERTY.equals(predicateURI)) {
				item.setGuid(t.getObject().getLiteral().getValue().toString());

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

}
