package org.epnoi.uia.learner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.learner.relations.Relation;
import org.epnoi.uia.learner.relations.RelationsExtractor;
import org.epnoi.uia.learner.relations.RelationsTable;
import org.epnoi.uia.learner.terms.AnnotatedWord;
import org.epnoi.uia.learner.terms.TermMetadata;
import org.epnoi.uia.learner.terms.TermVertice;
import org.epnoi.uia.learner.terms.TermsExtractor;
import org.epnoi.uia.learner.terms.TermsTable;

public class OntologyLearningProcess {
	private OntologyLearningParameters ontologyLearningParameters;
	private TermsExtractor termExtractor;
	private TermsTable termsTable;
	private RelationsTable relationsTable;

	// ---------------------------------------------------------------------------------------------------------

	public void init(Core core, OntologyLearningParameters ontologyLearningParameters) {
		this.termExtractor = new TermsExtractor();
		this.termExtractor.init(core,ontologyLearningParameters);

	}

	// ---------------------------------------------------------------------------------------------------------

	public void execute(OntologyLearningParameters ontologyLearningParameters) {

		this.ontologyLearningParameters = ontologyLearningParameters;

		double hypernymRelationsThreshold = Double
				.valueOf((String) this.ontologyLearningParameters
						.getParameterValue(OntologyLearningParameters.HYPERNYM_RELATION_THRESHOLD));
		boolean extractTerms = (boolean) this.ontologyLearningParameters
				.getParameterValue(OntologyLearningParameters.EXTRACT_TERMS);
		if (extractTerms) {
			this.termsTable = this.termExtractor.extract();	
		} else {
			this.termsTable = this.termExtractor.retrieve();
		}

		this.relationsTable = RelationsExtractor.extract();

		OntologyGraph ontologyNoisyGraph = OntologyGraphFactory.build(
				this.ontologyLearningParameters, this.termsTable,
				this.relationsTable);

		Set<TermVertice> visitedTerms = new HashSet<TermVertice>();

		Set<TermVertice> termsVerticesToExpand = ontologyNoisyGraph.vertexSet();

		do {

			for (TermVertice termVerticeToExpand : termsVerticesToExpand) {
				for (Relation relation : relationsTable.getRelations(
						termVerticeToExpand, hypernymRelationsThreshold)) {
					AnnotatedWord<TermMetadata> destinationTerm = relation
							.getDestionation();
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

		OntologyLearningParameters ontologyLearningParameters = new OntologyLearningParameters();
		ontologyLearningParameters.setParameter(
				OntologyLearningParameters.CONSIDERED_DOMAINS,
				consideredDomains);
		ontologyLearningParameters.setParameter(
				OntologyLearningParameters.TARGET_DOMAIN, targetDomain);
		ontologyLearningParameters.setParameter(
				OntologyLearningParameters.HYPERNYM_RELATION_THRESHOLD,
				hyperymMinimumThreshold);
		ontologyLearningParameters.setParameter(
				OntologyLearningParameters.EXTRACT_TERMS, extractTerms);
		ontologyLearningParameters.setParameter(
				OntologyLearningParameters.NUMBER_INITIAL_TERMS,
				numberInitialTerms);

		Core core = CoreUtility.getUIACore();
		
		OntologyLearningProcess ontologyLearningProcess = new OntologyLearningProcess();

		ontologyLearningProcess.init(core, ontologyLearningParameters);
		
		
		
		System.out.println("Ending the Ontology Learning Process!");
	}

}
