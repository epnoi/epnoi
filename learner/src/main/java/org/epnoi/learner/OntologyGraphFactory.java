package org.epnoi.learner;

import org.epnoi.learner.terms.TermVertice;
import org.epnoi.learner.terms.TermsTable;
import org.epnoi.model.RelationsTable;
import org.epnoi.model.Term;
import org.epnoi.model.commons.Parameters;

import java.util.List;

public class OntologyGraphFactory {

	static OntologyGraph build(Parameters ontologyLearningParamters, TermsTable termsTable, RelationsTable table){
		OntologyGraph initialOntology = new OntologyGraph();
		
		int initialNumberOfTerms = Integer.parseInt((String)ontologyLearningParamters.getParameterValue(LearningParameters.NUMBER_INITIAL_TERMS));

		List<Term> mostProblabeTerms= termsTable.getMostProbable(initialNumberOfTerms);
		for (Term term: mostProblabeTerms){
			TermVertice termVertice = new TermVertice(term);
			initialOntology.addVertex(termVertice);
		}
		
		return initialOntology;
	}
}
