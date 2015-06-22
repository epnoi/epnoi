package org.epnoi.uia.learner.relations.patterns.lexical;

import java.util.ArrayList;
import java.util.List;

import org.epnoi.uia.learner.relations.patterns.RelationalPattern;
import org.hibernate.hql.internal.ast.tree.Node;

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
					+ node.getGeneratedToken();
		}
		return "LexicalRelationalPattern [" + nodesRepresentation + "]";

	}
	// ----------------------------------------------------------------------------------------------------

}
