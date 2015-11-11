package org.epnoi.uia.informationstore.dao.rdf;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import org.epnoi.model.Context;
import org.epnoi.model.InformationSource;
import org.epnoi.model.RelationalSentencesCorpus;
import org.epnoi.model.Resource;
import org.epnoi.model.rdf.RDFHelper;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

public class RelationalSentencesCorpusRDFDAO extends RDFDAO {

	// ---------------------------------------------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		RelationalSentencesCorpus relationalSentencesCorpus = (RelationalSentencesCorpus) resource;
		String informationSourceURI = relationalSentencesCorpus.getUri();

		String queryExpression = "INSERT INTO GRAPH <{GRAPH}> { <{URI}> a <{RELATIONAL_SENTENCES_CORPUS_CLASS}> . }";

		queryExpression = queryExpression
				.replace("{GRAPH}", parameters.getGraph())
				.replace("{URI}", informationSourceURI)
				.replace("{RELATIONAL_SENTENCES_CORPUS_CLASS}",
						RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS);

		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(
				queryExpression, graph);

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
		RelationalSentencesCorpus relationalSentencesCorpus = new RelationalSentencesCorpus();
		return relationalSentencesCorpus;
	}
/*
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
			
			String predicateURI = t.getPredicate().getURI();
			if (RDFHelper.NAME_PROPERTY.equals(predicateURI)) {
				informationSource.setName(t.getObject().getLiteral().getValue()
						.toString());
			} else if (RDFHelper.URL_PROPERTY.equals(predicateURI)) {
				informationSource.setURL(t.getObject().getLiteral().getValue()
						.toString());
			} else if (RDFHelper.TYPE_PROPERTY.equals(predicateURI)) {
				informationSource.setType(t.getObject().getURI().toString());
			} else if (InformationSourceRDFHelper.HAS_INFORMATION_UNIT_TYPE
					.equals(predicateURI)) {
				informationSource.setInformationUnitType(t.getObject().getURI()
						.toString());
			}

		}
		return informationSource;
	}
*/
	// ---------------------------------------------------------------------------------------------------------------------

	public Boolean exists(String URI) {

		Node foo1 = NodeFactory.createURI(URI);

		return graph.find(new Triple(foo1, Node.ANY, Node.ANY)).hasNext();

	}

	// ---------------------------------------------------------------------------------------------------------------------

	public void update(Resource resource) {

	}

}