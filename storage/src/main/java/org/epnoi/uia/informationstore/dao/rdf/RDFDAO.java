package org.epnoi.uia.informationstore.dao.rdf;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.engine.binding.Binding;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.model.parameterization.InformationStoreParameters;
import org.epnoi.model.parameterization.VirtuosoInformationStoreParameters;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class RDFDAO {

	private String virtuosoURL = "jdbc:virtuoso://localhost:1111";

	protected static VirtuosoInformationStoreParameters parameters;
	protected static VirtGraph graph = null;

	abstract  public void create(Resource resource, Context context);

	abstract public void remove(String URI);

	abstract public Resource read(String URI);
	
	abstract public void update(Resource resource);

	// ---------------------------------------------------------------------------------------------------------------------------------------

	public synchronized void init(InformationStoreParameters parameters) {

		 //System.out.println(".............................................. "+parameters);
		if (graph == null) {
			//System.out.println(">>-----------------------> initialized");
			this.parameters = (VirtuosoInformationStoreParameters) parameters;
			if ((this.parameters.getPort() != null)
					&& (this.parameters.getHost() != null)) {
				this.virtuosoURL = "jdbc:virtuoso://"
						+ this.parameters.getHost() + ":"
						+ this.parameters.getPort();
				graph = new VirtGraph(this.parameters.getGraph(), virtuosoURL,
						this.parameters.getUser(),
						this.parameters.getPassword());

			}
		}else{
			//System.out.println(">>----------------------->"+this.graph);
		}
		//System.out.println("El result es "+ this.graph);
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------

	protected ResultSet makeQuery(String query) {
		//System.out.println("-------------------------------" + query);
		Query sparql = QueryFactory.create(query);

		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, this.graph);

		ResultSet results = vqe.execSelect();

		return results;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------

	public List<String> query(String query) {
		List<String> result = new ArrayList<String>();

		ResultSet results = this.makeQuery(query);
		while (results.hasNext()) {
			Binding binding = results.nextBinding();
			System.out.println(" >" + binding);

		}

		return result;
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


	
}