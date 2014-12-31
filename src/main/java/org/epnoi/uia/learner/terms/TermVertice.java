package org.epnoi.uia.learner.terms;


public class TermVertice {
	private AnnotatedWord<TermMetadata> term;

	//-------------------------------------------------------------------------------------------
	
	public TermVertice(AnnotatedWord<TermMetadata> term) {
		this.term=term;
	}
	
	//-------------------------------------------------------------------------------------------

	public AnnotatedWord<TermMetadata> getTerm() {
		return term;
	}
	
	//-------------------------------------------------------------------------------------------

	public void setTerm(AnnotatedWord<TermMetadata> term) {
		this.term = term;
	}
	
	//-------------------------------------------------------------------------------------------

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TermVertice) {
			AnnotatedWord<TermMetadata> otherTerm = ((TermVertice) obj)
					.getTerm();
			return otherTerm.equals(this.term);

		}
		return false;
	}
	
	//-------------------------------------------------------------------------------------------

}
