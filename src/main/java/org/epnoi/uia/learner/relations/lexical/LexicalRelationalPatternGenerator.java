package org.epnoi.uia.learner.relations.lexical;

import gate.Annotation;
import gate.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.epnoi.model.OffsetRangeSelector;
import org.epnoi.uia.commons.GateUtils;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.exceptions.EpnoiInitializationException;
import org.epnoi.uia.learner.nlp.gate.NLPAnnotationsHelper;
import org.epnoi.uia.learner.relations.RelationalSentence;

public class LexicalRelationalPatternGenerator {
	private AnnotationsComparator annotationsComparator;

	public LexicalRelationalPatternGenerator() {
		this.annotationsComparator = new AnnotationsComparator();
	}

	public List<LexicalRelationalPattern> generate(
			RelationalSentence relationalSentence) {
		// LexicalRelationalPattern pattern = new LexicalRelationalPattern();
		// return pattern;
		List<LexicalRelationalPattern> generatedPatterns = new ArrayList<>();

		String serializedAnnotatedSentente = relationalSentence
				.getAnnotatedSentence();

		OffsetRangeSelector source = relationalSentence.getSource();

		OffsetRangeSelector target = relationalSentence.getTarget();

		Document annotatedSentence = GateUtils
				.deserializeGATEDocument(serializedAnnotatedSentente);
		System.out.println(annotatedSentence.getAnnotationSetNames());

		LexicalRelationalPattern lexicalRelationalPattern = new LexicalRelationalPattern();

		List<Annotation> orderedAnnotations = new ArrayList<>();

		for (Annotation annotation : annotatedSentence.getAnnotations().get(
				NLPAnnotationsHelper.TOKEN)) {
			orderedAnnotations.add(annotation);

		}
		Collections.sort(orderedAnnotations, this.annotationsComparator);

		boolean insideWindow = false;
		for (Annotation annotation : orderedAnnotations) {

			LexicalRelationalPatternNode node = new LexicalRelationalPatternNode();

			/*
			 * System.out.println("token> " + annotation.getFeatures() + "[" +
			 * annotation.getStartNode() + "," + annotation.getEndNode() + "]");
			 */

			if (annotation.getStartNode().getOffset().equals(source.getStart())
					&& annotation.getEndNode().getOffset()
							.equals(source.getEnd())) {
				System.out.println("IT WAS SOURCE! "
						+ annotation.getFeatures().get("string"));
				insideWindow = !insideWindow;
				node.setOriginialToken(annotation.getFeatures().get("string")
						.toString());
				node.setGeneratedToken("<SOURCE>");
				lexicalRelationalPattern.getNodes().add(node);

			} else if (annotation.getStartNode().getOffset()
					.equals(target.getStart())
					&& annotation.getEndNode().getOffset()
							.equals(target.getEnd())) {
				System.out.println("IT WAS TARGET! "
						+ annotation.getFeatures().get("string"));
				node.setOriginialToken(annotation.getFeatures().get("string")
						.toString());
				node.setGeneratedToken("<TARGET>");
				insideWindow = !insideWindow;
				lexicalRelationalPattern.getNodes().add(node);
			} else if (insideWindow) {
				node.setOriginialToken(annotation.getFeatures().get("string")
						.toString());
				if (annotation.getFeatures().get("category")
						.equals("VBZ")) {
					node.setGeneratedToken(annotation.getFeatures()
							.get("string").toString());
				} else {
					node.setGeneratedToken(annotation.getFeatures()
							.get("category").toString());
				}

				lexicalRelationalPattern.getNodes().add(node);
			}
		}

		generatedPatterns.add(lexicalRelationalPattern);
		return generatedPatterns;
	}

	// --------------------------------------------------------------------------------------------------------

	public void init(Core core) throws EpnoiInitializationException {

	}

	class AnnotationsComparator implements Comparator<Annotation> {

		@Override
		public int compare(final Annotation annotationA,
				final Annotation annotationB) {
			return annotationA.getStartNode().getOffset()
					.compareTo(annotationB.getStartNode().getOffset());
		}
	}

}
