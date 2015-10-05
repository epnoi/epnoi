package org.epnoi.learner.relations.patterns;

import java.io.Serializable;


public interface RelationalPatternsModel extends Serializable  {
	public double calculatePatternProbability(
			RelationalPattern relationalPattern);
	public void show();
}
