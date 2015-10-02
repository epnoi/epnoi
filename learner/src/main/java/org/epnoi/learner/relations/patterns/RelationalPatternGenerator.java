package org.epnoi.learner.relations.patterns;

import java.util.List;

import org.epnoi.model.RelationalSentence;

public interface RelationalPatternGenerator {
	public List<RelationalPattern> generate(RelationalSentence sentence);
}
