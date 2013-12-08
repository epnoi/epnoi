package org.epnoi.uia.informationstore.dao.rdf;

import org.epnoi.uia.parameterization.InformationStoreParameters;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;

import epnoi.model.Resource;

public abstract class RDFDAO {
	private String virtuosoURL = "jdbc:virtuoso://localhost:1111";

	protected VirtuosoInformationStoreParameters parameters;
	protected VirtGraph graph = null;

	abstract public void create(Resource resource);

	public void init(InformationStoreParameters parameters) {

		System.out.println(".............................................. "
				+ parameters);
		this.parameters = (VirtuosoInformationStoreParameters) parameters;
		if ((this.parameters.getPort() != null)
				&& (this.parameters.getHost() != null)) {
			this.virtuosoURL = "jdbc:virtuoso://" + this.parameters.getHost()
					+ ":" + this.parameters.getPort();
			graph = new VirtGraph(this.parameters.getGraph(), virtuosoURL,
					this.parameters.getUser(), this.parameters.getPassword());

		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------

	public ResultSet makeQuery(String query) {

		// Query sparql =
		// QueryFactory.create("select * from <Example3> where {?s ?p ?o}");
		Query sparql = QueryFactory.create(query);

		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, this.graph);

		ResultSet results = vqe.execSelect();
		/*
		 * while (results.hasNext()) { QuerySolution result =
		 * results.nextSolution(); RDFNode graph = result.get("graph"); RDFNode
		 * s = result.get("s"); RDFNode p = result.get("p"); RDFNode o =
		 * result.get("o"); System.out.println(graph + " { " + s + "|||" + p +
		 * "||| " + o + " . }"); }
		 */
		return results;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------

	public Boolean exists(String URI) {

		VirtGraph set = new VirtGraph("http://informationSourceTest",
				virtuosoURL, "dba", "dba");
		Node foo1 = Node.createURI(URI);
		Node bar1 = Node.createURI(RDFHelper.TYPE_PROPERTY);
		Node baz1 = Node
				.createURI(InformationSourceRDFHelper.INFORMATION_SOURCE_CLASS);

		/*
		 * String queryExpression = "ASK WHERE { <" + URI + "> <" +
		 * RDFHelper.TYPE + "> <" +
		 * InformationSourceRDFHelper.INFORMATION_SOURCE_CLASS + "> }";
		 * 
		 * System.out.println("----> " + queryExpression); Query sparql =
		 * QueryFactory.create(queryExpression); VirtuosoQueryExecution vqe =
		 * VirtuosoQueryExecutionFactory.create( sparql, set); vqe =
		 * VirtuosoQueryExecutionFactory.create(sparql, set);
		 * System.out.println("->" + vqe.execAsk()); return vqe.execAsk();
		 */
		// System.out.println("--->"+new Triple(foo1,,baz1));
		return set.contains(foo1, Node.ANY, Node.ANY);

	}

	public static boolean test(VirtuosoInformationStoreParameters parameters) {
		boolean testResult;
		try {
			String virtuosoURL = "jdbc:virtuoso://" + parameters.getHost()
					+ ":" + parameters.getPort();
			VirtGraph testGraph = new VirtGraph(parameters.getGraph(),
					virtuosoURL, parameters.getUser(), parameters.getPassword());
			testResult = true;
		} catch (Exception e) {
			e.printStackTrace();
			testResult = false;
		}
		return testResult;
	}

}