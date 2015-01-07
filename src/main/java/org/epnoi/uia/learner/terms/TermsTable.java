package org.epnoi.uia.learner.terms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.epnoi.model.Term;

public class TermsTable {
	Map<String, Term> terms;
	

	// --------------------------------------------------------------------

	public TermsTable() {
		this.terms = new TreeMap<String, Term>(new TermsComparator());
	}

	// --------------------------------------------------------------------

	public List<AnnotatedWord<TermMetadata>> getMostProbable(
			int initialNumberOfTerms) {
		List<AnnotatedWord<TermMetadata>> mostProblableTerms = new ArrayList<AnnotatedWord<TermMetadata>>();
		// LOGIC GOES HERE, IF THEY ARE ORDERED, TAKE N, OTHERWISE, ORDER ADN
		// TAKE N
		return mostProblableTerms;
	}

	// --------------------------------------------------------------------

	public void addTerm(Term term) {

		this.terms.put(term.getAnnotatedTerm().getWord(), term);

	}

	// --------------------------------------------------------------------
	
	public int size() {
		return terms.size();
	}

	// --------------------------------------------------------------------

	class TermsComparator implements Comparator<Term>{
		 
	    @Override
	    public int compare(Term term1, Term term2) {
	        if(term1.getAnnotatedTerm().getAnnotation().getTermhood() > term2.getAnnotatedTerm().getAnnotation().getTermhood()){
	            return 1;
	        } else {
	            return -1;
	        }
	    }
	}
	
	// --------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "TermsTable [terms=" + terms + "]";
	}

	
}
