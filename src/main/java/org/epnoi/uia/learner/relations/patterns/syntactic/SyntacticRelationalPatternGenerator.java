package org.epnoi.uia.learner.relations.patterns.syntactic;

import gate.Annotation;
import gate.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.epnoi.model.OffsetRangeSelector;
import org.epnoi.uia.commons.GateUtils;
import org.epnoi.uia.learner.nlp.gate.NLPAnnotationsHelper;
import org.epnoi.uia.learner.relations.RelationalSentence;
import org.epnoi.uia.learner.relations.patterns.RelationalPattern;
import org.epnoi.uia.learner.relations.patterns.RelationalPatternGenerator;
import org.epnoi.uia.learner.relations.patterns.lexical.LexicalRelationalPattern;
import org.epnoi.uia.learner.relations.patterns.lexical.LexicalRelationalPatternNode;

public class SyntacticRelationalPatternGenerator implements
		RelationalPatternGenerator {

	private Comparator<? super Annotation> annotationsComparator;

	public SyntacticRelationalPatternGenerator() {

	}

	@Override
	public List<RelationalPattern> generate(RelationalSentence sentence) {
		System.out.println("---> " + sentence);

		List<RelationalPattern> generatedPatterns = new ArrayList<>();

		String serializedAnnotatedSentente = sentence.getAnnotatedSentence();

		OffsetRangeSelector source = sentence.getSource();

		OffsetRangeSelector target = sentence.getTarget();

		Document annotatedSentence = GateUtils
				.deserializeGATEDocument(serializedAnnotatedSentente);

		SyntacticRelationalPattern syntacticRelationalPattern = new SyntacticRelationalPattern();

		List<Annotation> orderedAnnotations = new ArrayList<>();

		for (Annotation annotation : annotatedSentence.getAnnotations().get(
				NLPAnnotationsHelper.TOKEN)) {
			orderedAnnotations.add(annotation);

		}
		Collections.sort(orderedAnnotations, this.annotationsComparator);

		/*
		 * boolean insideWindow = false;
		 * 
		 * int position = 0;
		 * 
		 * for (Annotation annotation : orderedAnnotations) {
		 * 
		 * LexicalRelationalPatternNode node = new
		 * LexicalRelationalPatternNode();
		 * 
		 * if (annotation.getStartNode().getOffset().equals(source.getStart())
		 * && annotation.getEndNode().getOffset() .equals(source.getEnd())) {
		 * 
		 * insideWindow = !insideWindow;
		 * node.setOriginialToken(annotation.getFeatures().get("string")
		 * .toString()); node.setGeneratedToken("<SOURCE>");
		 * lexicalRelationalPattern.getNodes().add(node);
		 * 
		 * } else if (annotation.getStartNode().getOffset()
		 * .equals(target.getStart()) && annotation.getEndNode().getOffset()
		 * .equals(target.getEnd())) { // System.out.println("IT WAS TARGET! " +
		 * // annotation.getFeatures().get("string"));
		 * node.setOriginialToken(annotation.getFeatures().get("string")
		 * .toString()); node.setGeneratedToken("<TARGET>"); insideWindow =
		 * !insideWindow; //System.out.println("TARGET");
		 * lexicalRelationalPattern.getNodes().add(node); } else if
		 * (insideWindow) {
		 * node.setOriginialToken(annotation.getFeatures().get("string")
		 * .toString()); if (_isAVerb((String)
		 * annotation.getFeatures().get("category"))) {
		 * node.setGeneratedToken(annotation.getFeatures()
		 * .get("string").toString()); } else {
		 * node.setGeneratedToken(annotation.getFeatures()
		 * .get("category").toString()); }
		 * 
		 * lexicalRelationalPattern.getNodes().add(node); } position++; }
		 * 
		 * generatedPatterns.add(lexicalRelationalPattern);
		 */
		generatedPatterns.add(new SyntacticRelationalPattern());
		return generatedPatterns;
	}

}
