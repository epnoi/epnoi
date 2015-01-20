package org.epnoi.uia.learner.relations;

import gate.Document;

public class RelationalSentence {
	
	String source;
	String target;
	String sentence;
	Document annotatedSentence;
	String type;

	// ---------------------------------------------------------------------------------------------------------------------

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public Document getAnnotatedSentence() {
		return annotatedSentence;
	}

	public void setAnnotatedSentence(Document annotatedSentence) {
		this.annotatedSentence = annotatedSentence;
	}

}
