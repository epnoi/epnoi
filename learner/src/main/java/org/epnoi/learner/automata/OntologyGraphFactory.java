package org.epnoi.learner.automata;

import java.util.List;

import org.epnoi.learner.terms.TermVertice;
import org.epnoi.learner.terms.TermsTable;
import org.epnoi.model.RelationsTable;
import org.epnoi.model.Term;
import org.epnoi.model.commons.Parameters;

public class OntologyGraphFactory {

	static OntologyGraph build(Parameters ontologyLearningParamters, TermsTable termsTable, RelationsTable table){
		OntologyGraph initialOntology = new OntologyGraph();
		
		int initialNumberOfTerms = Integer.parseInt((String)ontologyLearningParamters.getParameterValue(OntologyLearningWorkflowParameters.NUMBER_INITIAL_TERMS));

		List<Term> mostProblabeTerms= termsTable.getMostProbable(initialNumberOfTerms);
		for (Term term: mostProblabeTerms){
			TermVertice termVertice = new TermVertice(term);
			initialOntology.addVertex(termVertice);
		}
		
		return initialOntology;
	}
}
