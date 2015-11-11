package org.epnoi.learner.relations.patterns.syntactic;

import org.epnoi.learner.relations.patterns.PatternsConstants;
import org.epnoi.learner.relations.patterns.RelationalPattern;

import java.util.List;

public class SyntacticRelationalPattern implements RelationalPattern {

	List<SyntacticPatternGraphElement> nodes;
	private int sourcePosition;
	private int targetPosition;
	private int corePathLength;

	// -------------------------------------------------------------------------------------------

	public SyntacticRelationalPattern(List<SyntacticPatternGraphElement> list) {

		this.nodes = list;
		boolean sourceFound = false;
		boolean targetFound = false;

		int i = 0;
		while (!sourceFound) {
			if (PatternsConstants.SOURCE
					.equals(((SyntacticPatternGraphVertex) nodes.get(i))
							.getLabel())) {
				sourcePosition = i;
				sourceFound = true;
			}
			i = i + 2;

		}
		while (!targetFound) {
			if (PatternsConstants.TARGET
					.equals(((SyntacticPatternGraphVertex) nodes.get(i))
							.getLabel())) {
				targetPosition = i;
				targetFound = true;
			}
			i = i + 2;

		}
		this.corePathLength = this.targetPosition - this.sourcePosition;
		// System.out.println(":::::::> " + this.toString());
	}

	// -------------------------------------------------------------------------------------------
	/**
	 * Method that returns the length of the core of the syntactic pattern (the
	 * shortest dependency path between the source and the target)
	 * 
	 * @return
	 */
	public Integer getCoreLength() {
		return this.targetPosition - this.sourcePosition;
	}

	// -------------------------------------------------------------------------------------------

	// -------------------------------------------------------------------------------------------
	/**
	 * Method that returns the length of the syntactic pattern
	 * 
	 * @return
	 */
	@Override
	public Integer getLength() {
		return this.nodes.size();
	}

	// -------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "SyntacticRelationalPattern [nodes=" + nodes
				+ ", sourcePosition=" + sourcePosition + ", targetPosition="
				+ targetPosition + ", corePathLength=" + corePathLength + "]";
	}

	// -------------------------------------------------------------------------------------------

	public boolean matches(SyntacticRelationalPattern otherPattern) {
		if (this.getCoreLength() == otherPattern.getCoreLength()) {

			boolean matches = _matchesCore(otherPattern)
					&& _matchesLeftWindow(otherPattern)
					&& _matchesRightWindow(otherPattern);
			return matches;
		} else {
			return false;
		}
	}

	// -------------------------------------------------------------------------------------------

	private boolean _matchesRightWindow(SyntacticRelationalPattern otherPattern) {
		boolean matches = true;
		int i = targetPosition;
		while (matches && i < this.getLength()) {
			matches = matches
					&& otherPattern.getNode(i).equals(this.nodes.get(i));
			i++;
		}

		return matches;
	}

	// -------------------------------------------------------------------------------------------

	private boolean _matchesLeftWindow(SyntacticRelationalPattern otherPattern) {
		boolean matches = true;
		int i = this.sourcePosition;
		while (matches && i > 0) {
			matches = matches
					&& otherPattern.getNode(i).equals(this.nodes.get(i));
			i--;
		}

		return matches;
	}

	// -------------------------------------------------------------------------------------------

	private boolean _matchesCore(SyntacticRelationalPattern otherPattern) {
		int otherPatternSourcePosition = otherPattern.getSourcePosition();

		int i = 0;
		boolean matches = true;

		while (matches && (i < this.corePathLength)) {
			i++;
			matches = matches
					&& otherPattern.getNode(otherPatternSourcePosition + i)
							.equals(this.nodes.get(this.sourcePosition + i));
		}
		return matches;
	}

	// -------------------------------------------------------------------------------------------

	// -------------------------------------------------------------------------------------------
	@Override
	public boolean equals(Object other) {

		if (other instanceof SyntacticRelationalPattern) {
			
			SyntacticRelationalPattern otherPattern = (SyntacticRelationalPattern) other;
			if (this.getLength() == otherPattern.getLength()) {

				boolean matches = _equalsCore(otherPattern)
						&& _equalsLeftWindow(otherPattern)
						&& _equalsRightWindow(otherPattern);
				
				return matches;
			}
		}
		return false;
	}

	// -------------------------------------------------------------------------------------------

	private boolean _equalsRightWindow(SyntacticRelationalPattern otherPattern) {

		if (this.targetPosition == otherPattern.getTargetPosition()) {

			boolean matches = true;
			int i = targetPosition;
			while (matches && i < this.getLength()) {
				matches = matches
						&& otherPattern.getNode(i).equals(this.nodes.get(i));
				i++;
			}
			return matches;
		}
		return false;
	}

	// -------------------------------------------------------------------------------------------

	private boolean _equalsLeftWindow(SyntacticRelationalPattern otherPattern) {

		if (this.sourcePosition == otherPattern.getSourcePosition()) {
			boolean matches = true;
			int i = this.sourcePosition;
			while (matches && i > 0) {
				matches = matches
						&& otherPattern.getNode(i).equals(this.nodes.get(i));
				i--;
			}
			return matches;
		}
		return false;
	}

	// -------------------------------------------------------------------------------------------

	private boolean _equalsCore(SyntacticRelationalPattern otherPattern) {
		int otherPatternSourcePosition = otherPattern.getSourcePosition();

		int i = this.sourcePosition;
		boolean matches = true;
		if (this.getCoreLength() == otherPattern.getCoreLength()) {
		
			while (matches && (i <= this.targetPosition)) {
				i++;
				matches = matches
						&& otherPattern.getNode(i).equals(this.nodes.get(i));
			}
			
			return matches;
		}
		return false;
	}

	// -------------------------------------------------------------------------------------------

	public int getSourcePosition() {
		return sourcePosition;
	}

	// -------------------------------------------------------------------------------------------

	public int getTargetPosition() {
		return targetPosition;
	}

	// -------------------------------------------------------------------------------------------

	public SyntacticPatternGraphElement getNode(int position) {
		return this.nodes.get(position);
	}

}
