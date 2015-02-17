package org.epnoi.uia.learner.relations.lexical;

import java.util.ArrayList;
import java.util.List;

import org.epnoi.uia.learner.relations.RelationalPattern;

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

	public int getLength(){
		return ((this.nodes==null)? 0 : this.nodes.size());
	}
	
	// ----------------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "LexicalRelationalPattern [nodes=" + nodes + "]";
	}

	// ----------------------------------------------------------------------------------------------------

}
