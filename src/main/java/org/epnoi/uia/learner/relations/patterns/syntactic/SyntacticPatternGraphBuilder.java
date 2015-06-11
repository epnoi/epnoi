package org.epnoi.uia.learner.relations.patterns.syntactic;

import gate.Annotation;
import gate.Document;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.epnoi.model.OffsetRangeSelector;
import org.epnoi.uia.learner.nlp.gate.NLPAnnotationsConstants;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;

public class SyntacticPatternGraphBuilder {
	// -----------------------------------------------------------------------------------------------------

	private SyntacticPatternGraph patternGraph;
	private Document annotatedSentence;
	
	
	// -----------------------------------------------------------------------------------------------------

	 	
	

	// -----------------------------------------------------------------------------------------------------

	public SyntacticPatternGraph build(OffsetRangeSelector source,
			OffsetRangeSelector target, Document annotatedSentence) {
		
		this.annotatedSentence = annotatedSentence;

		// First of all we create the dependency graph for the sentence
		Graph<Integer, SyntacticPatternGraphEdge> dependencyGraph = createDependencyGraph(annotatedSentence);

		this.patternGraph = new SyntacticPatternGraph();

		Set<Integer> sourceIDs = _getTokensIDs(source, annotatedSentence);

		Set<Integer> targetIDs = _getTokensIDs(target, annotatedSentence);

		this.patternGraph.setSourceIDs(sourceIDs);
		this.patternGraph.setTargetIDs(targetIDs);

		_addVertex(dependencyGraph);

		_addEdges(dependencyGraph);

		return patternGraph;

	}

	// -----------------------------------------------------------------------------------------------------

	/**
	 * Method that adds all the vertex in the dependency graph to the pattern
	 * graph (except for those that conform the source/target
	 * 
	 * @param dependencyGraph
	 */

	private void _addVertex(
			Graph<Integer, SyntacticPatternGraphEdge> dependencyGraph) {

		for (Integer vertex : dependencyGraph.vertexSet()) {
			if (!(this.patternGraph.getSourceIDs().contains(vertex) && this.patternGraph
					.getTargetIDs().contains(vertex))) {
				// If it is not a source/target vertex, we add the vertex. The
				// id is the same that in the dependency graph

				String label = _generateLabel(vertex, annotatedSentence);
				SyntacticPatternGraphVertex newVertex = new SyntacticPatternGraphVertex(
						vertex, label);
				this.patternGraph.addVertex(newVertex);
			}

		}
	}

	// -----------------------------------------------------------------------------------------------------

	private void _addEdges(
			Graph<Integer, SyntacticPatternGraphEdge> dependencyGraph) {

		for (SyntacticPatternGraphEdge edge : dependencyGraph.edgeSet()) {

			Integer edgeSourceID = dependencyGraph.getEdgeSource(edge);
			Integer edgeTargetID = dependencyGraph.getEdgeTarget(edge);
			// We add all the edges except those that relate
			if (!_edgeJoinsSourceOrTargetIDs(edgeSourceID, edgeTargetID)) {

				System.out.println("Adding it! "+edgeSourceID+ "  >> "+edgeTargetID);
				this.patternGraph.addEdge(
						this.patternGraph.getVetex(edgeSourceID),
						this.patternGraph.getVetex(edgeTargetID), edge);

			}else{
				System.out.println("Not adding it :( "+edgeSourceID+ "  >> "+edgeTargetID);
			}
				

		}
	}

	// -----------------------------------------------------------------------------------------------------

	private boolean _edgeJoinsSourceOrTargetIDs(Integer edgeSourceID,
			Integer edgeTargetID) {
		return (this.patternGraph.getSourceIDs().contains(edgeSourceID) && this.patternGraph
				.getSourceIDs().contains(edgeTargetID))
				|| (this.patternGraph.getTargetIDs().contains(edgeSourceID) && this.patternGraph
						.getTargetIDs().contains(edgeTargetID));
	}

	private String _generateLabel(Integer vertex, Document annotatedSentence) {
		System.out.println(">> "+annotatedSentence);
		Annotation annotation = annotatedSentence.getAnnotations().get(vertex);
		String surfaceForm = (String) annotation.getFeatures().get("string");
		return surfaceForm;
	}

	// -----------------------------------------------------------------------------------------------------

	private Set<Integer> _getTokensIDs(OffsetRangeSelector range,
			Document annotatedSentence) {
		// System.out.println("-----> " + annotatedSentence.toXml());
		Set<Integer> ids = new HashSet<>();
		for (Annotation annotation : annotatedSentence.getAnnotations()
				.get(NLPAnnotationsConstants.TOKEN)
				.get(range.getStart(), range.getEnd())) {
			ids.add(annotation.getId());
		}
		return ids;
	}

	// -----------------------------------------------------------------------------------------------------

	public static Graph<Integer, SyntacticPatternGraphEdge> createDependencyGraph(
			Document annotatedSentence) {
		Graph<Integer, SyntacticPatternGraphEdge> dependencyGraph = new SimpleGraph<Integer, SyntacticPatternGraphEdge>(
				SyntacticPatternGraphEdge.class);

		for (Annotation dependencyAnnotation : annotatedSentence
				.getAnnotations().get(NLPAnnotationsConstants.DEPENDENCY)) {

			_updataPatternGraph(dependencyGraph, dependencyAnnotation);

		}
		return dependencyGraph;
	}

	// -----------------------------------------------------------------------------------------------------

	private static void _updataPatternGraph(
			Graph<Integer, SyntacticPatternGraphEdge> patternGraph,
			Annotation dependencyAnnotation) {
		List<Integer> ids = (List<Integer>) dependencyAnnotation.getFeatures()
				.get(NLPAnnotationsConstants.DEPENDENCY_ARGS);

		Integer sourceID = ids.get(0);
		Integer targetID = ids.get(1);

		String kind = (String) dependencyAnnotation.getFeatures().get(
				NLPAnnotationsConstants.DEPENDENCY_KIND);

		if (sourceID != null && targetID != null) {

			patternGraph.addVertex(sourceID);
			patternGraph.addVertex(targetID);
			patternGraph.addEdge(sourceID, targetID,
					new SyntacticPatternGraphEdge(kind));
		} else {
			System.out.println("Source > " + sourceID + " > " + "Target > "
					+ targetID);
		}
	}
}
