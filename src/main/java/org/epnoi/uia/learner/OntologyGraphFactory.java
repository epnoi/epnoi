package org.epnoi.uia.learner;

import java.util.List;

import org.epnoi.uia.learner.relations.RelationsTable;
import org.epnoi.uia.learner.terms.AnnotatedWord;
import org.epnoi.uia.learner.terms.TermMetadata;
import org.epnoi.uia.learner.terms.TermVertice;
import org.epnoi.uia.learner.terms.TermsTable;

public class OntologyGraphFactory {

	static OntologyGraph build(OntologyLearningParameters ontologyLearningParamters, TermsTable termsTable, RelationsTable table){
		OntologyGraph initialOntology = new OntologyGraph();
		
		int initialNumberOfTerms = Integer.parseInt((String)ontologyLearningParamters.getParameterValue(OntologyLearningParameters.NUMBER_INITIAL_TERMS));

		List<AnnotatedWord<TermMetadata>> mostProblabeTerms= termsTable.getMostProbable(initialNumberOfTerms);
		for (AnnotatedWord<TermMetadata> term: mostProblabeTerms){
			TermVertice termVertice = new TermVertice(term);
			initialOntology.addVertex(termVertice);
		}
		
		return initialOntology;
	}
}
