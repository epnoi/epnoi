package org.epnoi.uia.learner.relations.patterns.syntactic;

import org.jgrapht.graph.DefaultEdge;

public class SyntacticPatternGraphVertex {
	private Integer id;
	private String label;

	// ------------------------------------------------------------------------------------------------------------------------

	public SyntacticPatternGraphVertex(Integer id, String label) {
		super();
		this.id = id;
		this.label = label;
	}

	// ------------------------------------------------------------------------------------------------------------------------

	public Integer getId() {
		return id;
	}

	// ------------------------------------------------------------------------------------------------------------------------

	public void setId(Integer id) {
		this.id = id;
	}

	// ------------------------------------------------------------------------------------------------------------------------

	public String getLabel() {
		return label;
	}

	// ------------------------------------------------------------------------------------------------------------------------

	public void setLabel(String label) {
		this.label = label;
	}

	// ------------------------------------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "SyntacticPatternGraphVertex [id=" + id + ", label=" + label
				+ "]";
	}

	// ------------------------------------------------------------------------------------------------------------------------

}
