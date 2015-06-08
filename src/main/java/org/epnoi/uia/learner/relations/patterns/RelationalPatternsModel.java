package org.epnoi.uia.learner.relations.patterns;

import java.io.Serializable;

import org.epnoi.uia.learner.relations.patterns.lexical.LexicalRelationalPattern;


public interface RelationalPatternsModel extends Serializable  {
	public double calculatePatternProbability(
			RelationalPattern relationalPattern);
	public void show();
}
