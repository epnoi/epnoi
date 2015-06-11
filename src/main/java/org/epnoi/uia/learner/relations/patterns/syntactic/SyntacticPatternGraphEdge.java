package org.epnoi.uia.learner.relations.patterns.syntactic;

import org.jgrapht.graph.DefaultEdge;

public class SyntacticPatternGraphEdge extends DefaultEdge {
	private String kind;

	// ------------------------------------------------------------------------------------------------------------------------

	public SyntacticPatternGraphEdge(String kind) {
		this.kind = kind;
	}

	// ------------------------------------------------------------------------------------------------------------------------

	public String getKind() {
		return kind;
	}

	// ------------------------------------------------------------------------------------------------------------------------

	public void setKind(String kind) {
		this.kind = kind;
	}

	// ------------------------------------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "SyntacticPatternGraphEdge [kind=" + kind + "]";
	}

	// ------------------------------------------------------------------------------------------------------------------------

}
