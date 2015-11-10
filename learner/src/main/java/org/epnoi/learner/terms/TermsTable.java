package org.epnoi.learner.terms;

import org.epnoi.model.Term;

import java.util.*;

/**
 * The Terms Table is used to: 
 * <li>It stores the Terms ordered by the termhood</li>
 * <li>It provides look-up functionality for finding terms based on their URI and their surfaces form</li>
 * @author Rafael Gonzalez-Cabero ({@link https://github.com/fitash})
 *
 */
public class TermsTable {
	private Map<Term, String> orderedTerms;
	private Map<String, Term> terms;
	private Map<String, Term> termsBySurfaceForm;

	// --------------------------------------------------------------------

	public TermsTable() {

		this.orderedTerms = new TreeMap<Term, String>(new TermsComparator());
		this.terms = new HashMap<String, Term>();
		this.terms = new HashMap<>();
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
		this.orderedTerms.put(term, term.getUri());
		this.terms.put(term.getUri(), term);
		this.terms.put(term.getAnnotatedTerm().getWord(), term);
	}

	// --------------------------------------------------------------------

	public Term getTerm(String URI) {
		return this.terms.get(URI);
	}

	// --------------------------------------------------------------------

	public Term getTermBySurfaceForm(String word) {
		return this.termsBySurfaceForm.get(word);
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

	// --------------------------------------------------------------------

	public Collection<Term> getTerms() {
		return this.terms.values();
	}

	// --------------------------------------------------------------------

	public void show(int numberOfDeatiledTerms) {

		System.out
				.println("=====================================================================================================================");
		System.out.println("Terms Table");

		System.out
				.println("=====================================================================================================================");

		System.out.println("# of candidate terms: " + this.size());
		System.out.println("The top most " + numberOfDeatiledTerms
				+ " probable terms are: ");
		int i = 1;
		for (Term term : this.getMostProbable(numberOfDeatiledTerms)) {
			System.out.println("(" + i++ + ")"
					+ term.getAnnotatedTerm().getWord() + " with termhood "
					+ term.getAnnotatedTerm().getAnnotation().getTermhood());

			System.out
					.println("------------------------------------------------------");
			System.out.println(term);
			System.out
					.println("------------------------------------------------------");

		}

		System.out
				.println("=====================================================================================================================");
		System.out
				.println("=====================================================================================================================");
	}
}
