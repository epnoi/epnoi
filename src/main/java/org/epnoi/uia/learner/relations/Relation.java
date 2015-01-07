package org.epnoi.uia.learner.relations;

import org.epnoi.model.Term;
import org.epnoi.uia.learner.terms.AnnotatedWord;
import org.epnoi.uia.learner.terms.TermMetadata;

public class Relation {
	private Term origin;
	private Term desination;
	private String provenanceSentence;

	//------------------------------------------------------------------------------------------------------------
	
	public Term getDestionation() {
		// TODO Auto-generated method stub
		return null;
	}
	
	//------------------------------------------------------------------------------------------------------------

	public Term getOrigin() {
		return origin;
	}

	//------------------------------------------------------------------------------------------------------------
	
	public void setOrigin(Term origin) {
		this.origin = origin;
	}

	//------------------------------------------------------------------------------------------------------------
	
	public Term getDesination() {
		return desination;
	}

	//------------------------------------------------------------------------------------------------------------

	public void setDesination(Term desination) {
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
