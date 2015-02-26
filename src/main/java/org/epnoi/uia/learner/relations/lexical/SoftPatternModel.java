package org.epnoi.uia.learner.relations.lexical;

import java.io.Serializable;


public interface SoftPatternModel extends Serializable {
	public double calculatePatternProbability(
			LexicalRelationalPattern relationalPattern);
}
