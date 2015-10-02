package org.epnoi.learner.relations.patterns;

import java.util.ArrayList;
import java.util.List;

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

	@Override
	public String toString() {
		return "RelationalPatternsCorpus [type=" + type + ", patterns="
				+ patterns + "]";
	}

	// ---------------------------------------------------------------------------------

}
