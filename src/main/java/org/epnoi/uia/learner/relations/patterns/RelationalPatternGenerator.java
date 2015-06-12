package org.epnoi.uia.learner.relations.patterns;

import java.util.List;

import org.epnoi.uia.learner.relations.RelationalSentence;

public interface RelationalPatternGenerator {
	public List<RelationalPattern> generate(RelationalSentence sentence);
}
