package org.epnoi.uia.informationstore.dao.rdf;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import org.epnoi.model.Context;
import org.epnoi.model.InformationSourceSubscription;
import org.epnoi.model.Resource;
import org.epnoi.model.rdf.InformationSourceSubscriptionRDFHelper;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

import java.util.Iterator;

public class InformationSourceSubscriptionRDFDAO extends RDFDAO {

	// ---------------------------------------------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		InformationSourceSubscription informationSourceSubscription = (InformationSourceSubscription) resource;
		String informationSourceURI = informationSourceSubscription.getUri();

		String queryExpression = "INSERT INTO GRAPH <{GRAPH}>"
				+ "{ <{URI}> a <{INFORMATION_SOURCE_SUBSCRIPTION_CLASS}> ; "
				+ "<{MAX_NUMBER_OF_ITEMS_PROPERTY}> {INFORMATION_SOURCE_NUMBER_OF_ITEMS} ;"
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
						informationSourceSubscription.getInformationSource())
				.replace(
						"{MAX_NUMBER_OF_ITEMS_PROPERTY}",
						InformationSourceSubscriptionRDFHelper.MAX_NUMBER_OF_ITEMS_PROPERTY)
				.replace(
						"{INFORMATION_SOURCE_NUMBER_OF_ITEMS}",
						informationSourceSubscription.getNumberOfItems()
								.toString());

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

	public void update(Resource resource) {
		InformationSourceSubscription informationSource = (InformationSourceSubscription) resource;
		
		
		//System.out.println("q--> "+sparql.toString());
		Query sparql = QueryFactory.create("DESCRIBE <" + informationSource.getUri()
				+ "> FROM <" + this.parameters.getGraph() + ">");
		
		
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, this.graph);

		Model model = vqe.execDescribe();
		Graph g = model.getGraph();
		System.out.println("\nDESCRIBE results: " + informationSource.getUri());
		for (Iterator i = g.find(Node.ANY, Node.ANY, Node.ANY); i.hasNext();) {
			Triple t = (Triple) i.next();

			System.out.println(" { " + t.getSubject() + " SSS "
					+ t.getPredicate().getURI() + " " + t.getObject() + " . }");
			String predicateURI = t.getPredicate().getURI();
			if (InformationSourceSubscriptionRDFHelper.HAS_INFORMATION_SOURCE_PROPERTY
					.equals(predicateURI)) {
				String storedInformationSource = (t.getObject().getLiteral()
						.getValue().toString());
				if (!informationSource.getInformationSource().equals(
						storedInformationSource)) {
					Node objectNode = NodeFactory.createURI(informationSource
							.getInformationSource());
					Triple updatedTriple = new Triple(t.getSubject(),
							t.getPredicate(), objectNode);
					this.graph.remove(t);
					this.graph.add(updatedTriple);

				}
			} else if (InformationSourceSubscriptionRDFHelper.MAX_NUMBER_OF_ITEMS_PROPERTY
					.equals(predicateURI)) {
				String maxNumber = t.getObject().getLiteral().getValue()
						.toString();
				System.out.println("--> maxnumber: "+maxNumber);
				if (!informationSource.getInformationSource().equals(maxNumber)) {
					Node objectNode = NodeFactory.createLiteral(informationSource.getNumberOfItems().toString());
					Triple updatedTriple = new Triple(t.getSubject(),
							t.getPredicate(), objectNode);
					System.out.println("Updating from "+t+" to "+updatedTriple);
					this.graph.remove(t);
					this.graph.add(updatedTriple);
				}

			}

		}
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
		System.out
				.println("INFO SOURCE SUBS ----------> g: " + this.parameters);
		InformationSourceSubscription informationSource = new InformationSourceSubscription();
		informationSource.setUri(URI);
		Query sparql = QueryFactory.create("DESCRIBE <" + URI + "> FROM <"
				+ this.parameters.getGraph() + ">");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, this.graph);

		Model model = vqe.execDescribe();
		Graph g = model.getGraph();
		System.out.println("\nDESCRIBE results: " + URI);
		for (Iterator i = g.find(Node.ANY, Node.ANY, Node.ANY); i.hasNext();) {
			Triple t = (Triple) i.next();
			System.out.println(" { " + t.getSubject() + " SSS "
					+ t.getPredicate().getURI() + " " + t.getObject() + " . }");
			String predicateURI = t.getPredicate().getURI();
			if (InformationSourceSubscriptionRDFHelper.HAS_INFORMATION_SOURCE_PROPERTY
					.equals(predicateURI)) {
				informationSource.setInformationSource((t.getObject()
						.getLiteral().getValue().toString()));
			} else if (InformationSourceSubscriptionRDFHelper.MAX_NUMBER_OF_ITEMS_PROPERTY
					.equals(predicateURI)) {
				informationSource.setNumberOfItems(new Integer(t.getObject()
						.getLiteral().getValue().toString()));
			}

		}
		return informationSource;
	}

	
}
