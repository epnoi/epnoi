package org.epnoi.uia.informationstore.dao.rdf;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import org.epnoi.model.Context;
import org.epnoi.model.InformationSource;
import org.epnoi.model.Resource;
import org.epnoi.model.User;
import org.epnoi.model.rdf.UserRDFHelper;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

import java.util.Iterator;

public class UserRDFDAO extends RDFDAO {

	// ---------------------------------------------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		User user = (User) resource;
		String userURI = user.getUri();

		String queryExpression = "INSERT INTO GRAPH <{GRAPH}>"
				+ "{ <{URI}> a <{USER_CLASS}> . }";

		queryExpression = queryExpression
				.replace("{GRAPH}", this.parameters.getGraph())
				.replace("{URI}", userURI)
				.replace("{USER_CLASS}", UserRDFHelper.USER_CLASS);

		//System.out.println("AQUI MELON---------------------------------> " + queryExpression);
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(
				queryExpression, this.graph);

		vur.exec();
		// Here we add the user's subscriptions to information sources
		Node userNode = Node.createURI(userURI);
		Node isSubscribedToProperty = Node
				.createURI(UserRDFHelper.IS_SUBSCRIBED_PROPERTY);
		for (String informationSourceSubscription : user
				.getInformationSourceSubscriptions()) {
			// System.out.println(item.getURI());

			Node informationSourceSubscriptionNode = Node
					.createURI(informationSourceSubscription);

			this.graph.add(new Triple(userNode, isSubscribedToProperty,
					informationSourceSubscriptionNode));

		}

		// We add the relationships between users and their Knowledge Objects
		Node ownsProperty = Node.createURI(UserRDFHelper.OWNS_PROPERTY);

		for (String knowledgeObjectURI : user.getKnowledgeObjects()) {
			// System.out.println(item.getURI());

			Node objectKnowledgeObject = Node.createURI(knowledgeObjectURI);

			this.graph.add(new Triple(userNode, ownsProperty,
					objectKnowledgeObject));

		}

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
		User user = new User();
		user.setUri(URI);
		String queryExpression = "DESCRIBE <" + URI + "> FROM <"
				+ this.parameters.getGraph() + ">";
		/*
		System.out.println("----------------------------->>>>>>> "
				+ queryExpression);
		*/
		Query sparql = QueryFactory.create(queryExpression);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, this.graph);

		Model model = vqe.execDescribe();
		Graph g = model.getGraph();

		for (Iterator i = g.find(Node.ANY, Node.ANY, Node.ANY); i.hasNext();) {
			Triple t = (Triple) i.next();

			String predicateURI = t.getPredicate().getURI();
			
			if (UserRDFHelper.IS_SUBSCRIBED_PROPERTY.equals(predicateURI)) {
				user.addInformationSourceSubscription((t.getObject().getURI()
						.toString()));
			} else if (UserRDFHelper.OWNS_PROPERTY.equals(predicateURI)) {
				user.addKnowledgeObject((t.getObject().getURI().toString()));
			}

		}

		return user;
	}

	// ---------------------------------------------------------------------------------------------------------------------
	
	
		public void update(Resource resource) {
			
		}
	
	// ---------------------------------------------------------------------------------------------------------------------
	
}
