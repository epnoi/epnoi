package org.epnoi.uia.informationstore.dao.solr;

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

public abstract class SOLRDAO {
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

		return true;
	}

	public static boolean test(VirtuosoInformationStoreParameters parameters) {
		boolean testResult=true;
		
		return testResult;
	}

}