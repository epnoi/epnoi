package org.epnoi.learner.relations.corpus.parallel;

import gate.Annotation;

public class RelationalSentenceCandidate {
	Sentence sentence;
	Annotation source;
	Annotation target;

	public RelationalSentenceCandidate(Sentence sentence, Annotation source, Annotation target) {
		super();
		this.sentence = sentence;
		this.source = source;
		this.target = target;
	}

	public Sentence getSentence() {
		return sentence;
	}

	public void setSentence(Sentence sentence) {
		this.sentence = sentence;
	}

	public Annotation getSource() {
		return source;
	}

	public void setSource(Annotation source) {
		this.source = source;
	}

	public Annotation getTarget() {
		return target;
	}

	public void setTarget(Annotation target) {
		this.target = target;
	}

}
