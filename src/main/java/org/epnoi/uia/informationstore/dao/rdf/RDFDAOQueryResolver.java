package org.epnoi.uia.informationstore.dao.rdf;

import java.util.ArrayList;
import java.util.List;

import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.parameterization.InformationStoreParameters;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class RDFDAOQueryResolver {
	private String virtuosoURL = "jdbc:virtuoso://localhost:1111";

	protected VirtuosoInformationStoreParameters parameters;
	private static final Node typePropertyNode = Node
			.createURI(RDFHelper.TYPE_PROPERTY);
	protected VirtGraph graph = null;

	// ---------------------------------------------------------------------------------------------------------------------------------------

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

	protected ResultSet makeQuery(String query) {

		Query sparql = QueryFactory.create(query);

		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, this.graph);

		ResultSet results = vqe.execSelect();

		return results;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------

	public List<String> query(String query) {
		// showTriplets();

		ArrayList<String> resultURIs = new ArrayList<String>();
		Query sparql = QueryFactory.create(query);

		VirtuosoQueryExecution virtuosoQueryEngine = VirtuosoQueryExecutionFactory
				.create(sparql, this.graph);

		ResultSet results = virtuosoQueryEngine.execSelect();
		while (results.hasNext()) {
			QuerySolution result = results.nextSolution();
			RDFNode uriNode = result.get("uri");
			if (uriNode != null) {
				resultURIs.add(uriNode.toString());
			}
		}

		return resultURIs;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------

	public boolean exists(Selector selector) {
		Node uriNode = Node.createURI(selector.getProperty(SelectorHelper.URI));

		Node classNode = Node.createURI(selector
				.getProperty(SelectorHelper.TYPE));

		return this.graph.find(
				new Triple(uriNode, RDFDAOQueryResolver.typePropertyNode,
						classNode)).hasNext();

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

	protected String cleanOddCharacters(String text) {
		String cleanedText;
		cleanedText = text.replace("\"", "");
		return cleanedText;
	}
}
