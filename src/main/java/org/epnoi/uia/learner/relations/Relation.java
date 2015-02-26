package org.epnoi.uia.learner.relations;

import org.epnoi.model.Term;

public class Relation {
	private String URI;
	private Term source;
	private Term target;
	private String provenanceSentence;
	private double relationhood;

	// ------------------------------------------------------------------------------------------------------------

	public Term getSource() {
		return source;
	}

	// ------------------------------------------------------------------------------------------------------------

	public void setSource(Term source) {
		this.source = source;
	}

	// ------------------------------------------------------------------------------------------------------------

	public String getProvenanceSentence() {
		return provenanceSentence;
	}

	// ------------------------------------------------------------------------------------------------------------

	public void setProvenanceSentence(String provenanceSentence) {
		this.provenanceSentence = provenanceSentence;
	}

	// ------------------------------------------------------------------------------------------------------------

	public Term getTarget() {
		return target;
	}

	// ------------------------------------------------------------------------------------------------------------

	public void setTarget(Term target) {
		this.target = target;
	}

	// ------------------------------------------------------------------------------------------------------------

	public double getRelationhood() {
		return relationhood;
	}

	// ------------------------------------------------------------------------------------------------------------

	public void setRelationhood(double relationhood) {
		this.relationhood = relationhood;
	}

	public String getURI() {
		return URI;
	}

	public void setURI(String uRI) {
		URI = uRI;
	}

	// ------------------------------------------------------------------------------------------------------------

}
