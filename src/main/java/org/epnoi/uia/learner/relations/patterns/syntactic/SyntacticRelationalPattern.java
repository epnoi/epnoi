package org.epnoi.uia.learner.relations.patterns.syntactic;

import java.util.ArrayList;
import java.util.List;

import org.epnoi.uia.learner.relations.patterns.RelationalPattern;
import org.jgrapht.GraphPath;

public class SyntacticRelationalPattern implements RelationalPattern {

	List<SyntacticPatternGraphElement> nodes;
	private int sourcePosition;
	private int targetPosition;

		
	
	// -------------------------------------------------------------------------------------------

	public SyntacticRelationalPattern() {
		this.nodes = new ArrayList<>();
	}

	// -------------------------------------------------------------------------------------------

	public SyntacticRelationalPattern(List<SyntacticPatternGraphElement> list) {
	this.nodes=list;
	}


	@Override
	public Integer getLength() {
		return this.nodes.size();
	}

	// -------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "SyntacticRelationalPattern [nodes=" + nodes + "]";
	}

	// -------------------------------------------------------------------------------------------

}
