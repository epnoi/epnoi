package org.epnoi.uia.learner.relations.lexical;

import java.util.HashMap;
import java.util.Map;

public class BigramSoftPatternModel implements SoftPatternModel {
	private Double[] nodePositionProbability;
	private Map<String, Double> nodeProbability;
	private Map<String, Map<String, Double>> bigramProbability;
	private LexicalRelationalModelCreationParameters parmeters;
	private int maxPatternLength;
	private LexicalRelationalModelCreationParameters parameters;

	// ---------------------------------------------------------------------------------------------------------

	protected BigramSoftPatternModel(
			LexicalRelationalModelCreationParameters parameters) {
		this.parameters = parameters;
		this.maxPatternLength = (Integer) this.parameters
				.getParameterValue(LexicalRelationalModelCreationParameters.MAX_PATTERN_LENGTH_PARAMETER);
		this.nodePositionProbability = new Double[maxPatternLength];
		this.nodeProbability = new HashMap<>();
		this.bigramProbability = new HashMap<>();
	}

	// ---------------------------------------------------------------------------------------------------------

	@Override
	public double calculatePatternProbability(
			LexicalRelationalPattern relationalPattern) {
		// TODO Auto-generated method stub
		return 0;
	}

	// ---------------------------------------------------------------------------------------------------------

}
