package org.epnoi.uia.learner.relations.patterns.lexical;

import gate.Annotation;
import gate.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.epnoi.model.OffsetRangeSelector;
import org.epnoi.uia.commons.GateUtils;
import org.epnoi.uia.learner.relations.RelationalSentence;
import org.epnoi.uia.learner.relations.patterns.RelationalPattern;
import org.epnoi.uia.learner.relations.patterns.RelationalPatternGenerator;
import org.epnoi.uia.nlp.gate.AnnotationsComparator;
import org.epnoi.uia.nlp.gate.NLPAnnotationsConstants;



public class LexicalRelationalPatternGenerator  implements RelationalPatternGenerator{
	private AnnotationsComparator annotationsComparator;

	public LexicalRelationalPatternGenerator() {
		this.annotationsComparator = new AnnotationsComparator();
	}

	public List<RelationalPattern> generate(
			RelationalSentence relationalSentence) {
		// LexicalRelationalPattern pattern = new LexicalRelationalPattern();
		// return pattern;

		// System.out.println("---> "+relationalSentence+"["+relationalSentence.getSource+"+]--->["+target+"]");
		List<RelationalPattern> generatedPatterns = new ArrayList<>();

		String serializedAnnotatedSentente = relationalSentence
				.getAnnotatedSentence();

		OffsetRangeSelector source = relationalSentence.getSource();

		OffsetRangeSelector target = relationalSentence.getTarget();

		Document annotatedSentence = GateUtils
				.deserializeGATEDocument(serializedAnnotatedSentente);

		LexicalRelationalPattern lexicalRelationalPattern = new LexicalRelationalPattern();

		List<Annotation> orderedAnnotations = new ArrayList<>();

		for (Annotation annotation : annotatedSentence.getAnnotations().get(
				NLPAnnotationsConstants.TOKEN)) {
			orderedAnnotations.add(annotation);

		}
		Collections.sort(orderedAnnotations, this.annotationsComparator);

		boolean insideWindow = false;

		int position = 0;

		for (Annotation annotation : orderedAnnotations) {

			LexicalRelationalPatternNode node = new LexicalRelationalPatternNode();

			if (annotation.getStartNode().getOffset().equals(source.getStart())
					&& annotation.getEndNode().getOffset()
							.equals(source.getEnd())) {

				insideWindow = !insideWindow;
				node.setOriginialToken(annotation.getFeatures().get("string")
						.toString());
				node.setGeneratedToken("<SOURCE>");
				lexicalRelationalPattern.getNodes().add(node);

			} else if (annotation.getStartNode().getOffset()
					.equals(target.getStart())
					&& annotation.getEndNode().getOffset()
							.equals(target.getEnd())) {
				// System.out.println("IT WAS TARGET! " +
				// annotation.getFeatures().get("string"));
				node.setOriginialToken(annotation.getFeatures().get("string")
						.toString());
				node.setGeneratedToken("<TARGET>");
				insideWindow = !insideWindow;
				//System.out.println("TARGET");
				lexicalRelationalPattern.getNodes().add(node);
			} else if (insideWindow) {
				node.setOriginialToken(annotation.getFeatures().get("string")
						.toString());
				if (_isAVerb((String) annotation.getFeatures().get("category"))) {
					node.setGeneratedToken(annotation.getFeatures()
							.get("string").toString());
				} else {
					node.setGeneratedToken(annotation.getFeatures()
							.get("category").toString());
				}

				lexicalRelationalPattern.getNodes().add(node);
			}
			position++;
		}

		generatedPatterns.add(lexicalRelationalPattern);
		return generatedPatterns;
	}

	// --------------------------------------------------------------------------------------------------------

	private boolean _isAVerb(String tag) {
		return (tag.equals("VBZ") || tag.equals("VBG") || tag.equals("VBN")
				|| tag.equals("VBP") || tag.equals("VB") || tag.equals("VBD") || tag
					.equals("MD"));
	}

