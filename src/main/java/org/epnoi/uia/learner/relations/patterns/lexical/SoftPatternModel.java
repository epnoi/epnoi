package org.epnoi.uia.learner.relations.patterns.lexical;

import java.io.Serializable;


public interface SoftPatternModel extends Serializable {
	public double calculatePatternProbability(
			LexicalRelationalPattern relationalPattern);
}
