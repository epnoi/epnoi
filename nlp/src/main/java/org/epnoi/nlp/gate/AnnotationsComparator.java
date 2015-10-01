package org.epnoi.nlp.gate;

import gate.Annotation;

import java.util.Comparator;

public class AnnotationsComparator implements Comparator<Annotation> {

	@Override
	public int compare(final Annotation annotationA,
			final Annotation annotationB) {
		return annotationA.getStartNode().getOffset()
				.compareTo(annotationB.getStartNode().getOffset());
	}
}