	// --------------------------------------------------------------------------------------------------------

	public List<LexicalRelationalPattern> generate(Annotation source,
			Annotation target, Document document) {

		// LexicalRelationalPattern pattern = new LexicalRelationalPattern();
		// return pattern;
		List<LexicalRelationalPattern> generatedPatterns = new ArrayList<>();

		Long sourceStartOffset = source.getStartNode().getOffset();
		Long targetStartOffset = target.getStartNode().getOffset();

		Long sourceEndOffset = source.getEndNode().getOffset();
		Long targetEndOffset = target.getEndNode().getOffset();

		Long windowStartOffset;
		Long windowEndOffset;
		if (sourceEndOffset < targetStartOffset) {
			windowStartOffset = sourceStartOffset;
			windowEndOffset = targetEndOffset;
		} else {
			windowStartOffset = targetStartOffset;
			windowEndOffset = sourceEndOffset;
		}

		LexicalRelationalPattern lexicalRelationalPattern = new LexicalRelationalPattern();

		List<Annotation> orderedAnnotations = new ArrayList<>();

		for (Annotation annotation : document.getAnnotations()
				.get(NLPAnnotationsConstants.TOKEN)
				.get(windowStartOffset, windowEndOffset)) {
			orderedAnnotations.add(annotation);

		}
		Collections.sort(orderedAnnotations, this.annotationsComparator);
		/*
		 * System.out .println(
		 * "------------------------------------------------------------------------------------------------"
		 * ); System.out.println("source> " + source + " and target " + target);
		 */
		// System.out.println("orderedAnnotations> "+orderedAnnotations);

		String sourceString = source.getFeatures().get("string").toString();

		String targetString = target.getFeatures().get("string").toString();
		boolean sourceFound = false;
		boolean targetFound = false;
		
		//Evaluate pattern generation #76
		for (Annotation annotation : orderedAnnotations) {

			LexicalRelationalPatternNode node = new LexicalRelationalPatternNode();

			if ((sourceString.equals(annotation.getFeatures().get("string")
					.toString()))
					&& (annotation.getStartNode().getOffset()
							.equals(source.getStartNode().getOffset()) || annotation
							.getEndNode().getOffset()
							.equals(source.getEndNode().getOffset()))) {
				if (!sourceFound) {
					// System.out.println("IT WAS SOURCE! " +
					// annotation.getFeatures().get("string"));

					node.setOriginialToken(sourceString);
					node.setGeneratedToken("<SOURCE>");
					//System.out.println("SOURCE");
					lexicalRelationalPattern.getNodes().add(node);
					sourceFound = true;
				}

			} else if ((targetString.equals(annotation.getFeatures()
					.get("string").toString()))
					&& (annotation.getStartNode().getOffset()
							.equals(target.getStartNode().getOffset()) || annotation
							.getEndNode().getOffset()
							.equals(target.getEndNode().getOffset()))) {
				// System.out.println("IT WAS TARGET! "+
				// annotation.getFeatures().get("string"));
				if (!targetFound) {
					node.setOriginialToken(targetString);
					node.setGeneratedToken("<TARGET>");
					//System.out.println("TARGET");
					targetFound = true;

					lexicalRelationalPattern.getNodes().add(node);
				}
			} else {
				if (!(targetFound && sourceFound)
						&& (targetFound || sourceFound)) {
					node.setOriginialToken(annotation.getFeatures()
							.get("string").toString());
					if (_isAVerb((String) annotation.getFeatures().get(
							"category"))) {
						node.setGeneratedToken(annotation.getFeatures()
								.get("string").toString());
					} else {
						node.setGeneratedToken(annotation.getFeatures()
								.get("category").toString());
					}

					lexicalRelationalPattern.getNodes().add(node);
				}
			}
		}
		generatedPatterns.add(lexicalRelationalPattern);
		return generatedPatterns;
	}

	// --------------------------------------------------------------------------------------------------------

	

}
