package org.epnoi.uia.learner.relations;

import java.util.ArrayList;
import java.util.List;

import org.epnoi.uia.learner.relations.lexical.LexicalRelationalPattern;

public class RelationalPatternsCorpus {
	private String type;
	private List<RelationalPattern> patterns;

	public RelationalPatternsCorpus() {
		this.patterns = new ArrayList<>();

	}

	// ---------------------------------------------------------------------------------

	public String getType() {
		return type;
	}

	// ---------------------------------------------------------------------------------

	public void setType(String type) {
		this.type = type;
	}

	// ---------------------------------------------------------------------------------

	public List<RelationalPattern> getPatterns() {
		return patterns;
	}

	// ---------------------------------------------------------------------------------

	public void setPatterns(List<RelationalPattern> patterns) {
		this.patterns = patterns;
	}

	// ---------------------------------------------------------------------------------
}
