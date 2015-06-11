package org.epnoi.uia.learner.relations.patterns.syntactic;

import gate.Annotation;
import gate.Document;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.epnoi.model.OffsetRangeSelector;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.uia.commons.GateUtils;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.learner.nlp.gate.NLPAnnotationsConstants;
import org.epnoi.uia.learner.relations.RelationalSentence;
import org.epnoi.uia.learner.relations.corpus.MockUpRelationalSentencesCorpusCreator;
import org.epnoi.uia.learner.relations.patterns.RelationalPattern;
import org.epnoi.uia.learner.relations.patterns.RelationalPatternGenerator;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;

import scala.collection.mutable.HashMap;

public class SyntacticRelationalPatternGenerator implements
		RelationalPatternGenerator {

	private Comparator<? super Annotation> annotationsComparator;

	// -----------------------------------------------------------------------------------------------------

	public SyntacticRelationalPatternGenerator() {

	}

	// -----------------------------------------------------------------------------------------------------

	@Override
	public List<RelationalPattern> generate(RelationalSentence sentence) {
		System.out.println("---> " + sentence);

		List<RelationalPattern> generatedPatterns = new ArrayList<>();

		String serializedAnnotatedSentente = sentence.getAnnotatedSentence();

		OffsetRangeSelector source = sentence.getSource();

		OffsetRangeSelector target = sentence.getTarget();

		Document annotatedSentence = GateUtils
				.deserializeGATEDocument(serializedAnnotatedSentente);

		SyntacticPatternGraphBuilder patternGraphBuilder = new SyntacticPatternGraphBuilder();

		Graph<SyntacticPatternGraphVertex, SyntacticPatternGraphEdge> patternGraph = patternGraphBuilder
				.build(source, target, annotatedSentence);

		System.out.println("-------> " + patternGraph);

		SyntacticRelationalPattern syntacticRelationalPattern = new SyntacticRelationalPattern();

		/*
		 * List<Annotation> orderedAnnotations = new ArrayList<>();
		 * 
		 * for (Annotation annotation : annotatedSentence.getAnnotations().get(
		 * NLPAnnotationsConstants.TOKEN)) { orderedAnnotations.add(annotation);
		 * 
		 * } Collections.sort(orderedAnnotations, this.annotationsComparator);
		 */
		generatedPatterns.add(syntacticRelationalPattern);
		return generatedPatterns;
	}

	// -----------------------------------------------------------------------------------------------------

	public static void main(String[] args) throws EpnoiInitializationException {
		Core core = CoreUtility.getUIACore();
		SyntacticRelationalPatternGenerator patternGenerator = new SyntacticRelationalPatternGenerator();

		MockUpRelationalSentencesCorpusCreator corpusCreator = new MockUpRelationalSentencesCorpusCreator();
		corpusCreator.init(core);

		for (RelationalSentence sentence : corpusCreator.createTestCorpus()
				.getSentences()) {
			patternGenerator.generate(sentence);
		}

	}
}
