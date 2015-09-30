package org.epnoi.model;

public class AnnotatedWord<T extends Comparable<T>> implements Comparable<AnnotatedWord<T>> {
	private String word;
	private T annotation;

	// -------------------------------------------------------------------------------------------------------

	
	public AnnotatedWord(T annotation) {
		this.annotation= annotation;
	}
	
	public String getWord() {
		return word;
	}

	// -------------------------------------------------------------------------------------------------------

	public void setWord(String word) {
		this.word = word;
	}

	// -------------------------------------------------------------------------------------------------------

	public T getAnnotation() {
		return annotation;
	}

	// -------------------------------------------------------------------------------------------------------

	public void setAnnotation(T annotation) {
		this.annotation = annotation;
	}

	// -------------------------------------------------------------------------------------------------------

	public int compareTo(AnnotatedWord<T> annotation) {
		return this.getAnnotation().compareTo(annotation.getAnnotation());
	}

	@Override
	public String toString() {
		return "AnnotatedWord [word=" + word + ", annotation=" + annotation
				+ "]";
	}
}
