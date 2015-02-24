package org.epnoi.uia.learner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.epnoi.model.Term;
import org.epnoi.uia.commons.Parameters;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.exceptions.EpnoiInitializationException;
import org.epnoi.uia.learner.relations.Relation;
import org.epnoi.uia.learner.relations.RelationsExtractor;
import org.epnoi.uia.learner.relations.RelationsTable;
import org.epnoi.uia.learner.terms.TermVertice;
import org.epnoi.uia.learner.terms.TermsExtractor;
import org.epnoi.uia.learner.terms.TermsTable;

public class OntologyLearningProcess {
	private Parameters ontologyLearningParameters;
	private TermsExtractor termExtractor;
	private TermsTable termsTable;
	private RelationsTable relationsTable;
	private RelationsExtractor relationsTableExtractor;
	
	private DomainsGatherer domainsGatherer;
	private DomainsTable domainsTable;

	// ---------------------------------------------------------------------------------------------------------

	public void init(Core core, Parameters ontologyLearningParameters) throws EpnoiInitializationException {
		this.ontologyLearningParameters = ontologyLearningParameters;
		
		this.domainsGatherer = new DomainsGatherer();
		this.domainsTable = this.domainsGatherer.gather();

		this.termExtractor = new TermsExtractor();
		this.termExtractor.init(core, this.domainsTable,ontologyLearningParameters);

		this.relationsTableExtractor = new RelationsExtractor();
		this.relationsTableExtractor.init(core, this.domainsTable, ontologyLearningParameters);
		
	}

	// ---------------------------------------------------------------------------------------------------------

	public void execute() {

		double hypernymRelationsThreshold = Double
				.valueOf((String) this.ontologyLearningParameters
						.getParameterValue(OntologyLearningParameters.HYPERNYM_RELATION_EXPANSION_THRESHOLD));
		boolean extractTerms = (boolean) this.ontologyLearningParameters
				.getParameterValue(OntologyLearningParameters.EXTRACT_TERMS);
		if (extractTerms) {
			this.termsTable = this.termExtractor.extract();
		} else {
			this.termsTable = this.termExtractor.retrieve();
		}

		this.relationsTable = this.relationsTableExtractor.extract();
		
		System.exit(0);

		OntologyGraph ontologyNoisyGraph = OntologyGraphFactory.build(
				this.ontologyLearningParameters, this.termsTable,
				this.relationsTable);

		Set<TermVertice> visitedTerms = new HashSet<TermVertice>();

		Set<TermVertice> termsVerticesToExpand = ontologyNoisyGraph.vertexSet();

		do {

			for (TermVertice termVerticeToExpand : termsVerticesToExpand) {
				for (Relation relation : relationsTable.getRelations(
						termVerticeToExpand, hypernymRelationsThreshold)) {
					Term destinationTerm = relation.getDestionation();
					TermVertice destinationTermVertice = new TermVertice(
							destinationTerm);
					ontologyNoisyGraph.addEdge(termVerticeToExpand,
							destinationTermVertice);

					// If the destination term vertice has not been visited, we
					// must add it to the vertices to expnad so that it is
					// considered in the next iteration.
					if (!visitedTerms.contains(destinationTerm)) {
						termsVerticesToExpand.add(destinationTermVertice);
					}
				}
				visitedTerms.add(termVerticeToExpand);
			}
			// We stop the expansion when we have no further term vertices to
			// expand
		} while (termsVerticesToExpand.size() > 0);

		// In future versions the ontology graph should be cleaned here.

	}

	public static void main(String[] args) {
		System.out.println("Starting the Ontology Learning Process!");

		List<String> consideredDomains = Arrays.asList("cs", "math");
		String targetDomain = "cs";
		Double hyperymMinimumThreshold = 0.7;
		boolean extractTerms = false;
		Integer numberInitialTerms = 10;

		Parameters ontologyLearningParameters = new OntologyLearningParameters();
		ontologyLearningParameters.setParameter(
				OntologyLearningParameters.CONSIDERED_DOMAINS,
				consideredDomains);
		ontologyLearningParameters.setParameter(
				OntologyLearningParameters.TARGET_DOMAIN, targetDomain);
		ontologyLearningParameters.setParameter(
				OntologyLearningParameters.HYPERNYM_RELATION_EXPANSION_THRESHOLD,
				hyperymMinimumThreshold);
		ontologyLearningParameters.setParameter(
				OntologyLearningParameters.EXTRACT_TERMS, extractTerms);
		ontologyLearningParameters.setParameter(
				OntologyLearningParameters.NUMBER_INITIAL_TERMS,
				numberInitialTerms);

		Core core = CoreUtility.getUIACore();

		OntologyLearningProcess ontologyLearningProcess = new OntologyLearningProcess();

		try {
			ontologyLearningProcess.init(core, ontologyLearningParameters);
		} catch (EpnoiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Ending the Ontology Learning Process!");
	}

}
