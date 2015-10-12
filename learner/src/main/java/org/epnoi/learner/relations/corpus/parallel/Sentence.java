package org.epnoi.learner.relations.corpus.parallel;

import gate.Annotation;
import gate.AnnotationSet;
import gate.DocumentContent;

public class Sentence {


	private DocumentContent content;
	private Annotation annotation;
	private AnnotationSet containedAnnotations;
	
	public Sentence(DocumentContent content, Annotation annotation, AnnotationSet containedAnnotations) {
		super();
		this.content = content;
		this.annotation = annotation;
		this.containedAnnotations = containedAnnotations;
	}
	
	
	public AnnotationSet getContainedAnnotations() {
		return containedAnnotations;
	}
	public void setContainedAnnotations(AnnotationSet containedAnnotations) {
		this.containedAnnotations = containedAnnotations;
	}
	public Annotation getAnnotation() {
		return annotation;
	}
	public void setAnnotation(Annotation annotation) {
		this.annotation = annotation;
	}
	public DocumentContent getContent() {
		return content;
	}
	public void setContent(DocumentContent content) {
		this.content = content;
	}


	

}
