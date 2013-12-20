package org.epnoi.uia.informationstore.dao.rdf;

import org.epnoi.uia.parameterization.InformationStoreParameters;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

import epnoi.model.Resource;

public abstract class RDFDAO {
	private String virtuosoURL = "jdbc:virtuoso://localhost:1111";

	protected VirtuosoInformationStoreParameters parameters;
	protected VirtGraph graph = null;

	abstract public void create(Resource resource);
	
	abstract public void remove(String URI);

	public void init(InformationStoreParameters parameters) {

		// System.out.println(".............................................. "+
		// parameters);
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

		Query sparql = QueryFactory.create(query);

		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, this.graph);

		ResultSet results = vqe.execSelect();

		return results;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------

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

	// ---------------------------------------------------------------------------------------------------------------------------------------

	public void showTriplets() {
		System.out
				.println("SHOWING TRIPLETS-----------------------------------------------------------------------------------------------------");

		Query sparql = QueryFactory.create("SELECT * FROM <"
				+ this.parameters.getGraph() + ">  WHERE { { ?s ?p ?o } }");

		VirtuosoQueryExecution virtuosoQueryEngine = VirtuosoQueryExecutionFactory
				.create(sparql, this.graph);

		ResultSet results = virtuosoQueryEngine.execSelect();
		while (results.hasNext()) {
			QuerySolution result = results.nextSolution();
			RDFNode s = result.get("s");
			RDFNode p = result.get("p");
			RDFNode o = result.get("o");
			System.out.println(" { " + s + " | " + p + " | " + o + " }");
		}
		System.out
				.println("-----------------------------------------------------------------------------------------------------");
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------------------
	
	protected String cleanOddCharacters(String text){
		String cleanedText;
		cleanedText=text.replace("\"", "");
		return cleanedText;
	}
}