package org.epnoi.uia.learner.terms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.epnoi.model.Term;

public class TermsTable {
	Map<Term, String> orderedTerms;
	Map<String, Term> terms;

	// --------------------------------------------------------------------

	public TermsTable() {

		this.orderedTerms = new TreeMap<Term, String>(new TermsComparator());
		this.terms = new HashMap<String, Term>();
	}

	// --------------------------------------------------------------------

	public List<Term> getMostProbable(int initialNumberOfTerms) {
		List<Term> mostProblableTerms = new ArrayList<Term>();
		Iterator<Term> termsIt = this.orderedTerms.keySet().iterator();
		int i = 0;
		while (i < initialNumberOfTerms && termsIt.hasNext()) {

			Term term = termsIt.next();
			mostProblableTerms.add(term);
			i++;
		}

		return mostProblableTerms;
	}

	// --------------------------------------------------------------------

	public void addTerm(Term term) {
		this.orderedTerms.put(term, term.getURI());
		this.terms.put(term.getURI(), term);
	}

	// --------------------------------------------------------------------

	public Term getTerm(String URI) {
		return this.terms.get(URI);
	}

	// --------------------------------------------------------------------

	public boolean hasTerm(String URI) {
		return (this.terms.get(URI) != null);
	}

	// --------------------------------------------------------------------

	public int size() {
		return terms.size();
	}

	// --------------------------------------------------------------------

	class TermsComparator implements Comparator<Term> {
		public TermsComparator() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public int compare(Term term1, Term term2) {
			if (term1.getAnnotatedTerm().getAnnotation().getTermhood() < term2
					.getAnnotatedTerm().getAnnotation().getTermhood()) {
				return 1;
			} else {
				return -1;
			}
		}
	}

	// --------------------------------------------------------------------

	@Override
	public String toString() {
		return "TermsTable [terms=" + orderedTerms + "]";
	}

}
