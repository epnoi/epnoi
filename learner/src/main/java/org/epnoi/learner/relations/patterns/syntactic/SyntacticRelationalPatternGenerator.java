package org.epnoi.learner.relations.patterns.syntactic;

import gate.Annotation;
import gate.Document;
import org.epnoi.learner.relations.corpus.MockUpRelationalSentencesCorpusCreator;
import org.epnoi.learner.relations.patterns.RelationalPattern;
import org.epnoi.learner.relations.patterns.RelationalPatternGenerator;
import org.epnoi.model.OffsetRangeSelector;
import org.epnoi.model.RelationalSentence;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.Core;
import org.epnoi.uia.commons.GateUtils;
import org.epnoi.uia.core.CoreUtility;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

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

		SyntacticPatternGraph patternGraph = patternGraphBuilder.build(source,
				target, annotatedSentence);

		//SyntacticRelationalPattern syntacticRelationalPattern = new SyntacticRelationalPattern();

		DijkstraShortestPath shortestPathGenerator = new DijkstraShortestPath(
				patternGraph, patternGraph.getSource(),
				patternGraph.getTarget());
		// System.out.println("SP---> " + shortestPathGenerator.getPath());

		//TODO Fix compilation error:
		//ERROR] Failed to _execute goal org.apache.maven.plugins:maven-compiler-plugin:3.3:compile (default-compile) on project learner: Compilation failure
		//[ERROR] /Users/cbadenes/Projects/epnoi/learner/src/main/java/org/epnoi/learner/relations/patterns/syntactic/SyntacticRelationalPatternGenerator.java:[64,86] incompatible types: java.lang.Object cannot be converted to java.util.List<org.epnoi.learner.relations.patterns.syntactic.SyntacticPatternGraphElement>
		//[ERROR] -> [Help 1]
//		for (List<SyntacticPatternGraphElement> list : createPatternNodelists(
//				shortestPathGenerator.getPath(), patternGraph)) {
//			SyntacticRelationalPattern pattern = new SyntacticRelationalPattern(
//					list);
//			generatedPatterns.add(pattern);
//		}
				
		return generatedPatterns;
	}

	// -----------------------------------------------------------------------------------------------------

	private List<SyntacticPatternGraphElement> createPatternCoreNodesList(
			GraphPath<SyntacticPatternGraphVertex, SyntacticPatternGraphEdge> shortestGraphpath,
			SyntacticPatternGraph patternGraph) {
		List<SyntacticPatternGraphElement> nodes = new ArrayList<>();
		List<SyntacticPatternGraphVertex> vertexList = new ArrayList<>();
		vertexList.addAll(Graphs.getPathVertexList(shortestGraphpath));

		Iterator<SyntacticPatternGraphVertex> vertexIt = vertexList.iterator();

		int i = 0;

		while (i < vertexList.size() - 1) {

			nodes.add(vertexList.get(i));
			SyntacticPatternGraphEdge edge = patternGraph.getEdge(
					vertexList.get(i), vertexList.get(i + 1));

			nodes.add(edge);

			nodes.add(vertexList.get(i + 1));

			i++;

		}

		/*
		 * Iterator<SyntacticPatternGraphEdge> edgesIterator = shortestGraphpath
		 * .getEdgeList().iterator(); while (edgesIterator.hasNext()) {
		 * SyntacticPatternGraphEdge edge = edgesIterator.next();
		 * SyntacticPatternGraphVertex sourceVertex = patternGraph
		 * .getEdgeSource(edge);
		 * 
		 * System.out.println("source> "+sourceVertex); nodes.add(sourceVertex);
		 * nodes.add(edge); if (!edgesIterator.hasNext()) {
		 * SyntacticPatternGraphVertex targetVertex = patternGraph
		 * .getEdgeTarget(edge); nodes.add(targetVertex); }
		 * 
		 * }
		 */

		return nodes;

	}

	private List<List<SyntacticPatternGraphElement>> createPatternNodelists(
			GraphPath<SyntacticPatternGraphVertex, SyntacticPatternGraphEdge> shortestGraphpath,
			SyntacticPatternGraph patternGraph) {

		List<List<SyntacticPatternGraphElement>> nodesLists = new ArrayList<>();
		List<SyntacticPatternGraphElement> coreNodeList = createPatternCoreNodesList(
				shortestGraphpath, patternGraph);
		System.out.println("---------------> " + coreNodeList);

		SyntacticPatternGraphVertex source = patternGraph.getSource();
		SyntacticPatternGraphVertex target = patternGraph.getTarget();

		for (SyntacticPatternGraphVertex sourceNeighbour : Graphs
				.neighborListOf(patternGraph, patternGraph.getSource())) {
			if (!coreNodeList.contains(sourceNeighbour)) {

				for (SyntacticPatternGraphVertex targetNeighbour : Graphs
						.neighborListOf(patternGraph, patternGraph.getTarget())) {

					if (!coreNodeList.contains(targetNeighbour)) {
						List<SyntacticPatternGraphElement> nodeList = new ArrayList<>();
						nodeList.addAll(coreNodeList);

						SyntacticPatternGraphEdge edge = patternGraph.getEdge(
								sourceNeighbour, source);
						nodeList.add(0, edge);
						nodeList.add(0, sourceNeighbour);

						SyntacticPatternGraphEdge edge2 = patternGraph.getEdge(
								target, targetNeighbour);
						nodeList.add(edge2);
						nodeList.add(targetNeighbour);

						nodesLists.add(nodeList);
					}
				}
			}
		}
		return nodesLists;
	}

	// -----------------------------------------------------------------------------------------------------

	public static void main(String[] args) throws EpnoiInitializationException {
		Core core = CoreUtility.getUIACore();
		SyntacticRelationalPatternGenerator patternGenerator = new SyntacticRelationalPatternGenerator();

		MockUpRelationalSentencesCorpusCreator corpusCreator = new MockUpRelationalSentencesCorpusCreator();
		corpusCreator.init(core);

		for (RelationalSentence sentence : corpusCreator.createTestCorpus()
				.getSentences()) {
			List<RelationalPattern> patterns = patternGenerator
					.generate(sentence);
			for (RelationalPattern pattern : patterns) {
				System.out.println("Patter:> " + pattern);
			}
		}

	}
}
