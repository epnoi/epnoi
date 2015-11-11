package org.epnoi.learner.relations.patterns.syntactic;

import org.jgrapht.graph.SimpleGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;



public class SyntacticPatternGraph extends
		SimpleGraph<SyntacticPatternGraphVertex, SyntacticPatternGraphEdge> {
	private Set<Integer> sourceIDs;
	private Set<Integer> targetIDs;

	private SyntacticPatternGraphVertex source = new SyntacticPatternGraphVertex(
			0, "SOURCE");
	private SyntacticPatternGraphVertex target = new SyntacticPatternGraphVertex(
			0, "TARGET");

	Map<Integer, SyntacticPatternGraphVertex> vertexMap;

	// ------------------------------------------------------------------------------------------

	public SyntacticPatternGraph() {
		super(SyntacticPatternGraphEdge.class);
		this.vertexMap = new HashMap<>();
		this.sourceIDs = new HashSet<>();
		this.targetIDs = new HashSet<>();
		this.addVertex(this.source);
		this.addVertex(this.target);

	}

	// ------------------------------------------------------------------------------------------

	public void setSourceIDs(Set<Integer> sourceIDs) {
		this.sourceIDs = sourceIDs;
	}

	// ------------------------------------------------------------------------------------------

	public void setTargetIDs(Set<Integer> targetIDs) {
		this.targetIDs = targetIDs;
	}

	// ------------------------------------------------------------------------------------------

	public SyntacticPatternGraphVertex getSource() {
		return source;
	}

	// ------------------------------------------------------------------------------------------

	public SyntacticPatternGraphVertex getTarget() {
		return target;
	}

	// ------------------------------------------------------------------------------------------

	public Set<Integer> getSourceIDs() {
		return sourceIDs;
	}

	// ------------------------------------------------------------------------------------------

	public Set<Integer> getTargetIDs() {
		return targetIDs;
	}

	// ------------------------------------------------------------------------------------------

	@Override
	public boolean addVertex(SyntacticPatternGraphVertex vertex) {
		this.vertexMap.put(vertex.getId(), vertex);
		return super.addVertex(vertex);
	}

	// ------------------------------------------------------------------------------------------

	public SyntacticPatternGraphVertex getVetex(Integer id) {
		if (this.sourceIDs.contains(id)) {
			return this.source;
		} else if (this.targetIDs.contains(id)) {
			return this.target;
		}
		return this.vertexMap.get(id);
	}
}
