package org.epnoi.learner.terms;

import org.epnoi.model.Term;

public class TermVertice {
	private Term term;

	// -------------------------------------------------------------------------------------------

	public TermVertice(Term term) {
		this.term = term;
	}

	// -------------------------------------------------------------------------------------------

	public Term getTerm() {
		return term;
	}

	// -------------------------------------------------------------------------------------------

	public void setTerm(Term term) {
		this.term = term;
	}

	// -------------------------------------------------------------------------------------------

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TermVertice) {
			Term otherTerm = ((TermVertice) obj).getTerm();
			return otherTerm.equals(this.term);

		}
		return false;
	}

	// -------------------------------------------------------------------------------------------

}
