package org.epnoi.learner.relations.patterns.lexical;

import org.epnoi.learner.relations.patterns.RelationalPattern;

import java.util.ArrayList;
import java.util.List;

public class LexicalRelationalPattern implements RelationalPattern {
	private List<LexicalRelationalPatternNode> nodes;

	// ----------------------------------------------------------------------------------------------------

	public LexicalRelationalPattern() {
		this.nodes = new ArrayList<>();
	}

	// ----------------------------------------------------------------------------------------------------

	public List<LexicalRelationalPatternNode> getNodes() {
		return nodes;
	}

	// ----------------------------------------------------------------------------------------------------

	public void setNodes(List<LexicalRelationalPatternNode> nodes) {
		this.nodes = nodes;
	}

	// ----------------------------------------------------------------------------------------------------

	public Integer getLength() {
		return ((this.nodes == null) ? 0 : this.nodes.size());
	}

	// ----------------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		String nodesRepresentation = "";
		for (LexicalRelationalPatternNode node : nodes) {
			nodesRepresentation = nodesRepresentation + "|"
					+ "<"+node.getGeneratedToken()+","+node.getOriginialToken()+">";
		}
		return "LexicalRelationalPattern [" + nodesRepresentation + "]";

	}
	// ----------------------------------------------------------------------------------------------------

}
