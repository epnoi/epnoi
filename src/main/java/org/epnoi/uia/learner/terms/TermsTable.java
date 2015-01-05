package org.epnoi.uia.learner.terms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.epnoi.model.Term;

public class TermsTable {
	Map<String, Term> terms;

	//--------------------------------------------------------------------

	public TermsTable() {
		this.terms = new HashMap<String, Term>();
	}

	//--------------------------------------------------------------------
	
	public List<AnnotatedWord<TermMetadata>> getMostProbable(
			int initialNumberOfTerms) {
		List<AnnotatedWord<TermMetadata>> mostProblableTerms = new ArrayList<AnnotatedWord<TermMetadata>>();
		// LOGIC GOES HERE, IF THEY ARE ORDERED, TAKE N, OTHERWISE, ORDER ADN
		// TAKE N
		return mostProblableTerms;
	}

	//--------------------------------------------------------------------
	
	public void addTerm(Term term) {

		this.terms.put(term.getAnnotatedTerm().getWord(), term);

	}

}
