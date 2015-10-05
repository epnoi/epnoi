package org.epnoi.learner.relations.patterns.syntactic;

import org.jgrapht.graph.DefaultEdge;

public class SyntacticPatternGraphEdge extends DefaultEdge implements
		SyntacticPatternGraphElement {
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
	public boolean equals(Object other) {
		if (other instanceof SyntacticPatternGraphEdge) {
			SyntacticPatternGraphEdge otherEdge = (SyntacticPatternGraphEdge) other;
			return (this.kind.equals(otherEdge.getKind()));
		}
		return false;
	}

	// ------------------------------------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "E [kind=" + kind + "]";
	}

	// ------------------------------------------------------------------------------------------------------------------------

}
