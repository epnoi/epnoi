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
import epnoi.model.User;

public class UserRDFDAO extends RDFDAO {

	// ---------------------------------------------------------------------------------------------------------------------

	public void create(Resource resource) {
		User user = (User) resource;
		String userURI = user.getURI();

		String queryExpression = "INSERT INTO GRAPH <{GRAPH}>"
				+ "{ <{URI}> a <{USER_CLASS}> . }";

		queryExpression = queryExpression
				.replace("{GRAPH}", this.parameters.getGraph())
				.replace("{URI}", userURI)
				.replace("{USER_CLASS}", UserRDFHelper.USER_CLASS);

		System.out.println("---> " + queryExpression);
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
		user.setURI(URI);
		String queryExpression = "DESCRIBE <" + URI + "> FROM <"
				+ this.parameters.getGraph() + ">";
		System.out.println("----------------------------->>>>>>> "
				+ queryExpression);
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

	public static void main(String[] args) {
		String virtuosoURL = "jdbc:virtuoso://localhost:1111";

		String URI = "http://www.epnoi.org/informationSources#whatever";

		User informationSource = new User();

		informationSource.setURI(URI);

		for (int i = 0; i < 10; i++)
			informationSource
					.addInformationSourceSubscription("http://informationSourceSubscription"
							+ i);

		for (int i = 0; i < 10; i++)
			informationSource.addKnowledgeObject("http://knowledgeObject" + i);

		UserRDFDAO userRDFDAO = new UserRDFDAO();
		VirtuosoInformationStoreParameters parameters = new VirtuosoInformationStoreParameters();
		parameters.setGraph("http://informationSourceTest");
		parameters.setHost("localhost");
		parameters.setPort("1111");
		parameters.setUser("dba");
		parameters.setPassword("dba");

		userRDFDAO.init(parameters);

		if (!userRDFDAO.exists(URI)) {
			System.out.println("The user doesn't exist");

			userRDFDAO.create(informationSource);
		} else {
			System.out.println("The user already exists!");
		}

		userRDFDAO.showTriplets();
		VirtGraph graph = new VirtGraph("http://informationSourceTest",
				virtuosoURL, "dba", "dba");
		User readedUser = (User) userRDFDAO.read(URI);
		System.out.println("Readed user -> " + readedUser);
		if (userRDFDAO.exists(URI)) {
			System.out.println("The user source now exists :) ");
		}

		graph.clear();
	}
}
