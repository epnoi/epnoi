package org.epnoi.learner.automata;

public class State {
	private String name;
	private boolean isFinal;
	private boolean isInitial;

	// ---------------------------------------------------------------------------------

	public State(String name, boolean isFinal, boolean isInitial) {
		this.name=name;
		this.isFinal = isFinal;
		this.isInitial = isInitial;
	}

	// ---------------------------------------------------------------------------------

	public String getName() {
		return name;
	}

	// ---------------------------------------------------------------------------------

	public void setName(String name) {
		this.name = name;
	}

	// ---------------------------------------------------------------------------------

	public boolean isFinal() {
		return this.isFinal;
	}

	// ---------------------------------------------------------------------------------

	public boolean isInitial() {
		return this.isInitial;
	}
	
	// ---------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "State [name=" + name + ", isFinal=" + isFinal + ", isInitial="
				+ isInitial + "]";
	}
	
	// ---------------------------------------------------------------------------------
	
	

	
}
