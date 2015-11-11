package org.epnoi.learner.relations.patterns;

import org.epnoi.model.RelationalSentence;

import java.util.List;

public interface RelationalPatternGenerator {
	public List<RelationalPattern> generate(RelationalSentence sentence);
}
