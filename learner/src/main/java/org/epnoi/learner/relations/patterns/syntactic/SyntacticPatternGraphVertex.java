package org.epnoi.learner.relations.patterns.syntactic;

public class SyntacticPatternGraphVertex implements
		SyntacticPatternGraphElement {
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
	public boolean equals(Object other) {
		if (other instanceof SyntacticPatternGraphVertex) {
			SyntacticPatternGraphVertex otherVertex = (SyntacticPatternGraphVertex) other;
			return (this.label.equals(otherVertex.getLabel()));
		}
		return false;
	}

	// ------------------------------------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "V [id=" + id + ", label=" + label + "]";
	}

	// ------------------------------------------------------------------------------------------------------------------------

}
