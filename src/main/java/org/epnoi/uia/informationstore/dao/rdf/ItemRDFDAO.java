package org.epnoi.uia.informationstore.dao.rdf;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;

import virtuoso.jena.driver.VirtGraph;
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

import epnoi.model.Feed;
import epnoi.model.Item;
import epnoi.model.Resource;

public class ItemRDFDAO extends RDFDAO {

	// ---------------------------------------------------------------------------------------------------

	public void create(Resource resource) {
		Item item = (Item) resource;
		String itemURI = item.getURI();
		String queryExpression = "INSERT INTO GRAPH <{GRAPH}>"
				+ "{ <{URI}> a <{ITEM_CLASS}> ; "
				+ "<{URL_PROPERTY}> \"{ITEM_LINK}\" ; "
				+ "<{PUB_DATE_PROPERTY}> \"{ITEM_PUB_DATE}\" ; "
				+ "<{DESCRIPTION_PROPERTY}> \"{ITEM_DESCRIPTION}\" ; "
				+ "<{AUTHOR_PROPERTY}> \"{ITEM_AUTHOR}\" ; "
				+ "<{TITLE_PROPERTY}>  \"{ITEM_TITLE}\" . }";
		//System.out.println("....> " + item);
		queryExpression = queryExpression
				.replace("{GRAPH}", this.parameters.getGraph())
				.replace("{URI}", itemURI)
				.replace("{ITEM_CLASS}", FeedRDFHelper.ITEM_CLASS)
				.replace("{URL_PROPERTY}", RDFHelper.URL_PROPERTY)
				.replace("{ITEM_LINK}", item.getLink())
				.replace("{TITLE_PROPERTY}", RDFHelper.TITLE_PROPERTY)
				.replace("{ITEM_TITLE}", cleanOddCharacters(item.getTitle()))
				.replace("{PUB_DATE_PROPERTY}", FeedRDFHelper.PUB_DATE_PROPERTY)
				.replace("{DESCRIPTION_PROPERTY}",
						FeedRDFHelper.DESCRIPTION_PROPERTY)
				.replace("{ITEM_DESCRIPTION}",
						cleanOddCharacters(item.getDescription()))
				.replace("{ITEM_PUB_DATE}",
						convertDateFormat(item.getPubDate()))
				.replace("{AUTHOR_PROPERTY}", FeedRDFHelper.AUTHOR_PROPERTY)
				.replace("{ITEM_AUTHOR}", item.getAuthor());
		/*
		 * String queryExpression = "INSERT INTO GRAPH <" +
		 * this.parameters.getGraph() + "> { <" + itemURI + "> a <" +
		 * FeedRDFHelper.ITEM_CLASS + "> ; " + "<" + RDFHelper.URL_PROPERTY +
		 * ">" + " \"" + item.getLink() + "\"  ; " + "<" +
		 * RDFHelper.TITLE_PROPERTY + ">" + " \"" + item.getTitle() + "\" " +
		 * " . }";
		 */

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
		for (Iterator i = g.find(Node.ANY, Node.ANY, Node.ANY); i.hasNext();) {
			Triple triple = (Triple) i.next();
			this.graph.remove(triple);
		}
	}

	// ---------------------------------------------------------------------------------------------------

	public Resource read(String URI) {
		Item item = new Item();
		item.setURI(URI);
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

			if (RDFHelper.TITLE_PROPERTY.equals(predicateURI)) {
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
	
	
 private String clean(String expression){
	 return expression.replaceAll("[^a-zA-Z0-9]"," ");
 }
	

	// ---------------------------------------------------------------------------------------------------

	public Boolean exists(String URI) {

		Node foo1 = NodeFactory.createURI(URI);

		return graph.find(new Triple(foo1, Node.ANY, Node.ANY)).hasNext();

	}

	// ---------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		String virtuosoURL = "jdbc:virtuoso://localhost:1111";

		String URI = "http://algo";
		Feed feed = new Feed();

		feed.setURI(URI);
		feed.setTitle("arXiv");
		feed.setLink("http://localhost:8983/solr/select?facet=true&facet.field=subject&facet.field=setSpec&facet.field=creator&facet.field=date");

		FeedRDFDAO informationSourceRDFDAO = new FeedRDFDAO();
		VirtuosoInformationStoreParameters parameters = new VirtuosoInformationStoreParameters();
		parameters.setGraph("http://feedTest");
		parameters.setHost("localhost");
		parameters.setPort("1111");
		parameters.setUser("dba");
		parameters.setPassword("dba");

		informationSourceRDFDAO.init(parameters);
		System.out.println(".,.,.,.,jjjjjjj");
		if (!informationSourceRDFDAO.exists(URI)) {
			System.out.println("The information source doesn't exist");

			informationSourceRDFDAO.create(feed);
		} else {
			System.out.println("The information source already exists!");
		}

		informationSourceRDFDAO.showTriplets();
		VirtGraph graph = new VirtGraph(parameters.getGraph(), virtuosoURL,
				"dba", "dba");
		Feed readedInformationSource = informationSourceRDFDAO.read(URI);
		System.out.println("Readed information source -> "
				+ readedInformationSource);

		if (informationSourceRDFDAO.exists(URI)) {
			System.out.println("The information source now exists :) ");
		}

		graph.clear();
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
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		return (dt1.format(date));

	}

}
