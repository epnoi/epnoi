package org.epnoi.learner.relations.patterns.syntactic;

import gate.Annotation;
import gate.Document;
import org.epnoi.model.OffsetRangeSelector;
import org.epnoi.nlp.gate.NLPAnnotationsConstants;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		// Vertex-> The IDs of the annotations in the annotatedSentence
		// Edges-> The kind of grammatical dependency
		Graph<Integer, SyntacticPatternGraphEdge> dependencyGraph = createDependencyGraph(annotatedSentence);

		this.patternGraph = new SyntacticPatternGraph();

		// Then we generate the ids of the annotations of both the source and
		// target
		// Note that both source/target are defined with a offsetrange an they
		// might
		// be formed by more than one annotation
		Set<Integer> sourceIDs = _getTokensIDs(source, annotatedSentence);

		Set<Integer> targetIDs = _getTokensIDs(target, annotatedSentence);

		this.patternGraph.setSourceIDs(sourceIDs);
		this.patternGraph.setTargetIDs(targetIDs);

		// Once we have determined the IDs of the source/target of the pattern,
		// we create the graph using the dependecy graph
		// The vertices that are part of the source/target are colapsed, the
		// rest are left unaltered.
		_addVertex(dependencyGraph);

		_addEdges(dependencyGraph);

		return patternGraph;

	}

	// -----------------------------------------------------------------------------------------------------

	/**
	 * Method that adds all the vertex in the dependency graph to the pattern
	 * graph (except for those that conform the source/target)
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
				if (label == null) {
					label = "ROOT";
				}
				SyntacticPatternGraphVertex newVertex = new SyntacticPatternGraphVertex(
						vertex, label);
				this.patternGraph.addVertex(newVertex);
			}

		}
	}

	// -----------------------------------------------------------------------------------------------------

	/**
	 * Method that adds all the edges of the dependencyGraph except those that
	 * relate annotations that have been colapsed as part of either the
	 * source/target
	 * 
	 * @param dependencyGraph
	 */

	private void _addEdges(
			Graph<Integer, SyntacticPatternGraphEdge> dependencyGraph) {

		for (SyntacticPatternGraphEdge edge : dependencyGraph.edgeSet()) {

			Integer edgeSourceID = dependencyGraph.getEdgeSource(edge);
			Integer edgeTargetID = dependencyGraph.getEdgeTarget(edge);
			// We add all the edges except those that relate
			if (!_edgeJoinsSourceOrTargetIDs(edgeSourceID, edgeTargetID)) {

				this.patternGraph.addEdge(
						this.patternGraph.getVetex(edgeSourceID),
						this.patternGraph.getVetex(edgeTargetID), edge);

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

	// -----------------------------------------------------------------------------------------------------

	private String _generateLabel(Integer vertex, Document annotatedSentence) {

		Annotation annotation = annotatedSentence.getAnnotations().get(vertex);
		String surfaceForm = (String) annotation.getFeatures().get(
				NLPAnnotationsConstants.TOKEN_STRING);
		return surfaceForm;
	}

	// -----------------------------------------------------------------------------------------------------

	/**
	 * Method that given a offeset text range, returns the set of IDs of the
	 * annotations that lay in that range
	 * 
	 * @param range
	 * @param annotatedSentence
	 * @return
	 */

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
