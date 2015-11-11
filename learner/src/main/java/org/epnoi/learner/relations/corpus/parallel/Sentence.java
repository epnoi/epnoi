package org.epnoi.learner.relations.corpus.parallel;

import gate.Annotation;
import gate.AnnotationSet;
import gate.DocumentContent;

import java.io.Serializable;

public class Sentence implements Serializable {

	private static final long serialVersionUID = 6008687448023920270L;
	private DocumentContent content;
	private Annotation annotation;
	private AnnotationSet containedAnnotations;

	//-------------------------------------------------------------------------------------
	
	public Sentence(DocumentContent content, Annotation annotation, AnnotationSet containedAnnotations) {
		super();
		this.content = content;
		this.annotation = annotation;
		this.containedAnnotations = containedAnnotations;
	}

	//-------------------------------------------------------------------------------------
	
	public AnnotationSet getContainedAnnotations() {
		return containedAnnotations;
	}

	//-------------------------------------------------------------------------------------
	
	public void setContainedAnnotations(AnnotationSet containedAnnotations) {
		this.containedAnnotations = containedAnnotations;
	}

	//-------------------------------------------------------------------------------------
	
	public Annotation getAnnotation() {
		return annotation;
	}
	
	//-------------------------------------------------------------------------------------

	public void setAnnotation(Annotation annotation) {
		this.annotation = annotation;
	}

	public DocumentContent getContent() {
		return content;
	}

	//-------------------------------------------------------------------------------------
	
	public void setContent(DocumentContent content) {
		this.content = content;
	}
	
	//-------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "Sentence [content=" + content + ", annotation=" + annotation + ", containedAnnotations="
				+ containedAnnotations.size() + "]";
	}
}
