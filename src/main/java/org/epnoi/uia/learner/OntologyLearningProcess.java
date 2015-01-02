package org.epnoi.uia.learner;

import java.util.HashSet;
import java.util.Set;

import org.epnoi.uia.learner.relations.Relation;
import org.epnoi.uia.learner.relations.RelationsExtractor;
import org.epnoi.uia.learner.relations.RelationsTable;
import org.epnoi.uia.learner.terms.AnnotatedWord;
import org.epnoi.uia.learner.terms.TermMetadata;
import org.epnoi.uia.learner.terms.TermVertice;
import org.epnoi.uia.learner.terms.TerminologyExtractor;
import org.epnoi.uia.learner.terms.TermsTable;

public class OntologyLearningProcess {
	private OntologyLearningParameters ontologyLearningParameters;
	private TermsTable termsTable;
	private RelationsTable relationsTable;

	// ---------------------------------------------------------------------------------------------------------

	public void train() {
		// trainning of hte models goes here

	}

	// ---------------------------------------------------------------------------------------------------------

	public void execute(OntologyLearningParameters ontologyLearningParameters) {
		this.ontologyLearningParameters = ontologyLearningParameters;
		this.termsTable = TerminologyExtractor.extract();
		this.relationsTable = RelationsExtractor.extract();

		OntologyGraph ontologyNoisyGraph = OntologyGraphFactory.build(
				this.ontologyLearningParameters, this.termsTable,
				this.relationsTable);

		Set<TermVertice> visitedTerms = new HashSet<TermVertice>();

		

		Set<TermVertice> termsVerticesToExpand = ontologyNoisyGraph.vertexSet();
		do {

			for (TermVertice termVerticeToExpand : termsVerticesToExpand) {
				for (Relation relation : relationsTable
						.getRelations(
								termVerticeToExpand,
								this.ontologyLearningParameters
										.getParameterValue(OntologyLearningParameters.HYPERNYM_RELATION_THRESHOLD))) {
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

		//In future versions the ontology graph should be cleaned here.
		
	}

	private void expandTerms(Set<TermVertice> termsToExpand,
			OntologyGraph ontologyNoisyGraph) {
		// TODO Auto-generated method stub

	}
}
