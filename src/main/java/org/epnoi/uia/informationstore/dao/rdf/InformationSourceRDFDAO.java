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

import epnoi.model.Context;
import epnoi.model.InformationSource;
import epnoi.model.Resource;

public class InformationSourceRDFDAO extends RDFDAO {

	// ---------------------------------------------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		InformationSource informationSource = (InformationSource) resource;
		String informationSourceURI = informationSource.getURI();
		

		String queryExpression = "INSERT INTO GRAPH <{GRAPH}>"
				+ "{ <{URI}> a <{INFORMATION_SOURCE_CLASS}> ; "
				+ "<{URL_PROPERTY}> \"{INFORMATION_SOURCE_URL}\" ; "
				+ "<{HAS_INFORMATION_UNIT_TYPE_PROPERTY}> <{INFORMATION_SOURCE_UNIT_TYPE}> ; "
				+ "<{NAME_PROPERTY}> \"{INFORMATION_SOURCE_NAME}\" . }";

		queryExpression = queryExpression
				.replace("{GRAPH}", this.parameters.getGraph())
				.replace("{URI}", informationSourceURI)
				.replace("{INFORMATION_SOURCE_CLASS}",
						InformationSourceRDFHelper.INFORMATION_SOURCE_CLASS)
				.replace("{URL_PROPERTY}", RDFHelper.URL_PROPERTY)
				.replace("{INFORMATION_SOURCE_URL}", informationSource.getURL())
				.replace("{NAME_PROPERTY}", RDFHelper.NAME_PROPERTY)
				.replace("{INFORMATION_SOURCE_NAME}",
						cleanOddCharacters(informationSource.getName()))
				.replace("{HAS_INFORMATION_UNIT_TYPE_PROPERTY}", InformationSourceRDFHelper.HAS_INFORMATION_UNIT_TYPE)
				.replace("{INFORMATION_SOURCE_UNIT_TYPE}", informationSource.getInformationUnitType());

		
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(
				queryExpression, this.graph);

		vur.exec();

	}

	// ---------------------------------------------------------------------------------------------------------------------

	public void update(InformationSource informationSource) {

	}

	// ---------------------------------------------------------------------------------------------------------------------

	public void remove(String URI) {

	}

	// ---------------------------------------------------------------------------------------------------------------------

	public Resource read(String URI) {
		InformationSource informationSource = new InformationSource();
		informationSource.setURI(URI);
		Query sparql = QueryFactory.create("DESCRIBE <" + URI + "> FROM <"
				+ this.parameters.getGraph() + ">");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, this.graph);

		Model model = vqe.execDescribe();
		Graph g = model.getGraph();
		System.out.println("\nDESCRIBE results:");
		for (Iterator i = g.find(Node.ANY, Node.ANY, Node.ANY); i.hasNext();) {
			Triple t = (Triple) i.next();
			/*
			System.out.println(" { " + t.getSubject() + " SSS "
					+ t.getPredicate().getURI() + " " + t.getObject() + " . }");
			*/
			String predicateURI = t.getPredicate().getURI();
			if (RDFHelper.NAME_PROPERTY.equals(predicateURI)) {
				informationSource.setName(t.getObject().getLiteral().getValue()
						.toString());
			} else if (RDFHelper.URL_PROPERTY.equals(predicateURI)) {
				informationSource.setURL(t.getObject().getLiteral().getValue()
						.toString());
			} else if (RDFHelper.TYPE_PROPERTY.equals(predicateURI)) {
				informationSource.setType(t.getObject().getURI().toString());
			}else if (InformationSourceRDFHelper.HAS_INFORMATION_UNIT_TYPE.equals(predicateURI)) {
				informationSource.setInformationUnitType(t.getObject().getURI().toString());
			}

		}
		return informationSource;
	}

	// ---------------------------------------------------------------------------------------------------------------------

	public Boolean exists(String URI) {

		Node foo1 = NodeFactory.createURI(URI);

		return graph.find(new Triple(foo1, Node.ANY, Node.ANY)).hasNext();

	}
	
	// ---------------------------------------------------------------------------------------------------------------------
	
	public void update(Resource resource) {
		
	}

}
