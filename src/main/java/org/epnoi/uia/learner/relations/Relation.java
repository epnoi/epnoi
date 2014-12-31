package org.epnoi.uia.learner.relations;

import org.epnoi.uia.learner.terms.AnnotatedWord;
import org.epnoi.uia.learner.terms.TermMetadata;

public class Relation {
	private AnnotatedWord<TermMetadata> origin;
	private AnnotatedWord<TermMetadata> desination;
	private String provenanceSentence;

	//------------------------------------------------------------------------------------------------------------
	
	public AnnotatedWord<TermMetadata> getDestionation() {
		// TODO Auto-generated method stub
		return null;
	}
	
	//------------------------------------------------------------------------------------------------------------

	public AnnotatedWord<TermMetadata> getOrigin() {
		return origin;
	}

	//------------------------------------------------------------------------------------------------------------
	
	public void setOrigin(AnnotatedWord<TermMetadata> origin) {
		this.origin = origin;
	}

	//------------------------------------------------------------------------------------------------------------
	
	public AnnotatedWord<TermMetadata> getDesination() {
		return desination;
	}

	//------------------------------------------------------------------------------------------------------------

	public void setDesination(AnnotatedWord<TermMetadata> desination) {
		this.desination = desination;
	}

	//------------------------------------------------------------------------------------------------------------
	
	public String getProvenanceSentence() {
		return provenanceSentence;
	}

	//------------------------------------------------------------------------------------------------------------
	
	public void setProvenanceSentence(String provenanceSentence) {
		this.provenanceSentence = provenanceSentence;
	}

	//------------------------------------------------------------------------------------------------------------

}
