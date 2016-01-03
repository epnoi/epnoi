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

	@Override
	public String toString() {
		String sourceSurfaceForm;
		try {
			sourceSurfaceForm= sentence.getContent().getContent(source.getStartNode().getOffset()-sentence.getAnnotation().getStartNode().getOffset(), source.getEndNode().getOffset()-sentence.getAnnotation().getStartNode().getOffset()).toString();
		}catch (Exception e){
			e.printStackTrace();
			sourceSurfaceForm=source.toString();
		}

		String targetSurfaceForm;
		try {
			targetSurfaceForm= sentence.getContent().getContent(target.getStartNode().getOffset()-sentence.getAnnotation().getStartNode().getOffset(), target.getEndNode().getOffset()-sentence.getAnnotation().getStartNode().getOffset()).toString();
		}catch (Exception e){
			e.printStackTrace();
			targetSurfaceForm=source.toString();
		}


		return "S["+sourceSurfaceForm+"] T ["+targetSurfaceForm+ "Sentence "+sentence.getContent();
	}
}
