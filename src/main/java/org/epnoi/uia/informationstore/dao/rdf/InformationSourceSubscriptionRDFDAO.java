package org.epnoi.uia.informationstore.dao.rdf;

import java.util.Iterator;

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

import epnoi.model.InformationSource;
import epnoi.model.InformationSourceSubscription;
import epnoi.model.Resource;

public class InformationSourceSubscriptionRDFDAO extends RDFDAO {

	// ---------------------------------------------------------------------------------------------------------------------

	public void create(Resource resource) {
		InformationSourceSubscription informationSource = (InformationSourceSubscription) resource;
		String informationSourceURI = informationSource.getURI();

		String queryExpression = "INSERT INTO GRAPH <{GRAPH}>"
				+ "{ <{URI}> a <{INFORMATION_SOURCE_SUBSCRIPTION_CLASS}> ; "
				+ "<{INFORMATION_SOURCE_PROPERTY}> \"{INFORMATION_SOURCE}\". }";

		queryExpression = queryExpression
				.replace("{GRAPH}", this.parameters.getGraph())
				.replace("{URI}", informationSourceURI)
				.replace(
						"{INFORMATION_SOURCE_SUBSCRIPTION_CLASS}",
						InformationSourceSubscriptionRDFHelper.INFORMATION_SOURCE_SUBSCRIPTION_CLASS)
				.replace(
						"{INFORMATION_SOURCE_PROPERTY}",
						InformationSourceSubscriptionRDFHelper.HAS_INFORMATION_SOURCE_PROPERTY)
				.replace("{INFORMATION_SOURCE}",
						informationSource.getInformationSource());
		System.out.println("---> " + queryExpression);
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(
				queryExpression, this.graph);

		vur.exec();

	}

	// ---------------------------------------------------------------------------------------------------------------------

	public Boolean exists(String URI) {

		Node foo1 = NodeFactory.createURI(URI);

		return graph.find(new Triple(foo1, Node.ANY, Node.ANY)).hasNext();

	}

	// ---------------------------------------------------------------------------------------------------------------------

	public void update(InformationSource informationSource) {

	}

	// ---------------------------------------------------------------------------------------------------------------------

	public void remove(String URI) {

		Query sparql = QueryFactory.create("DESCRIBE <" + URI + "> FROM <"
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

	// ---------------------------------------------------------------------------------------------------------------------

	public Resource read(String URI) {
		System.out.println("INFO SOURCE SUBS ----------> "+URI);
		InformationSourceSubscription informationSource = new InformationSourceSubscription();
		informationSource.setURI(URI);
		Query sparql = QueryFactory.create("DESCRIBE <" + URI + "> FROM <"
				+ this.parameters.getGraph() + ">");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, this.graph);

		Model model = vqe.execDescribe();
		Graph g = model.getGraph();
		System.out.println("\nDESCRIBE results: "+URI);
		for (Iterator i = g.find(Node.ANY, Node.ANY, Node.ANY); i.hasNext();) {
			Triple t = (Triple) i.next();
			System.out.println(" { " + t.getSubject() + " SSS "
					+ t.getPredicate().getURI() + " " + t.getObject() + " . }");
			String predicateURI = t.getPredicate().getURI();
			if (InformationSourceSubscriptionRDFHelper.HAS_INFORMATION_SOURCE_PROPERTY
					.equals(predicateURI)) {
				informationSource.setInformationSource((t.getObject()
						.getLiteral().getValue().toString()));
			}

		}
		return informationSource;
	}

	// ---------------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		String virtuosoURL = "jdbc:virtuoso://localhost:1111";

		String URI = "http://www.epnoi.org/informationSources#whatever";

		InformationSourceSubscription informationSource = new InformationSourceSubscription();

		informationSource.setURI(URI);

		informationSource
				.setInformationSource("http://localhost:8983/solr/select?facet=true&facet.field=subject&facet.field=setSpec&facet.field=creator&facet.field=date");

		InformationSourceSubscriptionRDFDAO informationSourceRDFDAO = new InformationSourceSubscriptionRDFDAO();
		VirtuosoInformationStoreParameters parameters = new VirtuosoInformationStoreParameters();
		parameters.setGraph("http://informationSourceTest");
		parameters.setHost("localhost");
		parameters.setPort("1111");
		parameters.setUser("dba");
		parameters.setPassword("dba");

		informationSourceRDFDAO.init(parameters);
		System.out.println(".,.,.,.,jjjjjjj");
		if (!informationSourceRDFDAO.exists(URI)) {
			System.out.println("The information source doesn't exist");

			informationSourceRDFDAO.create(informationSource);
		} else {
			System.out.println("The information source already exists!");
		}

		informationSourceRDFDAO.showTriplets();
		VirtGraph graph = new VirtGraph("http://informationSourceTest",
				virtuosoURL, "dba", "dba");
		InformationSourceSubscription readedInformationSource = (InformationSourceSubscription) informationSourceRDFDAO
				.read(URI);
		System.out.println("Readed information source -> "
				+ readedInformationSource);
		if (informationSourceRDFDAO.exists(URI)) {
			System.out.println("The information source now exists :) ");
		}

		graph.clear();
	}
}
